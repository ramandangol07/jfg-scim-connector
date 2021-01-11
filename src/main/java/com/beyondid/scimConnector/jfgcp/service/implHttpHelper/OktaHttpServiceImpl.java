package com.beyondid.scimConnector.jfgcp.service.implHttpHelper;


import com.beyondid.scimConnector.jfgcp.config.BeyondRestBuilder;
import com.beyondid.scimConnector.jfgcp.service.implHttpHelper.urlbuild.HttpService;
import com.beyondid.scimConnector.jfgcp.util.OktaOrgDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("oktaRestCall")
public class OktaHttpServiceImpl implements HttpService {

    @Autowired(required = true)
    @Qualifier("oktaService")
    public RestTemplate oktaService;

    @Autowired
    private OktaOrgDetails oktaOrgDetails;

    @Autowired
    private BeyondRestBuilder beyondRestBuilder;

    private HttpHeaders headers;

    @PostConstruct
    @Override
    public void initHeaders() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List list = new ArrayList();
        list.add(MediaType.APPLICATION_JSON);
        headers.setAccept(list);
        headers.add("Authorization", "SSWS " + oktaOrgDetails.getApiKey());
    }

    @Override
    public ResponseEntity<String> doRequest(String url, HttpMethod method, String requestPayload) {

        HttpEntity<String> entity;

        if (requestPayload == null) {
            entity = new HttpEntity(null, headers);
        } else {
            entity = new HttpEntity(requestPayload, headers);
        }

        String fullUrl = oktaOrgDetails.getOrgUrl() + url;

        return oktaService.exchange(fullUrl, method, entity, String.class, 100);
    }

    @Override
    public ResponseEntity<String> doRequest(String url, HttpMethod method, String requestPayload, String immutableId) {
        return null;
    }
}
