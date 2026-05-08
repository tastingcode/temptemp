package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public UserInfo create(UserCommand.Create command) {
		if (userRepository.existsByLoginId(command.toLoginId())) {
			throw new CoreException(ErrorType.CONFLICT, "이미 가입된 ID입니다.");
		}

		if (userRepository.existsByEmail(command.toEmail())) {
			throw new CoreException(ErrorType.CONFLICT, "이미 가입된 이메일입니다.");
		}

		UserEntity user = UserEntity.from(command);

		return UserInfo.from(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public Optional<UserInfo> findUser(UserCommand.Find command){
		return userRepository.findById(command.userId()).map(UserInfo :: from);
	}

}
