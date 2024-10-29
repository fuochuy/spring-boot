package com.fuochuy.spring_boot.springboot.log.controller;

import com.fuochuy.spring_boot.springboot.constant.RestHeaderConstant;
import com.fuochuy.spring_boot.springboot.exception.CustomException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ControllerLogHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("ENDPOINT");

    private static final Gson gson = new Gson();

    private static final InheritableThreadLocal<ControllerLogSessionIdentify> logSessionIdentity = new InheritableThreadLocal<>();
    private static final Map<String, ControllerLog> logContext = new HashMap<>();


    private ControllerLogHelper() {
    }

    public static String generateHttpRequestId() {
        return String.format(RestHeaderConstant.HTTP_REQUEST_ID_PATTERN, UUID.randomUUID());
    }

    public static void setLogSessionIdentity(ControllerLogSessionIdentify sessionIdentity) {
        logSessionIdentity.set(sessionIdentity);
    }

    public static String getSessionId() {
        return logSessionIdentity.get() != null ? logSessionIdentity.get().getSessionId() : null;
    }

    public static Integer getUserId() {
        return Integer.valueOf(logSessionIdentity.get().getUserId());
    }

    public static void startLogging(String action) {
        startLogging(action, null, null, null, null);
    }

    public static void startLogging(String action, Object params) {
        startLogging(action, null, null, params, null);
    }

    public static void startLogging(String action, Object params, Object body) {
        startLogging(action, null, null, params, body);
    }

    public static void startLogging(String action, Object headers, Object pathVariables, Object params, Object body) {
        var logRequest = createEndpointLogRequest(action, headers, pathVariables, params, body);
        var sessionIdentity = logSessionIdentity.get();
        var now = new Date();
        var logEntity = ControllerLog.builder()
                .startAt(now.getTime())
                .timestamp(now.getTime())
                .date(now)
                .request(logRequest)
                .sessionIdentity(sessionIdentity)
                .build();
        logContext.put(sessionIdentity.getSessionId(), logEntity);
    }

    public static void endLogging(ControllerLogResponse controllerLogResponse) {
        try {
            String sessionId = getSessionId();
            var logEntity = logContext.get(sessionId);
            if (Objects.nonNull(logEntity)) {
                long start = logEntity.getStartAt();
                controllerLogResponse.setElapsed(System.currentTimeMillis() - start);
                logEntity.setResponse(controllerLogResponse);
                String logMessage = gson.toJson(logEntity);
                LOGGER.info(logMessage);

                logContext.remove(sessionId);
            }
        } finally {
            logSessionIdentity.remove();
        }
    }


    public static ControllerLogRequest createEndpointLogRequest(String action, Object headers, Object pathVariables, Object params, Object body) {
        return ControllerLogRequest
                .builder()
                .action(action)
                .headers(gson.toJson(headers))
                .pathVariables(gson.toJson(pathVariables))
                .params(gson.toJson(params))
                .body(gson.toJson(body))
                .build();
    }

    public static ControllerLogResponse createEndpointLogResponse(Object body) {
        return ControllerLogResponse.builder()
                .code(HttpStatus.OK.value())
                .body(gson.toJson(body))
                .build();
    }

    public static ControllerLogResponse createEndpointLogResponse(CustomException e) {
        var stackTraceElements = new String[0];
        for (Throwable t : ExceptionUtils.getThrowableList(e)) {
            stackTraceElements = ArrayUtils.addAll(stackTraceElements, ExceptionUtils.getStackTrace(t));
        }
        String stackTrace = String.join(System.lineSeparator(), stackTraceElements);
        return ControllerLogResponse.builder()
                .code(e.getCode())
                .errorMessage(e.getMessage())
                .stackTraceElements(stackTrace)
                .build();
    }

    public static ControllerLogResponse createEndpointLogResponse(Throwable e) {
        var stackTraceElements = new String[0];
        for (Throwable t : ExceptionUtils.getThrowableList(e)) {
            stackTraceElements = ArrayUtils.addAll(stackTraceElements, ExceptionUtils.getStackTrace(t));
        }
        return ControllerLogResponse.builder()
                .code(500)
                .errorMessage(e.getMessage())
                .stackTraceElements(String.join(System.lineSeparator(), stackTraceElements))
                .build();
    }
}
