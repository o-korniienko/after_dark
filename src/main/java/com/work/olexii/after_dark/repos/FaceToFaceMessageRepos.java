package com.work.olexii.after_dark.repos;

import com.work.olexii.after_dark.domain.FaceToFaceMessage;
import com.work.olexii.after_dark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaceToFaceMessageRepos extends JpaRepository<FaceToFaceMessage,Long> {

    List<FaceToFaceMessage> findAllByFromUser(User user);
}
