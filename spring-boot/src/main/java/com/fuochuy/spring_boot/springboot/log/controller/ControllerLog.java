package com.fuochuy.spring_boot.springboot.log.controller;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerLog {
    @SerializedName("start_at")
    private Long startAt;

    @SerializedName(value = "timestamp")
    private Long timestamp;

    @SerializedName(value = "date")
    private Date date;

    @SerializedName("session_identity")
    private ControllerLogSessionIdentify sessionIdentity;

    @SerializedName("request")
    private ControllerLogRequest request;

    @SerializedName("response")
    private ControllerLogResponse response;
}
