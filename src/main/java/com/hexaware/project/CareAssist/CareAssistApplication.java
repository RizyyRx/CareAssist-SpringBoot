package com.hexaware.project.CareAssist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CareAssistApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareAssistApplication.class, args);
//	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//	    String encoded = encoder.encode("123");
//	    System.out.println(encoded);
	}

}
