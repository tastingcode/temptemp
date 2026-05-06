package com.loopers.application.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserFacade {
	private final UserService userService;

	@Transactional
	public UserResult joinUser(UserCriteria.Create criteria){
		UserCommand.Create command = criteria.toCommand();
		UserInfo userInfo = userService.create(command);
		return UserResult.from(userInfo);
	}

	public UserResult getUser(UserCriteria.Get get) {
		UserCommand.Find command = get.toCommand();
		UserInfo userInfo = userService.findUser(command).orElseThrow(() -> new CoreException(
				ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다: " + get.userId()
		));

		return UserResult.from(userInfo);
	}
}
