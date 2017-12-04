package com.zenhomes.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenhomes.dto.CounterCallbackDTO;
import com.zenhomes.exception.ElectricityCounterException;
import com.zenhomes.service.ElectricityCounterCallbackService;

@RestController
public class EletricityCounterCallbackController {

	@Autowired
	private ElectricityCounterCallbackService service;

	@RequestMapping(method = POST, path = "counter-callback")
	private Long newCounterCallback(@Valid @RequestBody CounterCallbackDTO counterCallbackDTO)
			throws ElectricityCounterException {
		return service.createNewCounterCallback(counterCallbackDTO);
	}

}
