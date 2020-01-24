package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.Character;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CharacterController {

    @Autowired
    CharacterService characterService;

    @PostMapping("/characters")
    public List<Character> addAllCharactersToDB( ){
        return characterService.addAllCharactersToDB();
    }

    @GetMapping("/characters")
    public Iterable<Character> getAllCharacters(){
        return characterService.findAll();
    }

    @GetMapping("/chars")
    public Iterable<Character> getUserCharacters(@AuthenticationPrincipal User user){
        System.out.println(user);
        return characterService.getUserCharacters(user);
    }

    @PostMapping("/chartouser")
    public String[] characterToUser(@AuthenticationPrincipal User user,
                                    @RequestParam("charName") String charName){
        String[] models = new String[1];
        Character character = characterService.findByName(charName);
        if (character==null){
            models[0] = "Персонаж не найден!";
            return models;
        }
        if (character.getUser()!=null){
            models[0] = "Данный персонаж уже прикреплен к пользователю. Если это ваш персонаж и вы его не добавляли, " +
                    "просьба обратится к администратору!";
            return models;
        }
        character.setUser(user);
        characterService.save(character);
        return models;
    }

    @DeleteMapping("/chars")
    public void deleteAllCharacters(){
     characterService.deleteAllCharacters();
    }

    @PutMapping("/characters")
    public List<Character> updateCharacters(){
        return characterService.updateCharacters();
    }

}
