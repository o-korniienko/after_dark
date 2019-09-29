package com.work.olexii.after_dark.repos;

import com.work.olexii.after_dark.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, Long> {
}
