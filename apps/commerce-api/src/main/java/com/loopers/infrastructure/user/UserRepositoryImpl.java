package com.loopers.infrastructure.user;

import com.loopers.domain.user.Email;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public UserEntity save(UserEntity user) {
		return userJpaRepository.save(user);
	}

	@Override
	public boolean existsByLoginId(LoginId loginId) {
		return userJpaRepository.existsByLoginId(loginId);
	}

	@Override
	public boolean existsByEmail(Email email) {
		return userJpaRepository.existsByEmail(email);
	}

	@Override
	public Optional<UserEntity> findById(Long userId) {
		return userJpaRepository.findById(userId);
	}
}
