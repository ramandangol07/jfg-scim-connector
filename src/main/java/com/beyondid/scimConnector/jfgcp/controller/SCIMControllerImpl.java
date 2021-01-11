package com.beyondid.scimConnector.jfgcp.controller;

import com.beyondid.scimConnector.jfgcp.service.GetUserService;
import com.beyondid.scimConnector.jfgcp.service.helper.CreateUserHelper;
import com.beyondid.scimConnector.jfgcp.service.helper.SingleUserPutHelper;
import com.beyondid.scimConnector.jfgcp.util.JsonConversionUtil;
import com.beyondid.scimConnector.jfgcp.util.UserManagementCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class SCIMControllerImpl implements SCIMController {

    @Autowired
    private GetUserService getUserService;

    @Autowired
    private CreateUserHelper createUserHelper;

    @Autowired
    private SingleUserPutHelper singleUserPutHelper;

    @Autowired
    private JsonConversionUtil jsonConversionUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(SCIMControllerImpl.class);

    @Override
    public @ResponseBody Map getUsers(@RequestParam Map<String, String> pagination, HttpServletResponse httpResponse) throws IOException {
        LOGGER.info("INITIATING getUsers()");
        LOGGER.info("pagination: {}.",pagination);
    	Map toScimStandard=null;
        if(pagination!=null) {
            toScimStandard = getUserService.processGetUserRequest(pagination);
        }
        LOGGER.debug("RETURN DATA: {}",toScimStandard);
        LOGGER.info("TERMINATING getUsers()");
        return toScimStandard;
    }
    
	@Override
    public @ResponseBody Map createUser(@RequestBody Map<String, Object> scimuser, HttpServletResponse response) throws IOException {
        LOGGER.info("INITIATING createUser()");
        LOGGER.debug("INITIATING createUser(){}",scimuser);
        if(scimuser!=null){
            scimuser=createUserHelper.processCreateUserRequest(scimuser);
        }
        LOGGER.debug("RETURN DATA: {}",scimuser);
        LOGGER.info("TERMINATING createUser()");
        return scimuser;
    }



    @Override
    public @ResponseBody Map singleUserPut(@RequestBody Map<String, Object> payload,@PathVariable String id) throws IOException {
        LOGGER.info("INITIATING singleUserPut()  Map<String, Object> payload {} id {}",payload,id);
        Map toscimuser=null;
        if(payload!=null && (id!=null && id!="")){
            toscimuser=singleUserPutHelper.processSingleUserPutRequest(payload,id);
        }
        LOGGER.debug("TERMINATING singleUserPut(){}",toscimuser);
        LOGGER.info("TERMINATING singleUserPut()");
        return toscimuser;
    }




    /**
     * This method returns the User management/provisioning capabilities implemented by the connector.
     * It is required that you implement this method since it informs the
     * Okta provisioning feature that your connector is implemented.
     * **/
    public UserManagementCapabilities[] getImplementedUserManagementCapabilities(){
        return new UserManagementCapabilities[]{
                UserManagementCapabilities.IMPORT_NEW_USERS,
                UserManagementCapabilities.PUSH_NEW_USERS,
                UserManagementCapabilities.PUSH_PROFILE_UPDATES,
                UserManagementCapabilities.PUSH_USER_DEACTIVATION,
                UserManagementCapabilities.REACTIVATE_USERS,
                UserManagementCapabilities.PUSH_PASSWORD_UPDATES
//                UserManagementCapabilities.IMPORT_PROFILE_UPDATES
        };    

    }

}
