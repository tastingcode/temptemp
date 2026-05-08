package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserEntityTest {

	/**
	 * - [x] ID가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, User 객체 생성에 실패한다.
	 * - [x] 이메일이 `xx@yy.zz` 형식에 맞지 않으면, User 객체 생성에 실패한다.
	 * - [x] 생년월일이 `yyyy-MM-dd` 형식에 맞지 않으면, User 객체 생성에 실패한다.
	 */
	@Nested
	@DisplayName("User 생성")
	class Create {
		@DisplayName("ID가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"test1234567890",
				"!@#$%^"
		})
		void ID가_영문_및_숫자_10자_이내_형식에_맞지_않으면_User_객체_생성에_실패한다(String loginId) {
			String gender = "MALE";
			String birth = "2020-12-12";
			String email = "asd@asd.com";

			UserCommand.Create command = new UserCommand.Create(loginId, gender, birth, email);

			assertThrows(CoreException.class, () -> {
				UserEntity.from(command);
			});
		}

		@DisplayName("이메일이 `xx@yy.zz` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"asd@asd",
				"asd.com"
		})
		void 이메일_형식이_올바르지_않으면_User_객체_생성에_실패한다(String email) {
			String loginId = "temp123";
			String gender = "MALE";
			String birth = "2020-12-12";

			UserCommand.Create command = new UserCommand.Create(loginId, gender, birth, email);

			assertThrows(CoreException.class, () -> {
				UserEntity.from(command);
			});
		}

		@DisplayName("생년월일이 `yyyy-MM-dd` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"12345-33-44",
				"1111-1111"
		})
		void 생년월일_형식이_올바르지__않으면_User_객체_생성에_실패한다(String birth) {
			String loginId = "temp123";
			String gender = "MALE";
			String email = "temp@temp.com";

			UserCommand.Create command = new UserCommand.Create(loginId, gender, birth, email);


			assertThrows(CoreException.class, () -> {
				UserEntity.from(command);
			});
		}

	}

}
