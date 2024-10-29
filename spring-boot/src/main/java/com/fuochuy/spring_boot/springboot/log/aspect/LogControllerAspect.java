package com.fuochuy.spring_boot.springboot.log.aspect;

import com.fuochuy.spring_boot.springboot.exception.CustomException;
import com.fuochuy.spring_boot.springboot.exception.InternalException;
import com.fuochuy.spring_boot.springboot.log.controller.ControllerLogHelper;
import com.fuochuy.spring_boot.springboot.log.controller.ControllerLogResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class LogControllerAspect {

    @Around("@annotation(com.fuochuy.spring_boot.springboot.log.annotation.LogController)")
    public Object logControllerExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        ControllerLogResponse controllerLogResponse = null;
        Object responseBody = null;
        try {
            initialLogController(joinPoint);
            responseBody = joinPoint.proceed();
            controllerLogResponse = ControllerLogHelper.createEndpointLogResponse(responseBody);
            return responseBody;
        } catch (CustomException customException) {
            controllerLogResponse = ControllerLogHelper.createEndpointLogResponse(customException);
            throw customException;
        } catch (Throwable throwable) {
            controllerLogResponse = ControllerLogHelper.createEndpointLogResponse(throwable);
            throw throwable;
        } finally {
            if (controllerLogResponse != null) {
                controllerLogResponse = ControllerLogHelper.createEndpointLogResponse(new InternalException());
            }
            ControllerLogHelper.endLogging(controllerLogResponse);
        }

    }

    private void initialLogController(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> pathVariables = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        Object requestBody = null;

        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();

        assert args.length == annotations.length;
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof RequestBody) {
                    requestBody = args[i];
                } else if (annotation instanceof RequestParam requestParam) {
                    queryParams.put(requestParam.value(), Objects.isNull(args[i]) ? "" : args[i].toString());
                } else if (annotation instanceof PathVariable pathVariable) {
                    pathVariables.put(pathVariable.value(), Objects.isNull(args[i]) ? "" : args[i].toString());
                } else if (annotation instanceof RequestHeader requestHeader) {
                    headers.put(requestHeader.value(), Objects.isNull(args[i]) ? "" : args[i].toString());
                }
            }
        }
        ControllerLogHelper.startLogging(this.getAction(joinPoint), headers, pathVariables, queryParams, requestBody);
    }

    private String getAction(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        return className + "." + methodName;
    }
}
