package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointCriteria;
import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointResult;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {
	private final PointFacade pointFacade;

	@GetMapping("")
	@Override
	public ApiResponse<PointV1Dto.PointResponse> getPoint(@RequestHeader("X-USER-ID") Long userId) {
		PointCriteria.Get criteria = new PointCriteria.Get(userId);
		PointResult pointResult = pointFacade.findPoint(criteria);
		PointV1Dto.PointResponse response = PointV1Dto.PointResponse.from(pointResult);
		return ApiResponse.success(response);
	}

	@PostMapping("/charge")
	@Override
	public ApiResponse<PointV1Dto.PointResponse> chargePoint(@RequestBody PointV1Dto.ChargeRequest request) {
		PointCriteria.Charge criteria = request.toCriteria();
		PointResult pointResult = pointFacade.chargePoint(criteria);
		PointV1Dto.PointResponse response = PointV1Dto.PointResponse.from(pointResult);
		return ApiResponse.success(response);
	}
}
