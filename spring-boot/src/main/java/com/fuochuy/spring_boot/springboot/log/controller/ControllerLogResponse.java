package com.fuochuy.spring_boot.springboot.log.controller;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ControllerLogResponse {

    @SerializedName("code")
    private Integer code;

    @SerializedName("body")
    private String body;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("stack_trace_elements")
    private String stackTraceElements;

    @SerializedName("elapsed")
    private long elapsed;
}
