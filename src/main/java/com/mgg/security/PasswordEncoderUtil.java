package com.mgg.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("mgg2023");
		System.out.println("mgg2023 "+encoded);
		System.out.println("mgg2023 "+encoder.matches("mgg2023", encoded));
		System.out.println("mgg"+encoder.matches("mgg", encoded));
	}
}