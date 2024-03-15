package com.medilabo.prediction.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

public abstract class BasicCommunication {
    @Value("${login}")
    private String LOGIN;
    @Value("${password}")
    private String PASSWORD;

    public HttpHeaders headers;

    /**
     * This method sets headers with basic authentication.
     * The authentication information is found in a properties file.
     */
    public void setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(LOGIN, PASSWORD);
        this.headers = headers;
    }
}
