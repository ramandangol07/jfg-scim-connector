package com.beyondid.scimConnector.jfgcp.service.restApiHandler;

import com.beyondid.scimConnector.jfgcp.service.implHttpHelper.urlbuild.HttpService;
import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import com.beyondid.scimConnector.jfgcp.util.JsonConversionUtil;
import com.beyondid.scimConnector.jfgcp.util.OktaConstantUrl;
import com.beyondid.scimConnector.jfgcp.util.PropLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@PropertySource("classpath:application.properties")
public class OktaApi {

    @Autowired
    @Qualifier("oktaRestCall")
    private HttpService oktaService;

    @Autowired
    private JsonConversionUtil jsonConversionUtil;

  /*  @Autowired
    private PropLoadUtil propLoadUtil;

    @Autowired
    private ApplicationConstants applicationConstants;*/

    private static final Logger LOGGER = LoggerFactory.getLogger(OktaApi.class);

    public Map checkExistingMember(String memberEmail) {
        Map user = null;
        try {
            String url = OktaConstantUrl.GET_USER.replace("${userId}", memberEmail);
            ResponseEntity<String> response = oktaService.doRequest(url, HttpMethod.GET, null);
            Map userResponse = jsonConversionUtil.convertStringtoMap(response.getBody());
            if (!userResponse.containsKey("errorCode")) {
                user = userResponse;
                return user;
            }
        }catch (Exception ex){
            LOGGER.error("Error on getting existing member::"+ex.getMessage());
        }
        return user;
    }
}
