package com.work.olexii.after_dark.repos;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    Message findByUser(User user);

   List<Message> findByTag(String charter);
}
