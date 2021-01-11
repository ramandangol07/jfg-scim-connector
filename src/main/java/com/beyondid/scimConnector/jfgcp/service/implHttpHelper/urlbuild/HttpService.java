package com.beyondid.scimConnector.jfgcp.service.implHttpHelper.urlbuild;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public interface HttpService {
    @PostConstruct
    public void initHeaders();

    public ResponseEntity<String> doRequest(String url, HttpMethod method, String requestPayload);

    public ResponseEntity<String> doRequest(String url, HttpMethod method, String requestPayload, String immutableId);
}
