package com.example.kafkastreams.kafka.dto;

import java.time.LocalDateTime;

public record Greeting(String message, LocalDateTime timeStamp) {

}
