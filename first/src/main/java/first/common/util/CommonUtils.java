package first.common.util;

import java.util.UUID;

public class CommonUtils {
	public static String getRandomString() { // 32글자의 랜덤 문자열을 만들어준다.
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
