package com.tasks.domain.dto;

public record ErrorResponse(
    int status,
    String message,
    String details
) {

}
