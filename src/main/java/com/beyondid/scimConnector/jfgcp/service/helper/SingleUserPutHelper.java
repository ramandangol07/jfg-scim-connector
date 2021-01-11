package com.beyondid.scimConnector.jfgcp.service.helper;

import com.beyondid.scimConnector.jfgcp.conversion.JSONTransformationScimToExternalApp;
import com.beyondid.scimConnector.jfgcp.service.restApiHandler.JohnsonFinancialGroupApi;
import com.beyondid.scimConnector.jfgcp.service.restApiHandler.OktaApi;
import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import com.beyondid.scimConnector.jfgcp.util.JsonConversionUtil;
import com.beyondid.scimConnector.jfgcp.util.PropLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class SingleUserPutHelper {
    @Autowired
    @Qualifier("JSONTransformationScimToExternalApp")
    private JSONTransformationScimToExternalApp jsonTransformationScimToExternalApp;
    
    @Autowired
    private PropLoadUtil propLoadUtil;
    
    @Autowired
    private JsonConversionUtil jsonConversionUtil;

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    CreateUpdateHelper createUpdateHelper;

    @Autowired
    private JohnsonFinancialGroupApi johnsonFinancialGroupApi;

    @Autowired
    private OktaApi oktaApi;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserHelper.class);
    public Map processSingleUserPutRequest(Map<String, Object> payload,String id) throws IOException {
        return updateUserInExternalApplication(payload,id);
    }
    private Map updateUserInExternalApplication(Map<String, Object> payload,String id) throws IOException {
        LOGGER.info("INITIATING UpdateUser()");
        String url=null;
        ResponseEntity res=null;
        if(payload.get("active").equals(true)){
            LOGGER.info("STARTING USER UPDATE");
            updateUser(payload,id);
            LOGGER.info("ENDING USER UPDATE");
        }else{
            LOGGER.info("STARTING USER DEACTIVATION");
            System.out.println("deactivate");
//            deactivateUser(payload,id);
            LOGGER.info("ENDING USER DEACTIVATION");
        }
        LOGGER.info("TERMINATING UpdateUser()");
        return payload;
    }

    public void updateUser(Map<String, Object> payload,String id){
    	Map out=createUpdateHelper.processMapping(payload);
        Map user ;
        Map oktaUserTemp;
        try {
            //Check user in okta
            oktaUserTemp = oktaApi.checkExistingMember(payload.get("userName").toString());
            if (oktaUserTemp != null){
                user = johnsonFinancialGroupApi.updateMember(out, oktaUserTemp.get("id").toString(), id);
            }
        }catch (Exception ex){
            LOGGER.error("Error on update of member::"+ex.getMessage());
        }
    }
    
/*    public void deactivateUser(Map<String, Object> payload,String id){
        Map out=createUpdateHelper.processMapping(payload);
        out.put("id", Long.parseLong(id));
        JSONObject user = new JSONObject();
        try {
//            JSONObject token = classyApi.getMemberToken();
//            user = classyApi.deactivateMember(out, token.get("access_token").toString());
            user = testAppApi.deactivateMember(out, null);
        }catch (Exception ex){
            LOGGER.error("Error on deactivate of member::"+ex.getMessage());
        }
    }*/
}
