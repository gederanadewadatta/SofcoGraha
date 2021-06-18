package com.dewadatta.test.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class RestApiUtil {
    @Autowired
    ObjectMapper objectMapper;

    public static String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
    public static int LOG_STRING_LENGTH = 1024;

    public final static RestTemplate restTemplate;
    static {
        restTemplate = new RestTemplate();
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = (new RestTemplateBuilder())
                .setReadTimeout(Duration.ofSeconds(120))
                .setConnectTimeout(Duration.ofSeconds(120))
                .build();
        restTemplate.setRequestFactory(factory);
    }

    public String putPostString(String url, HttpHeaders headers, String requestBody, MediaType contentType, HttpMethod httpMethod) throws Exception {
        log.info("Post to "+url);
        log.info("Request body string: "+shortenLog(requestBody));
        ResponseEntity<String> httpResponse = null;
        try {
            headers.setContentType(contentType);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            httpResponse = restTemplate.exchange(url, httpMethod, entity, String.class);
            String responseBody = httpResponse.getBody();
            log.info("Response body string: "+shortenLog(responseBody));
            return httpResponse.getBody();
        } catch (Exception e) {
            if (httpResponse!=null) {
                log.error("Error post to URL: "+url+" -- Request: "+requestBody+" -- Response status code: "+httpResponse.getStatusCode()+", response body: "+httpResponse.getBody());
                throw e;
            }
            log.error("Error while API request to "+url+": "+e);
            throw e;
        }
    }

    public String postString(String url, HttpHeaders headers, String requestBody, MediaType contentType) throws Exception {
        return putPostString(url, headers, requestBody, contentType, HttpMethod.POST);
    }


    public JsonObject postJsonObject(String url, HttpHeaders headers, JsonObject requestBody) throws Exception {
        String responseString = postString(url, headers, requestBody.toString(), MediaType.APPLICATION_JSON);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public JsonObject postJsonObject(String url, JsonObject requestBody) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String responseString = postString(url, headers, requestBody.toString(), MediaType.APPLICATION_JSON);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public <T> T postObject(String url, HttpHeaders headers, Object requestObject, Class<T> responseObjectType) throws Exception {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat(GSON_DATE_FORMAT)
                    .create();
            String requestJsonString = gson.toJson(requestObject, requestObject.getClass());
            String responseJsonString = postString(url, headers, requestJsonString, MediaType.APPLICATION_JSON);
            return gson.fromJson(responseJsonString, responseObjectType);
        } catch (Exception e) {
            log.error("Error while post object: "+e);
            throw e;
        }
    }

    public <T> T postResponseObject(String url, HttpHeaders headers, Object requestObject, Class<T> cls) throws Exception {
        try {
            Response<T> response = postObject(url, headers, requestObject, Response.class);
            return objectMapper.convertValue(response.getData(), cls);
        } catch (Exception e) {
            log.error("Error while get object: "+e);
            throw e;
        }
    }

    public <T> T postResponseObject(String url, Object requestObject, Class<T> cls) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        return postResponseObject(url, headers, requestObject, cls);
    }


    public <T> T postObject(String url, Object requestObject, Class<T> responseObjectType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        return postObject(url, headers, responseObjectType);
    }

    public String putString(String url, HttpHeaders headers, String requestBody, MediaType contentType) throws Exception {
        return putPostString(url, headers, requestBody, contentType, HttpMethod.PUT);
    }

    public JsonObject putJsonObject(String url, HttpHeaders headers, JsonObject requestBody) throws Exception {
        String responseString = putString(url, headers, requestBody.toString(), MediaType.APPLICATION_JSON);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public JsonObject putJsonObject(String url, JsonObject requestBody) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String responseString = putString(url, headers, requestBody.toString(), MediaType.APPLICATION_JSON);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public <T> T putObject(String url, HttpHeaders headers, Object requestObject, Class<T> responseObjectType) throws Exception {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat(GSON_DATE_FORMAT)
                    .create();
            String requestJsonString = gson.toJson(requestObject, requestObject.getClass());
            String responseJsonString = putString(url, headers, requestJsonString, MediaType.APPLICATION_JSON);
            return gson.fromJson(responseJsonString, responseObjectType);

        } catch (Exception e) {
            log.error("Error while put object: "+e);
            throw e;
        }
    }

    public <T> T putObject(String url, Object requestObject, Class<T> responseObjectType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        return putObject(url, headers, responseObjectType);
    }

    public String getString(String srcUrl, HttpHeaders headers, Map<String,Object> queryStrings) throws Exception {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(srcUrl);
        for (String key : queryStrings.keySet()) {
            uriBuilder.queryParam(key, queryStrings.get(key));
        }
        final String url = uriBuilder.build().toUriString();

        log.info("Get to "+url);
        ResponseEntity<String> httpResponse = null;
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            httpResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseBody = httpResponse.getBody();
            log.info("Response body string: "+shortenLog(responseBody));
            return responseBody;
        } catch (Exception e) {
            if (httpResponse!=null) {
                log.error("Error GET to URL: "+url+" -- Response status code: "+httpResponse.getStatusCode()+" -- Response body: "+httpResponse.getBody());
                throw e;
            }
            log.error("Error while API request to "+url+": "+e);
            throw e;
        }
    }


    public String getString(String srcUrl, HttpHeaders headers) throws Exception {
        Map<String,Object> queryStrings = new HashMap<>();
        return getString(srcUrl, headers, queryStrings);
    }

    public String getString(String srcUrl) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        Map<String,Object> queryStrings = new HashMap<>();
        return getString(srcUrl, headers, queryStrings);
    }

    public JsonObject getJson(String srcUrl, HttpHeaders headers, Map<String,Object> queryStrings) throws Exception {
        String responseString = getString(srcUrl, headers, queryStrings);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public JsonObject getJson(String srcUrl, Map<String,Object> queryStrings) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String responseString = getString(srcUrl, headers, queryStrings);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public JsonObject getJson(String srcUrl) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        Map<String,Object> queryStrings = new HashMap<>();
        String responseString = getString(srcUrl, headers, queryStrings);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public <T> T getObject(String url, HttpHeaders headers, Map<String,Object> queryStrings, Class<T> responseObjectType) throws Exception {
        try {
            Gson gson = new Gson();
            JsonObject responseJson = getJson(url, headers, queryStrings);
            return gson.fromJson(responseJson, responseObjectType);
        } catch (Exception e) {
            log.error("Error while get object: "+e);
            throw e;
        }
    }

    public <T> T getObject(String url, Class<T> responseObjectType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        Map<String,Object> queryStrings = new HashMap<>();
        return getObject(url, headers, queryStrings, responseObjectType);
    }

    public <T> T getResponseObject(String url, Map<String,Object> queryStrings, Class<T> cls) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            String stringResp = getString(url, headers, queryStrings);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
            objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

            Response<T> resp = objectMapper.readValue(stringResp, new TypeReference<Response<T>>() {
            });
            return objectMapper.convertValue(resp.getData(), cls);
        } catch (Exception e) {
            log.error("Error while get object: "+e);
            throw e;
        }
    }

    public <T> T getResponseObject(String url, Class<T> cls) throws Exception {
        Map<String,Object> queryStrings = new HashMap<>();
        return getResponseObject(url, queryStrings, cls);
    }

    public <T> T postDataResponseObject(String url, Object object, Class<T> cls) throws Exception{
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Object> entity = new HttpEntity<>(object, headers);
            ResponseEntity<String> responseDetail = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
            objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

            Response<T> resp = objectMapper.readValue(responseDetail.getBody(), new TypeReference<Response<T>>() {
            });
            if(Boolean.FALSE.equals(resp.getData())){
                return objectMapper.convertValue(cls, cls);
            }
            return objectMapper.convertValue(resp.getData(), cls);
        } catch (Exception e) {
            log.error("Error while get object: "+e);
            throw e;
        }
    }

    public <T> T requestToObject(HttpServletRequest httpRequest, Class<T> responseObjectType) throws Exception {
        Gson gson = new Gson();
        String rawRequestString = IOUtils.toString(httpRequest.getInputStream(), StandardCharsets.UTF_8);
        log.info("API request string: "+shortenLog(rawRequestString));
        if (rawRequestString == null) throw new Exception("Request body is null!");
        return gson.fromJson(rawRequestString, responseObjectType);
    }

    public static String shortenLog(String logString) {
        return ((logString.length()>LOG_STRING_LENGTH)?logString.substring(0,LOG_STRING_LENGTH)+"...":logString);
    }

    public JsonObject objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJsonTree(obj, obj.getClass()).getAsJsonObject();
    }

    public void delete(String url) {
        try {
            restTemplate.delete(url);
        } catch (Exception ex) {
            log.error("Error while deleting:");
        }
    }
}
