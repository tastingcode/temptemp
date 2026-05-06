package com.loopers.application.user;

import com.loopers.domain.user.UserCommand;

public class UserCriteria {
	public record Create(String loginId, String gender, String birth, String email) {
		public UserCommand.Create toCommand() {
			return new UserCommand.Create(
					loginId,
					gender,
					birth,
					email
			);
		}
	}

	public record Get(Long userId) {
		public UserCommand.Find toCommand(){
			return new UserCommand.Find(userId);
		}
	}
}
