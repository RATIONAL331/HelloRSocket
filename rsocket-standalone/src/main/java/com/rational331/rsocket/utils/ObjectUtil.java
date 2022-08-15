package com.rational331.rsocket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;

public class ObjectUtil {
	public static Payload toPayload(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			byte[] bytes = objectMapper.writeValueAsBytes(object);
			return DefaultPayload.create(bytes);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T toObject(Payload payload, Class<T> type) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			byte[] bytes = payload.getData().array();
			return objectMapper.readValue(bytes, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
