package com.rational331.rsocket.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChartResponseDto {
	private int input;
	private int output;

	@Override
	public String toString() {
		String graphFormat = getFormat(this.output);
		return String.format(graphFormat, input, "X");
	}

	private String getFormat(int value) {
		return "%3s|%" + value + "s";
	}
}
