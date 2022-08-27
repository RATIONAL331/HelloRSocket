package com.example.rsocketspring.client.serviceregistry;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RSocketServiceInstance {
	private String host;
	private int port;
}
