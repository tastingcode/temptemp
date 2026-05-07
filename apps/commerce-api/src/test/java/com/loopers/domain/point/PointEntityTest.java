package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointEntityTest {


	/**
	 * - [x] 0 이하의 정수로 포인트를 충전 시 실패한다.
	 */
	@Nested
	@DisplayName("포인트 충전")
	class Charge {
		@DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
		@ParameterizedTest
		@ValueSource(longs = {-1, 0})
		public void 이하의_정수로_포인트를_충전_시_실패한다(Long amount) {
		    //given
			PointEntity point = PointEntity.from(1L);

		    //then
			assertThrows(CoreException.class, () -> {
				point.charge(amount);
			});

		}
		
		@DisplayName("포인트 충전 후 포인트가 반환된다.")
		@Test
		public void 포인트_충전_후_포인트가_반환된다() {
		    //given
			PointEntity point = PointEntity.from(1L);
			Long amount = 100L;

			//when
			PointEntity chargedPoint = point.charge(amount);

			//then
			assertThat(chargedPoint.getAmount().getValue()).isEqualTo(amount);

		}

	}


}
