package com.starboost.starboost_backend_demo;

import com.starboost.starboost_backend_demo.entity.Gender;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;

@SpringBootApplication

@EnableScheduling
public class StarboostBackendDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarboostBackendDemoApplication.class, args);
	}

	
	@Bean
	CommandLineRunner seedAdmin(UserRepository userRepo,
								PasswordEncoder encoder) {
		return args -> {
			
			if (userRepo.count() == 0) {
				User admin = User.builder()
						.firstName("Super")
						.lastName("Admin")
						.email("admin@starboost.com")
						.phoneNumber("0000000000")
						.gender(Gender.M)
						.dateOfBirth(LocalDate.of(1990, 1, 1))
						.role(Role.ADMIN)
						.agency(null)
						.region(null)
						.registrationNumber("REG-ADMIN-001")
						.password(encoder.encode("adminpass"))
						.build();

				userRepo.save(admin);
				System.out.println("✅ Seeded ADMIN user: " + admin.getEmail());
			}
		};
	}
}
