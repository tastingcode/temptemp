package com.loopers.interfaces.api.point;


import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "Point API 입니다.")
public interface PointV1ApiSpec {
	@Operation(
			summary = "포인트 조회",
			description = "X-USER-ID 헤더로 포인트 정보를 조회합니다."
	)
	ApiResponse<PointV1Dto.PointResponse> getPoint(
			@Schema(name = "사용자 ID", description = "사용자 ID")
			Long userId
	);

	@Operation(
			summary = "포인트 충전",
			description = "포인트 충전 API입니다."
	)
	ApiResponse<PointV1Dto.PointResponse> chargePoint(
			@Schema(name = "포인트 충전", description = "포인트 충전 정보")
			PointV1Dto.ChargeRequest request
	);


}
