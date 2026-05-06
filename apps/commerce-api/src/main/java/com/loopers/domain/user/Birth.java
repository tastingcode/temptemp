package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Birth {
	private static final Pattern BIRTH_PATTERN =
			Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

	private String birth;

	public Birth(String birth) {
		if (!BIRTH_PATTERN.matcher(birth).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식이어야 합니다.");
		}

		this.birth = birth;
	}

}
