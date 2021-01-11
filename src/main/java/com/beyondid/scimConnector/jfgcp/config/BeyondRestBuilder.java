package com.beyondid.scimConnector.jfgcp.config;

import com.beyondid.scimConnector.jfgcp.error.RestTemplateResponseErrorHandler;
import com.beyondid.scimConnector.jfgcp.util.LoggingRequestInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class BeyondRestBuilder {


    private final CloseableHttpClient httpClient;
    Logger logger = LoggerFactory.getLogger(BeyondRestBuilder.class);


    @Autowired
    public BeyondRestBuilder(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }


    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplateBuilder()
                .requestFactory(this::clientHttpRequestFactory)
                .errorHandler(new RestTemplateResponseErrorHandler())
                .interceptors(new LoggingRequestInterceptor())
                .build();
    }


    @Bean
    @Qualifier("oktaService")
    public RestTemplate get() {
        return restTemplate();
    }

    @Bean
    @Qualifier("johnsonAuthService")
    public RestTemplate getOrgAuthService() {
        return restTemplate();
    }
}
