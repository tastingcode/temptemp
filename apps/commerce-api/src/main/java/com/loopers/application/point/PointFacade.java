package com.loopers.application.point;

import com.loopers.domain.point.PointInfo;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PointFacade {

	private final UserService userService;
	private final PointService pointService;

	@Transactional(readOnly = true)
	public PointResult findPoint(PointCriteria.Get criteria){
		UserInfo userInfo = userService.findUser(criteria.toUserFind()).orElseThrow(() -> new CoreException(
				ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다: " + criteria.userId()
		));

		PointInfo pointInfo = pointService.findPoint(criteria.toPointFind()).orElseThrow(() -> new CoreException(
				ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다: " + criteria.userId()
		));

		return PointResult.from(pointInfo);
	}

	@Transactional
	public PointResult chargePoint(PointCriteria.Charge criteria){
		UserInfo userInfo = userService.findUser(criteria.toUserFind()).orElseThrow(() -> new CoreException(
				ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다: " + criteria.userId()
		));

		PointInfo pointInfo = pointService.charge(criteria.toPointCharge());
		return PointResult.from(pointInfo);
	}

}
