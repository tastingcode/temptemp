package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	UserRepository userRepository;


	/**
	 * - [x] 이미 존재하는 ID가 주어지면, CONFLICT 예외가 발생한다.
	 * - [x] 이미 존재하는 Email이 주어지면, CONFLICT 예외가 발생한다.
	 */
	@Nested
	@DisplayName("유저 생성")
	class Create {
		@DisplayName("이미 존재하는 ID가 주어지면, CONFLICT 예외가 발생한다.")
		@Test
		public void 이미_존재하는_ID가_주어지면_CONFLICT_예외가_발생한다() {
			//given
			UserCommand.Create command = new UserCommand.Create("tempId", "Male", "2000-12-12", "asd@asd.com");
			given(userRepository.existsByLoginId(command.toLoginId()))
					.willReturn(true);

			assertThatThrownBy(() -> userService.create(command))
					.isInstanceOf(CoreException.class)
					.extracting("errorType")
					.isEqualTo(ErrorType.CONFLICT);
		}

		@DisplayName("이미 존재하는 Email이 주어지면, CONFLICT 예외가 발생한다.")
		@Test
		public void 이미_존재하는_Email이_주어지면_CONFLICT_예외가_발생한다() {
			//given
			UserCommand.Create command = new UserCommand.Create("tempId", "Male", "2000-12-12", "asd@asd.com");
			given(userRepository.existsByLoginId(command.toLoginId()))
					.willReturn(false);

			given(userRepository.existsByEmail(command.toEmail()))
					.willReturn(true);

			assertThatThrownBy(() -> userService.create(command))
					.isInstanceOf(CoreException.class)
					.extracting("errorType")
					.isEqualTo(ErrorType.CONFLICT);
		}

	}

	/**
	 * - [x] 존재하지 않는 유저 ID로 조회하면, 빈 Optional을 반환한다.
	 */
	@DisplayName("회원 정보 조회 시,")
	@Nested
	class Find {
		@DisplayName("존재하지 않는 유저 ID로 조회하면, 빈 Optional을 반환한다.")
		@Test
		public void 존재하지_않는_유저_ID로_조회하면_빈_Optional을_반환한다() {
		    //given
			given(userRepository.findById(-1L))
					.willReturn(Optional.empty());

		    //when
			UserCommand.Find command = new UserCommand.Find(-1L);
			Optional<UserInfo> user = userService.findUser(command);

			//then
			assertThat(user).isEmpty();

		}
	}

}
