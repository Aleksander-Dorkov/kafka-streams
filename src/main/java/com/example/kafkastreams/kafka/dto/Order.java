package com.example.kafkastreams.kafka.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Order(
        Integer orderId,
        String locationId,
        BigDecimal finalAmount,
        OrderType orderType,
        List<String> orderLineItems,
        LocalDateTime orderDteTime) {

    public enum OrderType {
        GENERAL, RESTAURANT
    }
}
