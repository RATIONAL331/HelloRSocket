package com.example.rsocketspring.dto;

import com.example.rsocketspring.dto.error.ErrorEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Response<T> {
	private ErrorEvent errorResponse;
	private T successResponse;

	public boolean hasError() {
		return Objects.nonNull(this.errorResponse);
	}

	public Response(T successResponse) {
		this.successResponse = successResponse;
	}

	public Response(ErrorEvent errorResponse) {
		this.errorResponse = errorResponse;
	}

	public static <T> Response<T> with(T t) {
		return new Response<>(t);
	}

	public static <T> Response<T> with(ErrorEvent error) {
		return new Response<>(error);
	}
}
