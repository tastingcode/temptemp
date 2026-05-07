package com.loopers.domain.point;

import com.loopers.infrastructure.point.PointJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

	@InjectMocks
	private PointService pointService;

	@Mock
	private PointRepository pointRepository;

	/**
	 * 포인트 충전 시 포인트가 반환된다.
	 */
	@Nested
	@DisplayName("포인트 충전 시 포인트가 반환된다.")
	class Charge {
		@DisplayName("포인트 충전 시 포인트가 반환된다.")
		@Test
		public void 포인트_충전_시_포인트가_반환된다() {
		    //given
			Long userId = 100L;
			Long chargeAmount = 100000L;
			PointEntity targetPoint = PointEntity.from(userId);
			given(pointRepository.findByUserId(userId))
					.willReturn(Optional.of(targetPoint));

			given(pointRepository.save(any(PointEntity.class)))
					.willAnswer(invocation -> invocation.getArgument(0));

		    //when
			PointInfo savedPoint = pointService.charge(new PointCommand.Charge(userId, chargeAmount));

			//then
			assertAll(
					() -> assertThat(savedPoint.userId()).isEqualTo(userId),
					() -> assertThat(savedPoint.amount()).isEqualTo(chargeAmount)
			);

		}
	}


}
