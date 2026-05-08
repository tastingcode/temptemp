package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEntity extends BaseEntity {

	@Column(nullable = false, unique = true)
	private Long userId;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "amount", nullable = false))
	private Amount amount;

	public static PointEntity from(Long userId) {
		PointEntity point = new PointEntity();

		point.userId = userId;
		point.amount = new Amount(0L);
		return point;
	}

	public PointEntity charge(Long amount){
		this.amount.charge(amount);
		return this;
	}


}
