package com.loopers.domain.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointService {
	private final PointRepository pointRepository;

	@Transactional
	public PointInfo init(PointCommand.Init command){
		PointEntity point = PointEntity.from(command.userId());
		PointEntity pointEntity = pointRepository.save(point);
		return PointInfo.from(pointEntity);
	}

	@Transactional
	public PointInfo charge(PointCommand.Charge command){
		PointEntity point = pointRepository.findByUserId(command.userId())
				.orElse(PointEntity.from(command.userId()));

		point.charge(command.amount());
		PointEntity savedPoint = pointRepository.save(point);
		return PointInfo.from(savedPoint);
	}

	@Transactional(readOnly = true)
	public Optional<PointInfo> findPoint(PointCommand.Find command){
		return pointRepository.findByUserId(command.userId()).map(PointInfo::from);

	}
}
