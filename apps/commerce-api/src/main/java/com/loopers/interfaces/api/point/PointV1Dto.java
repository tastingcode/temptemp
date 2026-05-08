package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointCriteria;
import com.loopers.application.point.PointResult;

public class PointV1Dto {
	public record ChargeRequest(Long userId, Long amount){
		public PointCriteria.Charge toCriteria(){
			return new PointCriteria.Charge(userId, amount);
		}
	}

	public record PointResponse(Long userId, Long amount){
		public static PointResponse from(PointResult pointResult){
			return new PointResponse(pointResult.userId(), pointResult.amount());
		}
	}
}
