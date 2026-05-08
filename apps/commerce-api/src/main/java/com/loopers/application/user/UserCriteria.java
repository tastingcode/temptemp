package com.loopers.application.user;

import com.loopers.domain.point.PointCommand;
import com.loopers.domain.user.UserCommand;

public class UserCriteria {
	public record Join(String loginId, String gender, String birth, String email) {
		public UserCommand.Create toUserCreate() {
			return new UserCommand.Create(
					loginId,
					gender,
					birth,
					email
			);
		}
	}

	public record Get(Long userId) {
		public UserCommand.Find toUserFind(){
			return new UserCommand.Find(userId);
		}

		public PointCommand.Find toPointFind(){
			return new PointCommand.Find(userId);
		}
	}
}
