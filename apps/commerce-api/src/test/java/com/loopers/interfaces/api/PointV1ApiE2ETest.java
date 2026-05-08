package com.loopers.interfaces.api;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {
	private static final String ENDPOINT = "/api/v1/points";

	private final TestRestTemplate testRestTemplate;
	private final DatabaseCleanUp databaseCleanUp;
	private final UserRepository userRepository;
	private final PointRepository pointRepository;

	@Autowired
	public PointV1ApiE2ETest(
			TestRestTemplate testRestTemplate,
			DatabaseCleanUp databaseCleanUp,
			UserRepository userRepository,
			PointRepository pointRepository
	) {
		this.testRestTemplate = testRestTemplate;
		this.databaseCleanUp = databaseCleanUp;
		this.userRepository = userRepository;
		this.pointRepository = pointRepository;
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}


	/**
	 * 포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.
	 * X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.
	 */
	@DisplayName("GET /api/v1/points")
	@Nested
	class Find {

		@DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
		@Test
		public void 포인트_조회에_성공할_경우_보유_포인트를_응답으로_반환한다() {
			//given
			UserCommand.Create command = new UserCommand.Create("asd123", "MALE", "2020-12-12", "asd123@asd.com");
			UserEntity userEntity = UserEntity.from(command);

			UserEntity savedUser = userRepository.save(userEntity);
			PointEntity pointEntity = PointEntity.from(savedUser.getId());
			PointEntity savedPoint = pointRepository.save(pointEntity);

			HttpHeaders headers = new HttpHeaders();
			headers.add("X-USER-ID", savedUser.getId().toString());
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};

			//when

			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(
					ENDPOINT,
					HttpMethod.GET,
					new HttpEntity<>(headers),
					responseType
			);


			// then
			Assertions.assertAll(
					() -> assertTrue(response.getStatusCode().is2xxSuccessful()),
					() -> assertThat(response.getBody().data().userId()).isEqualTo(savedUser.getId()),
					() -> assertThat(response.getBody().data().amount()).isEqualTo(savedPoint.getAmount().getValue())
			);

		}

		@DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
		@Test
		public void X_USER_ID_헤더가_없을_경우_400_Bad_Request_응답을_반환한다() {
			//given
			HttpHeaders headers = new HttpHeaders();

			//when
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(
					ENDPOINT,
					HttpMethod.GET,
					new HttpEntity<>(headers),
					responseType
			);


			// then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		}

	}


	/**
	 * 존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.
	 * 존재하지 않는 유저로 요청할 경우, 404 Not Found 응답을 반환한다.
	 */
	@Nested
	@DisplayName("POST /api/v1/points/charge")
	class Charge {

		@DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
		@Test
		public void 존재하는_유저가_1000원을_충전할_경우_충전된_보유_총량을_응답으로_반환한다() {
			//given
			UserCommand.Create command = new UserCommand.Create("asd123", "MALE", "2020-12-12", "asd123@asd.com");
			UserEntity userEntity = UserEntity.from(command);

			UserEntity savedUser = userRepository.save(userEntity);
			PointEntity pointEntity = PointEntity.from(savedUser.getId());
			PointEntity savedPoint = pointRepository.save(pointEntity);

			Long chargePoint = 1000L;
			PointV1Dto.ChargeRequest chargeRequest = new PointV1Dto.ChargeRequest(savedUser.getId(), chargePoint);
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};

			//when
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(
					ENDPOINT + "/charge",
					HttpMethod.POST,
					new HttpEntity<>(chargeRequest),
					responseType
			);

			//then
			assertThat(response.getBody().data().amount()).isEqualTo(chargePoint);
		}
		
		@DisplayName("존재하지 않는 유저로 요청할 경우, 404 Not Found 응답을 반환한다.")
		@Test
		public void 존재하지_않는_유저로_요청할_경우_404_Not_Found_응답을_반환한다() {
		    //given
			Long notExistUserId = -1L;
			Long chargePoint = 1000L;
			PointV1Dto.ChargeRequest chargeRequest = new PointV1Dto.ChargeRequest(notExistUserId, chargePoint);
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};

			//when
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(
					ENDPOINT + "/charge",
					HttpMethod.POST,
					new HttpEntity<>(chargeRequest),
					responseType
			);

			//then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		}
	}
}
