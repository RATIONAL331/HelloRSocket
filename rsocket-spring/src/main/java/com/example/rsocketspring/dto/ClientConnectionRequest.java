package com.example.rsocketspring.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientConnectionRequest {
	private String clientId;
	private String secretKey;
}
