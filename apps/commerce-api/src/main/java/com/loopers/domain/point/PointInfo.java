package com.loopers.domain.point;

public record PointInfo(Long userId, Long amount) {
	public static PointInfo from(PointEntity point) {
		return new PointInfo(
				point.getUserId(),
				point.getAmount().getValue()
		);
	}
}
