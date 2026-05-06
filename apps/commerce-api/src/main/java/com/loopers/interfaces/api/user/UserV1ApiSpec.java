package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.example.ExampleV1Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "User API 입니다.")
public interface UserV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "사용자 정보를 입력하여 회원가입 합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> joinUser(
        @Schema(name = "회원가입 요청", description = "회원가입에 필요한 사용자 정보")
        UserV1Dto.CreateRequest createRequest
    );

	@Operation(
			summary = "내 정보 조회",
			description = "X-USER-ID 헤더로 사용자 정보를 조회합니다."
	)
	ApiResponse<UserV1Dto.UserResponse> getMyInfo(
			@Schema(name = "사용자 ID", description = "사용자 ID")
			Long userId
	);
}
