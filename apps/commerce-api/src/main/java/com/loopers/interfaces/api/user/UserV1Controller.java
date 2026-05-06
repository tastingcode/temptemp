package com.loopers.interfaces.api.user;

import com.loopers.application.example.ExampleFacade;
import com.loopers.application.example.ExampleInfo;
import com.loopers.application.user.UserCriteria;
import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.example.ExampleV1ApiSpec;
import com.loopers.interfaces.api.example.ExampleV1Dto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

	private final UserFacade userFacade;

	@PostMapping("")
	@Override
	public ApiResponse<UserV1Dto.UserResponse> joinUser(@Valid @RequestBody UserV1Dto.CreateRequest createRequest) {
		UserCriteria.Create criteria = createRequest.toCriteria();
		UserResult userResult = userFacade.joinUser(criteria);
		UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userResult);
		return ApiResponse.success(response);
	}

	@GetMapping("/me")
	@Override
	public ApiResponse<UserV1Dto.UserResponse> getMyInfo(@RequestHeader("X-USER-ID") Long userId) {
		UserResult userResult = userFacade.getUser(new UserCriteria.Get(userId));
		UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userResult);
		return ApiResponse.success(response);
	}
}
