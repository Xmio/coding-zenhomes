package com.zenhomes.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenhomes.dto.VillageConsumptionDTO;
import com.zenhomes.exception.ElectricityCounterException;
import com.zenhomes.model.ElectricityCounter;
import com.zenhomes.service.ElectricityCounterService;

@RestController
public class ElectricityCounterController {

	@Autowired
	private ElectricityCounterService service;

	@RequestMapping(method = POST, path = "counter")
	private Long newCounter(@Valid @RequestBody ElectricityCounter eletricityCounter) {
		return service.createNewCounter(eletricityCounter);
	}

	@RequestMapping(method = GET, path = "counter/{counterId}")
	private ElectricityCounter retriveCounter(@PathVariable("counterId") Long counterId)
			throws ElectricityCounterException {
		return service.find(counterId);
	}

	@RequestMapping(method = GET, path = "consumption-report/{duration}")
	private List<VillageConsumptionDTO> retriveReport(@PathVariable("duration") Long duration) {
		return service.buildReport(duration);
	}

}
