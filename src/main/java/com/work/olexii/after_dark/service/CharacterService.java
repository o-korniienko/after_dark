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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private static final String BNET_ID = "9125eaa751bf43f5aee173f0d8d50efd";
    private static final String BNET_SECRET = "AHfMwyqoBADo244amT1jRfTg1Ll0JrMw";

    static {
        BY_LEVEL = (o1, o2) -> (o2.getLevel() - o1.getLevel());
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
        RestTemplate restTemplate = new RestTemplate();
        String token = getToken(BNET_ID, BNET_SECRET);
        System.out.println(token);
        if (token != null) {
            String url = "https://eu.api.blizzard.com/wow/guild/borean-tundra/" +
                    "После Тьмы?fields=members&locale=ru_RU&access_token=" + token;
            String stringPosts = restTemplate.getForObject(url, String.class);
            String[] charactersStrings = stringPosts.split("character");
            for (int r = 1; r < charactersStrings.length; r++) {
                String characterStr = charactersStrings[r];
                if (r == charactersStrings.length - 1) {
                    characterStr = characterStr.split("\"emblem\":")[0];
                    characterStr = characterStr.substring(3, characterStr.length() - 3);
                } else {
                    characterStr = characterStr.substring(3, characterStr.length() - 4);
                }
                Character character = new Character();
                String[] characterFields = characterStr.split(",");
                for (int i = 0; i < characterFields.length; i++) {
                    String[] characterFields2 = characterFields[i].split(":");
                    characterFields2[0] = characterFields2[0].substring(1, characterFields2[0].length() - 1);
                    if (characterFields2[0].equals("name")) {
                        character.setName(characterFields2[1].substring(1, characterFields2[1].length() - 1));
                    }
                    if (characterFields2[0].equals("level")) {
                        character.setLevel(Integer.parseInt(characterFields2[1]));
                    }
                    if (characterFields2[0].equals("class")) {
                        character.setClassEnByInt(Integer.parseInt(characterFields2[1]));
                        character.setClassRuByInt(Integer.parseInt(characterFields2[1]));
                    }
                    if (characterFields2[0].equals("rank")) {
                        character.setRankByInt(Integer.parseInt(characterFields2[1]));
                    }
                }
                characterList.add(character);
            }
        }
        return characterList;
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
            }else{
                Token newToken = getTokenFromBlizzard(id,secret);
                token = newToken.getAccess_token();
                BeanUtils.copyProperties(newToken, tokenFromDB, "id");
                return token;
            }
        }else {
            Token newToken = getTokenFromBlizzard(id,secret);
            token = newToken.getAccess_token();
            tokenRepo.save(newToken);
        }
        return token;
    }

    private Token getTokenFromBlizzard(String id, String secret ){
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
