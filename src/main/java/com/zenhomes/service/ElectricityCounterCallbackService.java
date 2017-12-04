package com.zenhomes.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenhomes.dto.CounterCallbackDTO;
import com.zenhomes.exception.ElectricityCounterException;
import com.zenhomes.model.ElectricityCounter;
import com.zenhomes.model.ElectricityCounterCallback;

@Service
@Transactional
public class ElectricityCounterCallbackService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ElectricityCounterService electricityCounterService;

	public Long createNewCounterCallback(CounterCallbackDTO counterCallbackDTO) throws ElectricityCounterException {
		ElectricityCounter electricityCounter = electricityCounterService.find(counterCallbackDTO.getCounterId());
		ElectricityCounterCallback counterCallback = ElectricityCounterCallback.builder()
				.amount(counterCallbackDTO.getAmount()).electricityCounter(electricityCounter).build();
		entityManager.persist(counterCallback);
		return counterCallback.getId();
	}

}
