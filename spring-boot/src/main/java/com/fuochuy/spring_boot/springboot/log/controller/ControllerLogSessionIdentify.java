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
public class ControllerLogSessionIdentify {

    @SerializedName("parent_session_id")
    private String parentSessionId;

    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("flow_id")
    private String flowId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("app_id")
    private String appId;

    @SerializedName("method")
    private String method;

    @SerializedName("uri")
    private String uri;
}
