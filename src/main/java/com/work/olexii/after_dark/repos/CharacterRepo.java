package com.work.olexii.after_dark.repos;

import com.work.olexii.after_dark.domain.Character;
import com.work.olexii.after_dark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepo extends JpaRepository<Character,Long> {
    List<Character> findByUser(User user);

    Character findByName(String charName);
}
