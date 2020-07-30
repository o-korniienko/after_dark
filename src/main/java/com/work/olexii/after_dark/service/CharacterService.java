package com.work.olexii.after_dark.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.work.olexii.after_dark.domain.Character;
import com.work.olexii.after_dark.domain.Token;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.domain.dto.TokenResponse;
import com.work.olexii.after_dark.repos.CharacterRepo;
import com.work.olexii.after_dark.repos.TokenRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepo characterRepo;
    @Autowired
    private TokenRepo tokenRepo;
    public static Comparator<Character> BY_LEVEL;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Value("${spring.battle_net_id}")
    private String BNET_ID;
    @Value("${spring.battle_net_secret}")
    private String BNET_SECRET;

    static {
        BY_LEVEL = (o1, o2) -> (o2.getLevel() - o1.getLevel());
    }

    public CharacterService() {
    }

    public List<Character> addAllCharactersToDB() {
        List<Character> characters;
        characters = getGuildDataFromBlizzardDB();
        for (Character character : characters) {
            characterRepo.save(character);
        }

        return characters;
    }

    public Iterable<Character> findAll() {
        List<Character> characters = characterRepo.findAll();
        Collections.sort(characters, BY_LEVEL);
        return characters;
    }

    public Iterable<Character> getUserCharacters(User user) {
        List<Character> characters = characterRepo.findByUser(user);
        Collections.sort(characters, BY_LEVEL);
        return characters;
    }


    public Character findByName(String charName) {
        return characterRepo.findByName(charName);
    }

    public void save(Character character) {
        characterRepo.save(character);
    }

    public void deleteAllCharacters() {
        characterRepo.deleteAll();
    }

    private List<Character> getGuildDataFromBlizzardDB() {
        List<Character> characterList = new ArrayList<>();
        Character character;
        RestTemplate restTemplate = new RestTemplate();
        String token = getToken(BNET_ID, BNET_SECRET);
        System.out.println(token);
        if (token != null) {
            String url = "https://eu.api.blizzard.com/data/wow/guild/borean-tundra/после-тьмы/roster?namespace=profile-eu&locale=ru_RU&access_token=" + token;
            String stringPosts = restTemplate.getForObject(url, String.class);
            String[] charactersStrings = stringPosts.split("\"character\"");
            for (int r = 1; r < charactersStrings.length; r++) {
                String characterLink = charactersStrings[r];
                characterLink = characterLink.substring(17);
                String[] characterLinkArray = characterLink.split("\"}");
                String[] characterRankArray = characterLink.split("\"rank\"");
                String characterRank = characterRankArray[1];
                characterRank = characterRank.substring(1, 2);
                characterLink = characterLinkArray[0];

                character = getCharacterDataFromBlizzardAPI(characterLink, Integer.parseInt(characterRank));

                if (character != null) {
                    characterList.add(character);
                }
            }
        }
        System.out.println("Done");
        return characterList;
    }

    private Character getCharacterDataFromBlizzardAPI(String url, int rank) {
        Character character = null;
        RestTemplate restTemplate = new RestTemplate();
        String token = getToken(BNET_ID, BNET_SECRET);
        String response = null;
        if (token != null) {

            HttpURLConnection con;
            try {

                URL url1 = new URL(url + "&locale=ru_RU&access_token=" + token);
                con = (HttpURLConnection) url1.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if (responseCode != 200) {
                    System.out.println(con.getResponseMessage() + " " + con.getResponseCode());
                    System.out.println(url);
                    System.out.println(url1);
                }
                if (responseCode == 200) {
                    String name;
                    int lvl;
                    String classRu;
                    response = IOUtils.toString(con.getInputStream(), "UTF-8");

                    String[] characterArray = response.split("\"name\"");

                    name = characterArray[1].split(",")[0].substring(2).replace("\"", "");
                    classRu = characterArray[5].split(",")[1].substring(5).replace("}", "");
                    lvl = Integer.parseInt(response.split("\"level\":")[1].split(",")[0]);


                    character = new Character();
                    character.setName(name);
                    character.setClassEnByInt(Integer.parseInt(classRu));
                    character.setClassRuByInt(Integer.parseInt(classRu));
                    character.setRankByInt(rank);
                    character.setLevel(lvl);

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(response);
                e.printStackTrace();
            }
        }
        return character;
    }

    private String getToken(String id, String secret) {
        List<Token> tokens = tokenRepo.findAll();
        Token tokenFromDB = null;
        String token = null;
        if (tokens.size() != 0) {
            tokenFromDB = tokens.get(0);
            if (!isTokenExpired(tokenFromDB)) {
                token = tokenFromDB.getAccess_token();
                return token;
            } else {
                Token newToken = getTokenFromBlizzard(id, secret);
                token = newToken.getAccess_token();
                BeanUtils.copyProperties(newToken, tokenFromDB, "id");
                return token;
            }
        } else {
            Token newToken = getTokenFromBlizzard(id, secret);
            token = newToken.getAccess_token();
            tokenRepo.save(newToken);
        }
        return token;
    }

    private Token getTokenFromBlizzard(String id, String secret) {
        HttpURLConnection con;
        Token tokenToSave = new Token();
        try {
            String encodedCredentials = Base64.getEncoder().encodeToString(String.format("%s:%s", id,
                    secret).getBytes("UTF-8"));

            URL url1 = new URL("https://eu.battle.net/oauth/token");
            con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", String.format("Basic %s", encodedCredentials));
            con.setDoOutput(true);
            con.getOutputStream().write("grant_type=client_credentials".getBytes("UTF-8"));
            int responseCode = con.getResponseCode();
            System.out.println("response code: " + responseCode + " response message: " + con.getResponseMessage());
            if (responseCode == 200) {
                String response = IOUtils.toString(con.getInputStream(), "UTF-8");
                TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);

                tokenToSave.setAccess_token(tokenResponse.getAccess_token());
                tokenToSave.setExpires_in(tokenResponse.getExpires_in());
                tokenToSave.setCreateTime(LocalDateTime.now());
                return tokenToSave;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isTokenExpired(Token token) {
        long expiresIn = token.getExpires_in();
        LocalDateTime createTokenDate = token.getCreateTime();
        LocalDateTime dateTokenWorkUntil = createTokenDate.plusSeconds(expiresIn);
        LocalDateTime nowDate = LocalDateTime.now();
        if (nowDate.isAfter(dateTokenWorkUntil)) {
            return true;
        }

        return false;
    }

    public List<Character> updateCharacters() {
        List<Character> charactersFromBlizzardDB = getGuildDataFromBlizzardDB();
        List<Character> charactersFromOurDB = characterRepo.findAll();
        for (Character character : charactersFromOurDB) {
            if (!isCharacterInThisDB(character, charactersFromBlizzardDB)) {
                characterRepo.delete(character);
            }
        }

        for (Character character : charactersFromBlizzardDB) {
            if (!isCharacterInThisDB(character, charactersFromOurDB)) {
                characterRepo.save(character);
            }
        }

        for (Character our_character : charactersFromOurDB) {
            for (Character blizzard_character : charactersFromBlizzardDB) {
                if (our_character.getName().equals(blizzard_character.getName())) {
                    if (our_character.getRank() != blizzard_character.getRank() ||
                            our_character.getLevel() != blizzard_character.getLevel()) {
                        blizzard_character.setUser(our_character.getUser());
                        System.out.println("they are not the same");
                        System.out.println(our_character.getName());
                        System.out.println(
                                "levels - our:" + our_character.getLevel() + ", blizzard`s:" + blizzard_character.getLevel());
                        System.out.println(
                                "rank - our:" + our_character.getRank() + ", blizzard`s:" + blizzard_character.getRank());
                        BeanUtils.copyProperties(blizzard_character, our_character, "id");
                    }
                }
            }
        }

        return charactersFromOurDB;
    }

    private boolean isCharacterInThisDB(Character character, List<Character> characters) {
        boolean check = false;
        for (Character character_db : characters) {
            if (character.getName().equals(character_db.getName())) {
                check = true;
                break;
            }
        }


        return check;
    }
}
