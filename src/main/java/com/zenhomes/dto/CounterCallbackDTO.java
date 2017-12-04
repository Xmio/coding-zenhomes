package com.zenhomes.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CounterCallbackDTO {

	@NotNull
	private Long counterId;
	@NotNull
	private Double amount;

}
