package com.beyondid.scimConnector.jfgcp.service.helper;

import com.beyondid.scimConnector.jfgcp.conversion.JSONTransformationScimToExternalApp;
import com.beyondid.scimConnector.jfgcp.service.implHttpHelper.OktaHttpServiceImpl;
import com.beyondid.scimConnector.jfgcp.service.restApiHandler.JohnsonFinancialGroupApi;
import com.beyondid.scimConnector.jfgcp.service.restApiHandler.OktaApi;
import com.beyondid.scimConnector.jfgcp.util.JsonConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CreateUserHelper {
    @Autowired
    @Qualifier("JSONTransformationScimToExternalApp")
    private JSONTransformationScimToExternalApp jsonTransformationScimToExternalApp;

    @Autowired
    private JsonConversionUtil jsonConversionUtil;

    @Autowired
    CreateUpdateHelper createUpdateHelper;

    @Autowired
    private OktaApi oktaApi;

    @Autowired
    private JohnsonFinancialGroupApi johnsonFinancialGroupApi;

    @Autowired
    private OktaHttpServiceImpl oktaHttpService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserHelper.class);

    public Map processCreateUserRequest(Map<String, Object> scimuser) throws IOException {

        return createUserInExternalApplication(scimuser);
    }

    private Map createUserInExternalApplication(Map<String, Object> scimuser) {
        LOGGER.info("INITIATING createUser()");
        Map user = createUpdateHelper.processMapping(scimuser);

        LOGGER.debug("createUser payload {}", user.toString());
        Map oktaUserTemp = null;
        Map userTemp = null;
        String jfgGuid = "";
        String customerId = "";
        String jfgguidFromDatahub =null;
        try {
            //Check user in okta
            oktaUserTemp = oktaApi.checkExistingMember(user.get("emailAddress").toString());

            if (oktaUserTemp != null) {
                Map profile =(Map) oktaUserTemp.get("profile");
                if (profile.containsKey("jfgguid")){
                   jfgGuid = profile.get("jfgguid").toString();
                }
                if (profile.containsKey("customerId")){
                    customerId = profile.get("customerId").toString();
                }
                if (!profile.containsKey("jfgguid") && !profile.containsKey("customerId")){
                   userTemp = johnsonFinancialGroupApi.creatMember(user, oktaUserTemp.get("id").toString());
                }
                if ("".equals(jfgGuid) && "".equals(customerId)){
                  userTemp =  johnsonFinancialGroupApi.creatMember(user, oktaUserTemp.get("id").toString());
                }
            }

            //Datahub response
            if (userTemp != null){
                if (userTemp.containsKey("responseDetails")){
                    Map responseDetails = (Map) userTemp.get("responseDetails");
                    if (responseDetails.containsKey("jfgguid")){
                        jfgguidFromDatahub = responseDetails.get("jfgguid").toString();
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error on creation of member::"+ex.getMessage());
        }

        if (!"".equals(jfgguidFromDatahub)){
            scimuser.put("id", jfgguidFromDatahub);
        }


        LOGGER.info("TERMINATING createUser()");
        return scimuser;
    }

}
