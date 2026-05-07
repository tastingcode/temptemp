package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
	private final PointJpaRepository pointJpaRepository;

	@Override
	public PointEntity save(PointEntity point) {
		return pointJpaRepository.save(point);
	}

	@Override
	public Optional<PointEntity> findByUserId(Long userId) {
		return pointJpaRepository.findByUserId(userId);
	}

}
