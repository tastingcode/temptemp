package com.loopers.domain.user;

public class UserCommand {
	public record Create(String loginId, String gender, String birth, String email) {
		public LoginId toLoginId() {
			return new LoginId(loginId);
		}

		public Gender toGender() {
			return Gender.from(gender);
		}

		public Birth toBirth() {
			return new Birth(birth);
		}

		public Email toEmail() {
			return new Email(email);
		}
	}

	public record Find(Long userId) {

	}
}
