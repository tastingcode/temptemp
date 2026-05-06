package com.loopers.interfaces.api;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.interfaces.api.example.ExampleV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserV1ApiE2ETest {

	private static final String ENDPOINT = "/api/v1/users";

	private final TestRestTemplate testRestTemplate;
	private final DatabaseCleanUp databaseCleanUp;
	private final UserRepository userRepository;


	@Autowired
	public UserV1ApiE2ETest(
			TestRestTemplate testRestTemplate,
			DatabaseCleanUp databaseCleanUp,
			UserRepository userRepository
	) {
		this.testRestTemplate = testRestTemplate;
		this.databaseCleanUp = databaseCleanUp;
		this.userRepository = userRepository;
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}


	/**
	 * - [x] 회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.
	 * - [x] 회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.
	 */
	@DisplayName("POST /api/v1/users")
	@Nested
	class Post {

		@DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다")
		@Test
		public void 회원_가입이_성공할_경우_생성된_유저_정보를_응답으로_반환한다() {
			//given
			UserV1Dto.CreateRequest createRequest = new UserV1Dto.CreateRequest(
					"asd123",
					"MALE",
					"2020-12-12",
					"asd123@asd.com"
			);

			//when
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(
					ENDPOINT,
					HttpMethod.POST,
					new HttpEntity<>(createRequest),
					responseType
			);

			//then
			Assertions.assertAll(
					() -> assertTrue(response.getStatusCode().is2xxSuccessful()),
					() -> assertThat(response.getBody().data().loginId()).isEqualTo(createRequest.loginId())
			);

		}

		@DisplayName("회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.")
		@Test
		public void 회원_가입_시에_성별이_없을_경우_400_Bad_Request_응답을_반환한다() {
			//given
			UserV1Dto.CreateRequest request = new UserV1Dto.CreateRequest("asd123", null, "2020-12-12", "asd123@asd.com");
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};

			//when
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(
					ENDPOINT,
					HttpMethod.POST,
					new HttpEntity<>(request),
					responseType
			);

			//then
			Assertions.assertAll(
					() -> assertTrue(response.getStatusCode().is4xxClientError()),
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
			);
		}
	}

	/**
	 * - [x]  내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.
	 * - [x]  존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.
	 */
	@DisplayName("GET /api/v1/users/me")
	@Nested
	class GetUser{
		@DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
		@Test
		public void 내_정보_조회에_성공할_경우_해당하는_유저_정보를_응답으로_반환한다() {
		    //given
			UserCommand.Create command = new UserCommand.Create("asd123", "MALE", "2020-12-12", "asd123@asd.com");
			UserEntity user = UserEntity.create(command);
			UserEntity savedUser = userRepository.save(user);

			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {};
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-USER-ID", savedUser.getId().toString());

			//when
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(
					ENDPOINT + "/me",
					HttpMethod.GET,
					new HttpEntity<>(headers),
					responseType
			);

			//then
			Assertions.assertAll(
					() -> assertTrue(response.getStatusCode().is2xxSuccessful()),
					() -> assertThat(response.getBody().data().id()).isEqualTo(savedUser.getId()),
					() -> assertThat(response.getBody().data().loginId()).isEqualTo(savedUser.getLoginId().getLoginId())
			);
		}
		
		@DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
		@Test
		public void 존재하지_않는_ID_로_조회할_경우_404_Not_Found_응답을_반환한다() {
		    //given
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {};
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-USER-ID", "-1");

		    //when
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(
					ENDPOINT + "/me",
					HttpMethod.GET,
					new HttpEntity<>(headers),
					responseType
			);
		    
		    //then
			Assertions.assertAll(
					() -> assertTrue(response.getStatusCode().is4xxClientError()),
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
			);
		    
		}
	}
}
