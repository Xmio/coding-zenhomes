package com.zenhomes.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.zenhomes.model.ElectricityCounter;
import com.zenhomes.model.ElectricityCounterCallback;
import com.zenhomes.model.QElectricityCounter;
import com.zenhomes.model.QElectricityCounterCallback;

@Repository
public class ElectricityCounterRepository extends QueryDslRepositorySupport {

	public ElectricityCounterRepository() {
		super(ElectricityCounter.class);
	}

	public List<ElectricityCounterCallback> findCallbackAfter(LocalDateTime reportPeriod) {
		QElectricityCounterCallback qCallBack = QElectricityCounterCallback.electricityCounterCallback;
		return from(qCallBack).select(qCallBack)
				.join(qCallBack.electricityCounter, QElectricityCounter.electricityCounter).fetchJoin()
				.where(qCallBack.dateTime.after(reportPeriod)).fetch();
	}

}
