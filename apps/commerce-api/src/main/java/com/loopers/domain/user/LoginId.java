package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class LoginId {
	private static final Pattern USER_ID_PATTERN =
			Pattern.compile("^[a-zA-Z0-9]{1,10}$");

	private String loginId;

	public LoginId(String loginId) {
		if (!USER_ID_PATTERN.matcher(loginId).matches()){
			throw new CoreException(ErrorType.BAD_REQUEST, "ID 형식이 올바르지 않습니다.");
		}

		this.loginId = loginId;
	}

}
