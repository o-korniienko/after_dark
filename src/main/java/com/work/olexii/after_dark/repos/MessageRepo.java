package com.work.olexii.after_dark.repos;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
    Message findByUser(User user);
}
