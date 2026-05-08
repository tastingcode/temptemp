package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
	@Embedded
	private LoginId loginId;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Embedded
	private Birth birth;
	@Embedded
	private Email email;


	public static UserEntity from(UserCommand.Create command){
		UserEntity userEntity = new UserEntity();

		userEntity.loginId = command.toLoginId();
		userEntity.gender = command.toGender();
		userEntity.birth = command.toBirth();
		userEntity.email = command.toEmail();

		return userEntity;
	}

}
