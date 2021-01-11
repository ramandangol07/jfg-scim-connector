package com.beyondid.scimConnector.jfgcp.service.implHttpHelper;

import com.beyondid.scimConnector.jfgcp.config.BeyondRestBuilder;
import com.beyondid.scimConnector.jfgcp.service.implHttpHelper.urlbuild.HttpService;
import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import com.beyondid.scimConnector.jfgcp.util.PropLoadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
@Qualifier("johnsonFinancialRestCall")
public class JohnsonFinancialHttpServiceImpl implements HttpService {

    @Autowired(required = true)
    @Qualifier("johnsonAuthService")
    public RestTemplate johnsonAuthService;

    private HttpHeaders headers;

    @Autowired
    private PropLoadUtil propLoadUtil;

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private BeyondRestBuilder beyondRestBuilder;

    @PostConstruct
    @Override
    public void initHeaders() {
        Properties apiInfo = propLoadUtil.loadMappingProp(applicationConstants.JOHNSON_APP_INFO);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List list = new ArrayList();
        list.add(MediaType.APPLICATION_JSON);
        headers.setAccept(list);
        headers.set("client_id", apiInfo.get("clientId").toString());
        headers.set("client_secret", apiInfo.get("clientSecret").toString());
    }

    @Override
    public ResponseEntity<String> doRequest(String url, HttpMethod method, String requestPayload) {
        return null;
    }


    @Override
    public ResponseEntity<String> doRequest(String url, HttpMethod method, String requestPayload, String immutableId) {
        HttpEntity<String> entity;

        if (requestPayload == null){
            entity = new HttpEntity(null, headers);
        }
        else{
            headers.set("uuid", UUID.randomUUID().toString());
            headers.set("immutableId", immutableId);
            entity = new HttpEntity(requestPayload, headers);
        }
        return johnsonAuthService.exchange(url, method, entity, String.class, 100);

    }
}
