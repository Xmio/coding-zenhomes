package com.zenhomes.service;

import static java.util.stream.Collectors.groupingBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenhomes.dto.VillageConsumptionDTO;
import com.zenhomes.exception.ElectricityCounterException;
import com.zenhomes.model.ElectricityCounter;
import com.zenhomes.model.ElectricityCounterCallback;
import com.zenhomes.repository.ElectricityCounterRepository;

@Service
@Transactional
public class ElectricityCounterService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ElectricityCounterRepository repository;

	public Long createNewCounter(ElectricityCounter electricityCounter) {
		entityManager.persist(electricityCounter);
		return electricityCounter.getId();
	}

	public ElectricityCounter find(Long counterId) throws ElectricityCounterException {
		ElectricityCounter electricityCounter = entityManager.find(ElectricityCounter.class, counterId);
		if (electricityCounter == null)
			throw new ElectricityCounterException(counterId);
		return electricityCounter;
	}

	public List<VillageConsumptionDTO> buildReport(Long duration) {
		LocalDateTime reportPeriod = LocalDateTime.now().minusHours(duration);
		List<ElectricityCounterCallback> retrivedCallbacks = repository.findCallbackAfter(reportPeriod);
		Map<ElectricityCounter, List<ElectricityCounterCallback>> counters = retrivedCallbacks.stream()
				.collect(groupingBy(ElectricityCounterCallback::getElectricityCounter));
		List<VillageConsumptionDTO> villagesConsumption = new ArrayList<VillageConsumptionDTO>();
		counters.forEach((counter, callbacks) -> villagesConsumption.add(buildVillageConsumption(counter, callbacks)));
		return villagesConsumption.stream()
				.sorted((dto1, dto2) -> dto1.getConsumption().compareTo(dto2.getConsumption()))
				.collect(Collectors.toList());
	}

	private VillageConsumptionDTO buildVillageConsumption(ElectricityCounter counter,
			List<ElectricityCounterCallback> callbacks) {
		return VillageConsumptionDTO.builder().villageName(counter.getVillageName())
				.consumption(calculateConsumption(callbacks)).build();
	}

	private BigDecimal calculateConsumption(List<ElectricityCounterCallback> callbacks) {
		Double maxAmount = callbacks.stream().mapToDouble(ElectricityCounterCallback::getAmount).max().orElse(0);
		Double minAmount = callbacks.stream().mapToDouble(ElectricityCounterCallback::getAmount).min().orElse(0);
		return BigDecimal.valueOf(maxAmount).subtract(BigDecimal.valueOf(minAmount));
	}

}
