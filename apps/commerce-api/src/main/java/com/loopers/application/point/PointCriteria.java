package com.loopers.application.point;

import com.loopers.domain.point.PointCommand;
import com.loopers.domain.user.UserCommand;

public class PointCriteria {
	public record Get(Long userId){
		public PointCommand.Find toPointFind(){
			return new PointCommand.Find(userId);
		}

		public UserCommand.Find toUserFind(){
			return new UserCommand.Find(userId);
		}
	}

	public record Charge(Long userId, Long amount){
		public PointCommand.Charge toPointCharge(){
			return new PointCommand.Charge(userId, amount);
		}

		public UserCommand.Find toUserFind(){
			return new UserCommand.Find(userId);
		}
	}

}
