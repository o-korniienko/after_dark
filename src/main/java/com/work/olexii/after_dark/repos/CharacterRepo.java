package com.work.olexii.after_dark.repos;

import com.work.olexii.after_dark.domain.Character;
import com.work.olexii.after_dark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepo extends JpaRepository<Character,Long> {
    Iterable<Character> findByUser(User user);

    Character findByName(String charName);
}
