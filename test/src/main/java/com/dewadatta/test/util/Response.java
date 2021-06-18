package com.dewadatta.test.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T>  {
    private Integer code;
    private String status;
    private T data;
    private String errorMessage;
    private List<String> specificationError;

    public static <T> Response<T> status(HttpServletResponse httpServletResponse, HttpStatus httpStatus, @Nullable T data) {
        httpServletResponse.setStatus(httpStatus.value());
        Response<T> resp = Response.<T>builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .data(data)
                .build();

        String responseBody = ""+resp;
        log.debug("Request body string: "+ RestApiUtil.shortenLog(responseBody));

        return resp;
    }

    public static <T> Response<T> statusError(HttpServletResponse httpServletResponse, HttpStatus httpStatus, String errorMessage, List<String> specificationError) {
        httpServletResponse.setStatus(httpStatus.value());
        return Response.<T>builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .errorMessage(errorMessage)
                .specificationError(specificationError)
                .build();
    }
}
