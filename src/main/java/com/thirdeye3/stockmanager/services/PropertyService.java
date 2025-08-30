package com.thirdeye3.stockmanager.services;

import java.time.LocalTime;

public interface PropertyService {

	void fetchProperties();

	Integer getNoOFWebscrapperMarket();

	Integer getNoOFWebscrapperUser();

	LocalTime getMorningPriceUpdaterEndTime();

	LocalTime getMorningPriceUpdaterStartTime();

	LocalTime getEveningPriceUpdaterStartTime();

	LocalTime getEveningPriceUpdaterEndTime();

}
