package com.loopers.domain.user;

public record UserInfo(
		Long id,
		String loginId,
		String gender,
		String birth,
		String email
) {
	public static UserInfo from(UserEntity user){
		return new UserInfo(
				user.getId(),
				user.getLoginId().getLoginId(),
				user.getGender().name(),
				user.getBirth().getBirth(),
				user.getEmail().getAddress()
		);
	}
}
