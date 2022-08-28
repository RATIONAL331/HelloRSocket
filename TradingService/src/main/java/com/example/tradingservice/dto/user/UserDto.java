package com.example.tradingservice.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
	private String id;
	private String name;
	private int balance;
}
