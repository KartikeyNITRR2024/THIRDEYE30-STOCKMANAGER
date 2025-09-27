package com.thirdeye3.stockmanager.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thirdeye3.stockmanager.dtos.StockDto;
import com.thirdeye3.stockmanager.entities.Stock;
import com.thirdeye3.stockmanager.exceptions.CSVException;
import com.thirdeye3.stockmanager.exceptions.InvalidTimeException;
import com.thirdeye3.stockmanager.exceptions.ResourceNotFoundException;
import com.thirdeye3.stockmanager.repositories.StockRepo;
import com.thirdeye3.stockmanager.services.MachineService;
import com.thirdeye3.stockmanager.services.PropertyService;
import com.thirdeye3.stockmanager.services.StockService;
import com.thirdeye3.stockmanager.utils.TimeManager;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private MachineService machineService;
    
    @Autowired
    private TimeManager timeManager;
    
    @Autowired
    private PropertyService propertyService;

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private ConcurrentSkipListMap<Long, Stock> stocks = null;
    private Map<String, Stock> stocks2 = null;

    public void updateStocks() {
        logger.info("Updating stock caches from DB...");
        stocks = new ConcurrentSkipListMap<>();
        stocks2 = new ConcurrentHashMap<>();
        List<Stock> list = stockRepo.findAll();
        for (Stock stock : list) {
            stocks.put(stock.getUniqueId(), stock);
            stocks2.put(stock.getUniqueCode() + stock.getMarketCode(), stock);
        }
        logger.info("Stock cache updated: {} entries loaded", list.size());
    }

    public void addStocks(List<StockDto> stockList) {
        if (stocks == null || stocks2 == null) {
            updateStocks();
        }
        int addedCount = 0;
        for (StockDto stockDto : stockList) {
            String key = stockDto.getUniqueCode() + stockDto.getMarketCode();
            if (!stocks2.containsKey(key)) {
                stockRepo.save(toEntity(stockDto));
                addedCount++;
            }
        }
        updateStocks();
        propertyService.updateInitiatier();
        logger.info("{} new stocks added", addedCount);
    }

    public StockDto getStock(Long uniqueId) {
        if (stocks == null || stocks2 == null) {
            updateStocks();
        }
        Stock stock = stocks.get(uniqueId);
        if (stock == null) {
            throw new ResourceNotFoundException("Stock with ID " + uniqueId + " not found");
        }
        return toDto(stock);
    }

    public List<StockDto> getAllStock() {
        logger.info("Fetching all stocks");
        return stockRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<StockDto> getAllStockInBatch(Long startUniqueId, Long endUniqueId) {
        if (stocks == null || stocks2 == null) {
            updateStocks();
        }
        logger.info("Fetching stocks in batch: {} to {}", startUniqueId, endUniqueId);
        return stocks.subMap(startUniqueId, true, endUniqueId, true)
                .values()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public void updatePriceOfStock(List<StockDto> stockDtos) {
        int totalRequested = stockDtos.size();
        int updatedCount = 0;

        for (StockDto stockDto : stockDtos) {
            int count = 0;
            if (timeManager.allowMorningPriceUpdate(stockDto.getCurrentTime())) {
                count = stockRepo.updateTodaysOpeningPrice(
                        stockDto.getUniqueId(),
                        stockDto.getTodaysOpeningPrice()
                );
            } 
            else if (timeManager.allowEveningPriceUpdate(stockDto.getCurrentTime())) {
                count = stockRepo.updateLastNightClosingPrice(
                        stockDto.getUniqueId(),
                        stockDto.getLastNightClosingPrice()
                );
            }
            else
            {
            	throw new InvalidTimeException("Invalid time to update stock price");
            }

            updatedCount += count;
        }

        if (updatedCount < totalRequested) {
            throw new ResourceNotFoundException(
                "Some stocks were not found. Requested: " + totalRequested + ", Updated: " + updatedCount
            );
        }
    }


    @Override
    public List<StockDto> getByMachineNo(Integer machineId, String machineUniqueCode) {
        Integer number = machineService.validateMachine(machineId, machineUniqueCode);
        Integer totalStocks = stocks.size();
        Integer totalMachine1 = machineService.getType1MachinesCount();
        Integer inEachBatch = (totalStocks / totalMachine1) + (totalStocks % totalMachine1 == 0 ? 0 : 1);
        Integer startPoint = inEachBatch * (number - 1);
        Integer endPoint = Math.min(inEachBatch * number, totalStocks);

        List<Long> keys = new ArrayList<>(stocks.keySet());
        List<Long> batchKeys = keys.subList(startPoint, endPoint);

        return batchKeys.stream()
                .map(stocks::get)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StockDto> getStocks(long page, long size) {
        List<Stock> stocks = stockRepo.findAll(PageRequest.of((int)page, (int)size)).getContent();
        return toDtoList(stocks);
    }

    private StockDto toDto(Stock stock) {
        return new StockDto(
                stock.getUniqueId(),
                stock.getUniqueCode(),
                stock.getMarketCode(),
                stock.getLastNightClosingPrice(),
                stock.getTodaysOpeningPrice(),
                null
        );
    }
    
    private List<StockDto> toDtoList(List<Stock> stocks) {
        return stocks.stream()
                     .map(this::toDto)
                     .collect(Collectors.toList());
    }

    private Stock toEntity(StockDto dto) {
        return new Stock(
                dto.getUniqueId(),
                dto.getUniqueCode(),
                dto.getMarketCode(),
                dto.getLastNightClosingPrice(),
                dto.getTodaysOpeningPrice()
        );
    }

	@Override
	public Long getStockSize() {
        if (stocks == null || stocks2 == null) {
            updateStocks();
        }
        return (long) stocks.size();
	}

	@Override
	public void addStocksUsingCsv(MultipartFile file) {
	    List<StockDto> stockList = new ArrayList<>();

	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

	        String line;
	        boolean firstLine = true;

	        while ((line = br.readLine()) != null) {
	            if (firstLine) {
	                firstLine = false;
	                continue;
	            }

	            String[] values = line.split(",");
	            if (values.length >= 3) {
	                String sno = values[0].trim();
	                String uniqueCode = values[1].trim();
	                String marketCode = values[2].trim();

	                StockDto stock = new StockDto(
	                        null,
	                        uniqueCode,
	                        marketCode,
	                        null,
	                        null,
	                        null
	                );

	                stockList.add(stock);
	            }
	        }

	    } catch (Exception e) {
	        throw new CSVException("Failed to parse CSV file: " + e.getMessage());
	    }

	    addStocks(stockList);
	}
	
	@Override
	public void resetAllOpeningPrice()
	{
		int count = stockRepo.resetAllOpeningPrices();
		logger.info("Reset prices of "+count+" stocks to null");
	}
}
