package com.loopers.infrastructure.user;

import com.loopers.domain.user.Email;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByLoginId(LoginId loginId);

	boolean existsByEmail(Email email);
}
