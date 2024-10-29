package com.fuochuy.spring_boot.springboot.log.controller;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerLogRequest {

    @SerializedName("action")
    private String action;

    @SerializedName("params")
    private String params;

    @SerializedName("headers")
    private String headers;

    @SerializedName("path_variables")
    private String pathVariables;

    @SerializedName("body")
    private String body;
}
