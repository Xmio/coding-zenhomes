package com.zenhomes.exception;

import lombok.Getter;

@Getter
public class ElectricityCounterException extends Exception {

	private static final long serialVersionUID = 1L;

	public ElectricityCounterException(Long counterId) {
		super("Eletricity counter not found for id: " + counterId);
	}

}
