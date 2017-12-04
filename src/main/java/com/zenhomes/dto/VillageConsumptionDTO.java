package com.zenhomes.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class VillageConsumptionDTO {

	private String villageName;
	private BigDecimal consumption;

}
