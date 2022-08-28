package com.example.userservice.controller;

import com.example.userservice.dto.OperationType;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// RS
	@MessageMapping("get.all")
	public Flux<UserDto> allUsers() {
		return userService.getAllUsers();
	}

	// RR
	@MessageMapping("get.{id}")
	public Mono<UserDto> getUserById(@DestinationVariable String id) {
		return userService.getUserById(id);
	}

	// RR
	@MessageMapping("create")
	public Mono<UserDto> createUser(Mono<UserDto> userDtoMono) {
		return userService.createUser(userDtoMono);
	}

	// RR
	@MessageMapping("update.{id}")
	public Mono<UserDto> updateUser(@DestinationVariable String id, Mono<UserDto> userDtoMono) {
		return userService.updateUser(id, userDtoMono);
	}

	// FF
	@MessageMapping("delete.{id}")
	public Mono<Void> deleteUser(@DestinationVariable String id) {
		return userService.deleteUser(id);
	}

	@MessageMapping("operation.type")
	public Mono<Void> metadataOperationType(@Header("operation-type") OperationType operationType,
	                                        Mono<UserDto> userDtoMono) {
		System.out.println(operationType);
		userDtoMono.doOnNext(System.out::println).subscribe();
		return Mono.empty();
	}
}
