package com.albert.vaadin_task;

import com.albert.vaadin_task.model.User;
import com.albert.vaadin_task.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;

@SpringBootApplication
public class VaadinTaskApplication {

	private static final Logger log = LoggerFactory.getLogger(VaadinTaskApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(VaadinTaskApplication.class);
	}

	@Bean
	public CommandLineRunner loadData(UserRepository repo, PasswordEncoder encoder) {
		return (args -> {
			// save a couple of users
			repo.save(new User("admin", "", "", null,
					"", "", "ROLE_ADMIN", encoder.encode("admin")));
			repo.save(new User("Albert", "Ivanov", "Konstantinovich",
					new Date(93, 0, 20),
					"ivanov.albert93@yandex.ru", "+79991112233",
					"ROLE_USER", encoder.encode("PASSWORD")));
			repo.save(new User("Pasha", "Ivkin", "Michailovich",
					new Date(80, 11, 25),
					"pasha999@task.ru", "+79999999999",
					"ROLE_USER", encoder.encode("PASSWORD")));
			repo.save(new User("Masha", "Petrova", "Yurievna",
					new Date(100, 2, 1),
					"mariya@task.ru", "+79888888888",
					"ROLE_USER", encoder.encode("PASSWORD")));
			repo.save(new User("Alexandr", "Kozlov", "Pavlovich",
					new Date(122, 8, 7),
					"alexandr@task.ru", "+78888888888",
					"ROLE_USER", encoder.encode("PASSWORD")));
			repo.save(new User("Ekaterina", "Barashkova", "Nikolaevna",
					new Date(99, 4, 5),
					"katya@task.ru", "+77777777777",
					"ROLE_USER", encoder.encode("PASSWORD")));
			repo.save(new User("Ksenia", "Boloban", "Ananievna",
					new Date(105, 10, 17),
					"ksusha@task.ru", "+71111111111",
					"ROLE_USER", encoder.encode("PASSWORD")));
		});
	}

}
