package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceIT {

	@Autowired
	private UserService userService;

	@MockitoSpyBean
	private UserRepository userRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	/**
	 * - [x] 회원 가입시 User 저장이 수행된다. ( spy 검증 )
	 * - [x] 이미 가입된 ID 로 회원가입 시도 시, 실패한다.
	 */
	@DisplayName("회원 가입 시")
	@Nested
	class register {
		@DisplayName("회원 가입시 User 저장이 수행된다. ( spy 검증 )")
		@Test
		public void 회원_가입시_User_저장이_수행된다() {
			//given
			UserCommand.Create command = new UserCommand.Create("asd123", "MALE", "2020-12-12", "asd@asd.com");

			//when
			UserInfo userInfo = userService.create(command);

			//then
			verify(userRepository).save(any(UserEntity.class));
			assertThat(userInfo.loginId()).isEqualTo("asd123");

		}

		@DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
		@Test
		public void 이미_가입된_ID_로_회원가입_시도_시_실패한다() {
			//given
			String existsId = "existsId";
			String email1 = "email1@asd.com";
			String email2 = "email2@asd.com";
			UserCommand.Create command1 = new UserCommand.Create(existsId, "MALE", "2020-12-12", email1);
			userService.create(command1);

			//when
			UserCommand.Create command2 = new UserCommand.Create(existsId, "MALE", "2020-12-12", email2);

			//then
			assertThrows(CoreException.class, () -> {
				userService.create(command2);
			});

		}
	}

	/**
	 * 해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.
	 * 해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
	 */
	@DisplayName("내 정보 조회")
	@Nested
	class Find {
		@DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
		@Test
		public void 해당_ID_의_회원이_존재할_경우_회원_정보가_반환된다() {
			//given
			UserCommand.Create command = new UserCommand.Create("asd123", "MALE", "2020-12-12", "asd123@asd.com");
			UserEntity user = UserEntity.from(command);

			//when
			UserEntity savedUser = userRepository.save(user);
			UserCommand.Find find = new UserCommand.Find(savedUser.getId());
			Optional<UserInfo> findUser = userService.findUser(find);

			//then
			assertAll(
					() -> assertThat(findUser).isNotEmpty(),
					() -> assertThat(findUser.get().id()).isEqualTo(savedUser.getId())
			);

		}

		@DisplayName("해당 ID 의 회원이 존재하지 않을 경우, 빈 Optional 이 반환된다.")
		@Test
		public void 해당_ID_의_회원이_존재하지_않을_경우_빈_Optional_이_반환된다() {
			//given
			UserCommand.Find find = new UserCommand.Find(-1L);

			//when
			Optional<UserInfo> findUser = userService.findUser(find);

			//then
			assertThat(findUser).isEmpty();

		}
	}


}
