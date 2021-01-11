package com.beyondid.scimConnector.jfgcp.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    private static final String SERVER_ERROR = "SERVER ERROR: STATUS CODE: {}";
    private static final String CLIENT_ERROR = "CLIENT ERROR: STATUS CODE: {}";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.SERVER_ERROR) {
            String error = getStringFromInputStream(httpResponse.getBody());
            logger.error(SERVER_ERROR, httpResponse.getStatusCode(), error);
            throw new RestServiceException(SERVER_ERROR + httpResponse.getStatusCode() + error);
        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR && httpResponse.getStatusCode() != HttpStatus.NOT_FOUND) {
            String error = getStringFromInputStream(httpResponse.getBody());
            logger.error(CLIENT_ERROR, httpResponse.getStatusCode(), error);
            throw new RestServiceException(error);
        }

    }


    public String getStringFromInputStream(InputStream is) {


        StringBuilder sb = new StringBuilder();

        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            logger.error(SERVER_ERROR, e.getMessage());
            return null;
        }


        return sb.toString();

    }

}
