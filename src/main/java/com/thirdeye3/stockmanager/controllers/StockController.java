package com.thirdeye3.stockmanager.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.dtos.StockDto;
import com.thirdeye3.stockmanager.services.StockService;

@RestController
@RequestMapping("/sm/stocks")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @GetMapping("/{uniqueId}")
    public Response<StockDto> getStock(@PathVariable Long uniqueId) {
        logger.info("Request received: getStock with uniqueId={}", uniqueId);
        StockDto stock = stockService.getStock(uniqueId);
        if (stock != null) {
            return new Response<>(true, 0, null, stock);
        } else {
            return new Response<>(false, 404, "Stock not found", null);
        }
    }

    @GetMapping("/all")
    public Response<List<StockDto>> getAllStock() {
        logger.info("Request received: getAllStock");
        List<StockDto> stocks = stockService.getAllStock();
        return new Response<>(true, 0, null, stocks);
    }
    
    @DeleteMapping()
    public Response<String> deleteAllStocks()
    {
    	logger.info("Deleteing all stocks");
    	stockService.deleteAllStocks();
    	return new Response<>(true, 0, null, "All stocks deleted successfully");
    }

    
    @GetMapping("/webscrapper/{id}/{code}")
    public Response<List<StockDto>> getForWebscrapper(@PathVariable("id") Integer webscrapperId, @PathVariable("code") String webscrapperCode) {
        return new Response<>(true, 0, null, stockService.getByMachineNo(webscrapperId, webscrapperCode));
    }
    
    @GetMapping("/batch")
    public Response<List<StockDto>> getAllStockByMachine(
            @RequestParam("start") Long startUniqueId,
            @RequestParam("end") Long endUniqueId) {
        logger.info("Request received: getAllStockInBatch from {} to {}", startUniqueId, endUniqueId);
        List<StockDto> stocks = stockService.getAllStockInBatch(startUniqueId, endUniqueId);
        return new Response<>(true, 0, null, stocks);
    }

    @PostMapping
    public Response<String> addStocks(@RequestBody List<StockDto> stockList) {
        logger.info("Request received: addStocks with {} items", stockList.size());
        stockService.addStocks(stockList);
        return new Response<>(true, 0, null, "Stocks added successfully");
    }
    
    @PostMapping("/updatePrice")
    public Response<String> updatePriceOfStock(@RequestBody List<StockDto> stockList) {
        logger.info("Request received: updatePriceOfStock with {} items", stockList.size());
        stockService.updatePriceOfStock(stockList);
        return new Response<>(true, 0, null,
                "Stocks price updated successfully: " + stockList.size() + " updated.");
    }
    
    @GetMapping("/all/{page}/{size}")
    public Response<List<StockDto>> getStocks(
            @PathVariable long page,
            @PathVariable long size) {
        return new Response<>(true, 0, null,stockService.getStocks(page, size));
    }
    
    @GetMapping("/all/size")
    public Response<Long> getStockSize() {
        return new Response<>(true, 0, null,stockService.getStockSize());
    }
    
    @PostMapping("/uploadCSV")
    public Response<String> addStocksUsingCsv(@RequestParam("file") MultipartFile file) {
        stockService.addStocksUsingCsv(file);
        return new Response<>(true, 0, null, "Stocks uploaded successfully");
    }

}
