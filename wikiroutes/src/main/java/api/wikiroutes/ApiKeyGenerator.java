package api.wikiroutes;

import java.util.UUID;

public class ApiKeyGenerator {
	
	public static String generate() {
		return UUID.randomUUID().toString();
	}
}
