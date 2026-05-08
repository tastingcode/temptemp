package com.loopers.application.point;

import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointInfo;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class PointFacadeIT {

	@Autowired
	private UserService userService;

	@Autowired
	private PointService pointService;

	@Autowired
	private PointFacade pointFacade;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	/**
	 * 해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
	 * 해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
	 */
	@DisplayName("포인트 조회")
	@Nested
	class Find {
		@DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
		@Test
		public void 해당_ID_의_회원이_존재할_경우_보유_포인트가_반환된다() {
			//given
			UserCommand.Create userCommand = new UserCommand.Create("asd123", "MALE", "2020-12-12", "asd123@asd.com");
			UserInfo userInfo = userService.create(userCommand);

			PointCommand.Init pointCommand = new PointCommand.Init(userInfo.id());
			PointInfo pointInfo = pointService.init(pointCommand);

			//when
			PointCriteria.Get pointCriteria = new PointCriteria.Get(userInfo.id());
			PointResult point = pointFacade.findPoint(pointCriteria);

			//then
			assertThat(point.amount()).isEqualTo(0L);

		}

		@DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
		@Test
		public void 해당_ID_의_회원이_존재하지_않을_경우_null_이_반환된다() {
			//given
			Long notExistUserId = -1L;
			PointCriteria.Get pointCriteria = new PointCriteria.Get(notExistUserId);

			//then
			CoreException coreException = assertThrows(CoreException.class, () -> {
				pointFacade.findPoint(pointCriteria);
			});

			assertEquals(ErrorType.NOT_FOUND, coreException.getErrorType());
		}
	}
	
	@DisplayName("포인트 충전")
	@Nested
	class Charge {
		@DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
		@Test
		public void 존재하지_않는_유저_ID_로_충전을_시도한_경우_실패한다() {
		    //given
			Long notExistUserId = -1L;
			PointCriteria.Charge pointCriteria = new PointCriteria.Charge(notExistUserId, 1000L);

			//then
			CoreException coreException = assertThrows(CoreException.class, () -> {
				pointFacade.chargePoint(pointCriteria);
			});

			assertEquals(ErrorType.NOT_FOUND, coreException.getErrorType());
		}
		
	}
	
}
