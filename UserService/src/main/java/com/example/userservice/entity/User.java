package com.example.userservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	private String id;
	private String name;
	private int balance;

	public User(String name, int balance) {
		this.name = name;
		this.balance = balance;
	}
}
