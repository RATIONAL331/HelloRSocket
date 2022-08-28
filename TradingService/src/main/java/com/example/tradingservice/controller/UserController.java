package com.example.tradingservice.controller;

import com.example.tradingservice.client.UserClient;
import com.example.tradingservice.dto.UserStockDto;
import com.example.tradingservice.dto.user.UserDto;
import com.example.tradingservice.service.UserStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
	private final UserClient userClient;
	private final UserStockService userStockService;

	@GetMapping("all")
	public Flux<UserDto> allUsers() {
		return userClient.allUsers();
	}

	@GetMapping("{userId}/stocks")
	public Flux<UserStockDto> getUserStocks(@PathVariable String userId) {
		return userStockService.getUserStocks(userId);
	}

}
