package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
	UserEntity save(UserEntity user);

	boolean existsByLoginId(LoginId loginId);

	boolean existsByEmail(Email email);

	Optional<UserEntity> findById(Long userId);
}
