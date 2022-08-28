package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.utils.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public Flux<UserDto> getAllUsers() {
		return userRepository.findAll()
		                     .map(EntityDtoUtil::toDto);
	}

	public Mono<UserDto> getUserById(String id) {
		return userRepository.findById(id)
		                     .map(EntityDtoUtil::toDto);
	}

	public Mono<UserDto> createUser(Mono<UserDto> userDtoMono) {
		return userDtoMono.map(EntityDtoUtil::toEntity)
		                  .flatMap(userRepository::save)
		                  .map(EntityDtoUtil::toDto);
	}

	public Mono<UserDto> updateUser(String id, Mono<UserDto> userDtoMono) {
		return userRepository.findById(id)
		                     .flatMap(u -> userDtoMono.map(EntityDtoUtil::toEntity)
		                                              .doOnNext(e -> e.setId(id)))
		                     .flatMap(userRepository::save)
		                     .map(EntityDtoUtil::toDto);
	}

	public Mono<Void> deleteUser(String id) {
		return userRepository.deleteById(id);
	}
}
