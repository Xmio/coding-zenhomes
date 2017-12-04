package com.zenhomes.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zenhomes.dto.CounterCallbackDTO;
import com.zenhomes.model.ElectricityCounter;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ElectricityCounterCallbackControllerTest {

	private static final Double DEFAULT_AMOUNT = 200.5;
	private static final String VILLAGE_NAME = "Villarriba";
	private ElectricityCounter defaultCounter;
	private CounterCallbackDTO defaultCounterCallbackDTO;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void startup() {
		defaultCounter = ElectricityCounter.builder().villageName(VILLAGE_NAME).build();
		Long counterId = restTemplate.postForEntity("/counter", defaultCounter, Long.class).getBody();
		defaultCounter.setId(counterId);
		defaultCounterCallbackDTO = CounterCallbackDTO.builder().counterId(defaultCounter.getId()).amount(DEFAULT_AMOUNT)
				.build();
	}

	@Test
	public void tryToCreateANewCounterCallback() {
		ResponseEntity<Long> response = restTemplate.postForEntity("/counter-callback", defaultCounterCallbackDTO,
				Long.class);
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isNotNull();
	}

	@Test
	public void tryToCreateANewCounterCallbackWithInvalidCounter() {
		CounterCallbackDTO counterCallback = CounterCallbackDTO.builder().counterId(Long.MAX_VALUE)
				.amount(DEFAULT_AMOUNT).build();
		ResponseEntity<Void> response = restTemplate.postForEntity("/counter-callback", counterCallback, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void tryToCreateANewCounterCallbackWithoutAmount() {
		CounterCallbackDTO counterCallback = CounterCallbackDTO.builder().counterId(defaultCounter.getId()).build();
		ResponseEntity<Void> response = restTemplate.postForEntity("/counter-callback", counterCallback, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

}
