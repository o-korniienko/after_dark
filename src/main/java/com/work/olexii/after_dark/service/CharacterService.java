package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.*;
import com.work.olexii.after_dark.domain.Character;
import com.work.olexii.after_dark.repos.CharacterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepo characterRepo;
    public static Comparator<Character> BY_LEVEL;

    static {
        BY_LEVEL = new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return (o2.getLevel() - o1.getLevel());
            }
        };
    }

    public List<Character> addAllCharactersInDB() {
        Map<Integer, List<String>> characters = loadFromFile("after_dark-_ist.txt");
        List<Character> charactersList = new ArrayList<>();

        for (List<String> character : characters.values()) {
            System.out.println(character);
            Character character1 = new Character();
            character1.setName(character.get(0));
            character1.setLevel(Integer.parseInt(character.get(2)));
            setClassAndRang(character1, character);
            charactersList.add(character1);
            characterRepo.save(character1);
        }

        return charactersList;
    }

    public Map<Integer, List<String>> loadFromFile(String fileName) {
        Map<Integer, List<String>> map = new HashMap<>();
        int size = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("/");
                List<String> characters = new ArrayList<>(Arrays.asList(words));
                map.put(size, characters);
                size++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private void setClassAndRang(Character character, List<String> list) {
        switch (list.get(1)) {
            case "Паладин":
                character.setClassEn(ClassEn.Paladin);
                character.setClassRu(ClassRu.Паладин);
                break;
            case "Монах":
                character.setClassEn(ClassEn.Monk);
                character.setClassRu(ClassRu.Монах);
                break;
            case "Жрец":
                character.setClassEn(ClassEn.Priest);
                character.setClassRu(ClassRu.Жрец);
                break;
            case "Разбойник":
                character.setClassEn(ClassEn.Rogue);
                character.setClassRu(ClassRu.Разбойник);
                break;
            case "Охотник":
                character.setClassEn(ClassEn.Hunter);
                character.setClassRu(ClassRu.Охотник);
                break;
            case "Шаман":
                character.setClassEn(ClassEn.Shaman);
                character.setClassRu(ClassRu.Шаман);
                break;
            case "Друид":
                character.setClassEn(ClassEn.Druid);
                character.setClassRu(ClassRu.Друид);
                break;
            case "Чернокнижник":
                character.setClassEn(ClassEn.Warlock);
                character.setClassRu(ClassRu.Чернокнижник);
                break;
            case "Маг":
                character.setClassEn(ClassEn.Mage);
                character.setClassRu(ClassRu.Маг);
                break;
            case "Воин":
                character.setClassEn(ClassEn.Warrior);
                character.setClassRu(ClassRu.Воин);
                break;
            case "Рыцарь смерти":
                character.setClassEn(ClassEn.DeathKnight);
                character.setClassRu(ClassRu.Рыцарь_смерти);
                break;
            case "Охотник на демонов":
                character.setClassEn(ClassEn.DemonHunter);
                character.setClassRu(ClassRu.Охотник_на_демонов);
                break;

        }
        switch (list.get(3)) {
            case "0":
                character.setRank(Rank.Гильд_Мастер);
                break;
            case "1":
                character.setRank(Rank.Зам);
                break;
            case "2":
                character.setRank(Rank.Хранитель);
                break;
            case "3":
                character.setRank(Rank.Офицер);
                break;
            case "4":
                character.setRank(Rank.Рейдер);
                break;
            case "5":
                character.setRank(Rank.Ветеран);
                break;
            case "6":
                character.setRank(Rank.Мастер);
                break;
            case "7":
                character.setRank(Rank.Защитник);
                break;
            case "8":
                character.setRank(Rank.Игрок);
                break;
            case "9":
                character.setRank(Rank.Рекрут);
                break;
        }
    }


    public Iterable<Character> findAll() {
        List<Character> characters = characterRepo.findAll();
        Collections.sort(characters, BY_LEVEL);
        return characters;
    }

    public Iterable<Character> getYourCharacters(User user) {
        List<Character> characters = characterRepo.findByUser(user);
        Collections.sort(characters, BY_LEVEL);
        for (Character character : characters) {
            System.out.println(character);
        }
        return characters;
    }


    public Character findByName(String charName) {
        return characterRepo.findByName(charName);
    }

    public void save(Character character) {
        characterRepo.save(character);
    }

}
