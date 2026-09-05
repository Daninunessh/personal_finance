package com.api.personal.finance.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandardError  {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}