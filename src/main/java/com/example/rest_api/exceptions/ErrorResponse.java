package com.example.rest_api.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;
}
