package com.medilabo.prediction.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Base64;

public class BasicCommunication {
    @Value("${login}")
    private String LOGIN;
    @Value("${password}")
    private String PASSWORD;

    public HttpHeaders headers;

    public void setHeaders() {
        String auth = LOGIN + ":" + PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        this.headers = headers;
    }
}
