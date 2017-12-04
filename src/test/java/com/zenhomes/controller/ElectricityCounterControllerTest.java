package com.zenhomes.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zenhomes.model.ElectricityCounter;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ElectricityCounterControllerTest {

	private static final String VILLAGE_NAME = "Villarriba";
	private static final ElectricityCounter DEFAULT_COUNTER = ElectricityCounter.builder().villageName(VILLAGE_NAME)
			.build();

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void tryToCreateANewCounter() {
		ResponseEntity<Long> response = restTemplate.postForEntity("/counter", DEFAULT_COUNTER, Long.class);
		assertThat(response.getStatusCode()).isEqualTo(OK);
		ElectricityCounter electricityCounter = restTemplate
				.getForEntity("/counter/{id}", ElectricityCounter.class, response.getBody()).getBody();
		assertThat(electricityCounter.getVillageName()).isEqualTo(VILLAGE_NAME);
	}

	@Test
	public void tryToCreateANewCounterWithoutVillageName() {
		ResponseEntity<Void> response = restTemplate.postForEntity("/counter", new ElectricityCounter(), Void.class);
		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void tryToFindANonExistentCounter() {
		ResponseEntity<Void> response = restTemplate.getForEntity("/counter/{id}", Void.class, Long.MAX_VALUE);
		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

}
