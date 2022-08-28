package com.example.userservice.config;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
@RequiredArgsConstructor
public class DataSetupConfig implements CommandLineRunner {
	private final UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		User sam = new User("sam", 10000);
		User mike = new User("mike", 10000);
		User jake = new User("jake", 10000);

		Flux.just(sam, mike, jake)
		    .flatMap(userRepository::save)
		    .doOnNext(System.out::println)
		    .subscribe();
	}
}
