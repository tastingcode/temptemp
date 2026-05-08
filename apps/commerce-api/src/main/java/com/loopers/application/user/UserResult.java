package com.loopers.application.user;

import com.loopers.domain.point.PointInfo;
import com.loopers.domain.user.UserInfo;

public record UserResult(
		Long id,
		String loginId,
		String gender,
		String birth,
		String email,
		Long point
) {
	public static UserResult of(UserInfo userInfo, PointInfo pointInfo){
		return new UserResult(
				userInfo.id(),
				userInfo.loginId(),
				userInfo.gender(),
				userInfo.birth(),
				userInfo.email(),
				pointInfo.amount()
		);
	}
}
