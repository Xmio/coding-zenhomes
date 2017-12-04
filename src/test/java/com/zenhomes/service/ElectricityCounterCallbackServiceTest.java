package com.zenhomes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.zenhomes.dto.CounterCallbackDTO;
import com.zenhomes.exception.ElectricityCounterException;
import com.zenhomes.model.ElectricityCounter;
import com.zenhomes.model.ElectricityCounterCallback;

@RunWith(MockitoJUnitRunner.class)
public class ElectricityCounterCallbackServiceTest {

	private static final Long DEFAULT_COUNTER_ID = 1L;
	private static final String VILLAGE_NAME = "Villarriba";
	private static final Double DEFAULT_AMOUNT = 200.5;

	@Mock
	private EntityManager entityManager;
	@Mock
	private ElectricityCounterService electricityCounterService;
	@InjectMocks
	private ElectricityCounterCallbackService service;

	private ElectricityCounter defaultCounter;
	private CounterCallbackDTO defaultCounterCallbackDTO;

	@Before
	public void startup() {
		defaultCounter = ElectricityCounter.builder().id(DEFAULT_COUNTER_ID).villageName(VILLAGE_NAME).build();
		defaultCounterCallbackDTO = CounterCallbackDTO.builder().counterId(defaultCounter.getId())
				.amount(DEFAULT_AMOUNT).build();
	}

	@Test
	public void tryToPersistAElectricityCounterCallback() throws Exception {
		doNothing().when(entityManager).persist(any(ElectricityCounterCallback.class));
		when(electricityCounterService.find(DEFAULT_COUNTER_ID)).thenReturn(defaultCounter);
		service.createNewCounterCallback(defaultCounterCallbackDTO);
		Mockito.verify(entityManager).persist(any(ElectricityCounterCallback.class));
	}

	@Test(expected = ElectricityCounterException.class)
	public void tryToPersistAInvalidCounterCallback() throws Exception {
		when(electricityCounterService.find(DEFAULT_COUNTER_ID)).thenThrow(ElectricityCounterException.class);
		service.createNewCounterCallback(defaultCounterCallbackDTO);
	}

	@Test
	public void tryToPersistAInvalidCounterCallbackMessage() throws Exception {
		try {
			when(electricityCounterService.find(DEFAULT_COUNTER_ID)).thenThrow(new ElectricityCounterException(1L));
			service.createNewCounterCallback(defaultCounterCallbackDTO);
		}
		catch (ElectricityCounterException e) {
			assertThat(e.getMessage()).isEqualTo("Eletricity counter not found for id: " + DEFAULT_COUNTER_ID);
		}
	}

}
