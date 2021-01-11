package com.beyondid.scimConnector.jfgcp.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                        final ClientHttpRequestExecution execution) throws IOException {
        logger.info("Request Method: {} Request URI: {}", request.getMethod(), request.getURI());
        logger.debug("Request Body: {} ", new String(body));
        ClientHttpResponse response = execution.execute(request, body);

        response = log(request, body, response);

        return response;
    }

    private ClientHttpResponse log(final HttpRequest request, final byte[] body, final ClientHttpResponse response) throws IOException {
        final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
        logger.info("Response Status: {} ", responseCopy.getStatusCode());
        logger.debug("Response Body: {} ", StreamUtils.copyToString(responseCopy.getBody(), Charset.defaultCharset()));
        logger.debug("Response Headers: {} ", responseCopy.getHeaders());

        return responseCopy;
    }


}
