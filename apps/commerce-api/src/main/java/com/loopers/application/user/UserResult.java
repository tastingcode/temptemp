package com.loopers.application.user;

import com.loopers.domain.user.UserInfo;

public record UserResult(
		Long id,
		String loginId,
		String gender,
		String birth,
		String email
) {
	public static UserResult from(UserInfo userInfo){
		return new UserResult(
				userInfo.id(),
				userInfo.loginId(),
				userInfo.gender(),
				userInfo.birth(),
				userInfo.email()
		);
	}
}
