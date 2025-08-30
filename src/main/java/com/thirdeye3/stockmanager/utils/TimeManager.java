package com.thirdeye3.stockmanager.utils;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirdeye3.stockmanager.services.PropertyService;

@Component
public class TimeManager {
    
    @Value("${thirdeye.timezone}")
    private String timeZone;
    
    @Autowired
    PropertyService propertyService;

    public Timestamp getCurrentTime() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of(timeZone));
        LocalDateTime localDateTime = currentTime.toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }
    
    public long getMillisUntilNextMinute() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(timeZone));
        LocalDateTime nextMinute = now.plusMinutes(1).withSecond(0).withNano(0);
        return ChronoUnit.MILLIS.between(now, nextMinute);
    }
    
    public boolean allowMorningPriceUpdate(Timestamp currentTime) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZone));
        DayOfWeek day = now.getDayOfWeek();

        boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;

        LocalTime currentLocalTime = currentTime.toLocalDateTime().toLocalTime();

        boolean isWithinMarketHours =
                !currentLocalTime.isBefore(propertyService.getMorningPriceUpdaterStartTime()) &&
                !currentLocalTime.isAfter(propertyService.getMorningPriceUpdaterEndTime());

        return isWeekday && isWithinMarketHours;
    }

    public boolean allowEveningPriceUpdate(Timestamp currentTime) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZone));
        DayOfWeek day = now.getDayOfWeek();

        boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;

        LocalTime currentLocalTime = currentTime.toLocalDateTime().toLocalTime();

        boolean isWithinMarketHours =
                !currentLocalTime.isBefore(propertyService.getEveningPriceUpdaterStartTime()) &&
                !currentLocalTime.isAfter(propertyService.getEveningPriceUpdaterEndTime());

        return isWeekday && isWithinMarketHours;
    }



}
