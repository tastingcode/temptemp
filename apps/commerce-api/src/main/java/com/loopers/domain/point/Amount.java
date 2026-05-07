package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Amount {

	private Long value;


	public Amount(Long amount){
		if (amount == null || amount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0 이상이어야 합니다.");
		}

		this.value = amount;
	}

	public void charge(Long amount){
		if (amount == null || amount <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "0 이하의 포인트는 충전할 수 없습니다.");
		}

		this.value += amount;
	}

}
