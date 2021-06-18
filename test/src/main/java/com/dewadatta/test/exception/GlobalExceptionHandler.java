package com.dewadatta.test.exception;

import com.dewadatta.test.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class GlobalExceptionHandler {
    public static final Response<Object> ChekingExceptionGlobal(String applicatioName, HttpServletResponse httpServletResponse, Exception e){
        //fluentReport(applicatioName,e);
        Throwable t = ExceptionUtils.getRootCause(e);
        List<String> specificationError = new ArrayList<>();
        specificationError.add(t.getStackTrace()[0].getClassName());
        specificationError.add(t.getStackTrace()[0].getMethodName());

        if(t instanceof ResourceNotFoundException){
            return Response.statusError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), specificationError);
        }
        else if(t instanceof JsonProcessingException){
            return Response.statusError(httpServletResponse, HttpStatus.BAD_REQUEST, e.getMessage(), specificationError);
        }

        return Response.statusError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), specificationError);

    }
}
