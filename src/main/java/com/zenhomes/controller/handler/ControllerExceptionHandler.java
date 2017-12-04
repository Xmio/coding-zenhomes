package com.zenhomes.controller.handler;

import static java.awt.TrayIcon.MessageType.ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.zenhomes.dto.MessageDTO;
import com.zenhomes.exception.ElectricityCounterException;

import lombok.extern.log4j.Log4j;

@ControllerAdvice
@Log4j
public class ControllerExceptionHandler {

	@ExceptionHandler(ElectricityCounterException.class)
	@ResponseStatus(BAD_REQUEST)
	public @ResponseBody MessageDTO handleException(ElectricityCounterException exception) {
		log.error(exception.getMessage(), exception);
		return new MessageDTO(exception.getMessage(), ERROR);
	}

}