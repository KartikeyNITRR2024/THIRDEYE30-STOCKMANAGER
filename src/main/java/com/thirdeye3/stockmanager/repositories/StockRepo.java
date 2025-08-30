package com.thirdeye3.stockmanager.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.thirdeye3.stockmanager.entities.Stock;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long> {
	@Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.lastNightClosingPrice = :price WHERE s.uniqueId = :id")
    int updateLastNightClosingPrice(Long id, Double price);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.todaysOpeningPrice = :price WHERE s.uniqueId = :id")
    int updateTodaysOpeningPrice(Long id, Double price);
    
    Page<Stock> findAll(Pageable pageable);
}
