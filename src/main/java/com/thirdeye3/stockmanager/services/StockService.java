package com.thirdeye3.stockmanager.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thirdeye3.stockmanager.dtos.StockDto;

public interface StockService {

	List<StockDto> getAllStockInBatch(Long startUniqueId, Long endUniqueId);

	List<StockDto> getAllStock();

	StockDto getStock(Long uniqueId);

	void addStocks(List<StockDto> stockList);
	
	void updateStocks();

	List<StockDto> getByMachineNo(Integer machineId, String machineUniqueCode);

	void updatePriceOfStock(List<StockDto> stockDtos);

	List<StockDto> getStocks(long page, long size);

	Long getStockSize();
	
	void addStocksUsingCsv(MultipartFile file);

}
