package com.example.userservice.utils;

import com.example.userservice.dto.TransactionRequest;
import com.example.userservice.dto.TransactionResponse;
import com.example.userservice.dto.TransactionStatus;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
	public static UserDto toDto(User user) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

	public static User toEntity(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		return user;
	}

	public static TransactionResponse toResponse(TransactionRequest request, TransactionStatus status) {
		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setAmount(request.getAmount());
		transactionResponse.setType(request.getType());
		transactionResponse.setUserId(request.getUserId());
		transactionResponse.setStatus(status);
		return transactionResponse;
	}
}
