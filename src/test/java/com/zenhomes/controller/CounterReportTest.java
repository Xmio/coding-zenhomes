package com.zenhomes.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zenhomes.dto.CounterCallbackDTO;
import com.zenhomes.dto.VillageConsumptionDTO;
import com.zenhomes.model.ElectricityCounter;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
		"spring.datasource.url=jdbc:h2:file:~/h2/report;DB_CLOSE_ON_EXIT=TRUE" })
public class CounterReportTest {

	private static final int TIME_IN_HOURS_TO_CALCULATE_THE_REPORT = 24;
	private ElectricityCounter villaArribaCounter;
	private ElectricityCounter villaAbajoCounter;
	private ElectricityCounter redVillaCounter;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void startup() {
		villaArribaCounter = ElectricityCounter.builder().villageName("Villarriba").build();
		Long arribaId = restTemplate.postForEntity("/counter", villaArribaCounter, Long.class).getBody();
		villaArribaCounter.setId(arribaId);
		villaAbajoCounter = ElectricityCounter.builder().villageName("Villaabajo").build();
		Long abajoId = restTemplate.postForEntity("/counter", villaAbajoCounter, Long.class).getBody();
		villaAbajoCounter.setId(abajoId);
		redVillaCounter = ElectricityCounter.builder().villageName("VillaRed").build();
		Long redVillaId = restTemplate.postForEntity("/counter", redVillaCounter, Long.class).getBody();
		redVillaCounter.setId(redVillaId);
	}

	@Test
	public void tryToRetriveCallback() {
		ResponseEntity<VillageConsumptionDTO[]> response = restTemplate.getForEntity("/consumption-report/{duration}",
				VillageConsumptionDTO[].class, TIME_IN_HOURS_TO_CALCULATE_THE_REPORT);
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody().length).isEqualTo(0);
	}

	@Test
	public void tryToRetriveNewReport() {
		sendCounterCallback(villaArribaCounter, 100.5);
		sendCounterCallback(villaArribaCounter, 150.6);
		sendCounterCallback(villaAbajoCounter, 100.1);
		sendCounterCallback(villaAbajoCounter, 120.3);
		sendCounterCallback(villaAbajoCounter, 190.7);
		sendCounterCallback(redVillaCounter, 100.9);

		ResponseEntity<VillageConsumptionDTO[]> response = restTemplate.getForEntity("/consumption-report/{duration}",
				VillageConsumptionDTO[].class, TIME_IN_HOURS_TO_CALCULATE_THE_REPORT);
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody().length).isEqualTo(3);
		assertThat(response.getBody()[0].getConsumption()).isEqualTo(BigDecimal.valueOf(0.0));
		assertThat(response.getBody()[0].getVillageName()).isEqualTo(redVillaCounter.getVillageName());
		assertThat(response.getBody()[1].getConsumption()).isEqualTo(BigDecimal.valueOf(50.1));
		assertThat(response.getBody()[1].getVillageName()).isEqualTo(villaArribaCounter.getVillageName());
		assertThat(response.getBody()[2].getConsumption()).isEqualTo(BigDecimal.valueOf(90.6));
		assertThat(response.getBody()[2].getVillageName()).isEqualTo(villaAbajoCounter.getVillageName());
	}

	private void sendCounterCallback(ElectricityCounter village, Double amount) {
		CounterCallbackDTO counterCallback = CounterCallbackDTO.builder().counterId(village.getId()).amount(amount)
				.build();
		restTemplate.postForEntity("/counter-callback", counterCallback, Void.class);
	}
}
