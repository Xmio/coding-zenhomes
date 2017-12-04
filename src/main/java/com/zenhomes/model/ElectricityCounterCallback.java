package com.zenhomes.model;

import static javax.persistence.GenerationType.AUTO;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityCounterCallback {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@ManyToOne
	private ElectricityCounter electricityCounter;

	private Double amount;

	private LocalDateTime dateTime;

	@PrePersist
	private void prePersist() {
		this.dateTime = LocalDateTime.now();
	}

}
