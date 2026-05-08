package com.loopers.domain.point;

public class PointCommand {

	public record Init(Long userId){
	}

	public record Charge(Long userId, Long amount) {
	}

	public record Find(Long userId) {
	}
}
