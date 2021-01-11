package com.beyondid.scimConnector.jfgcp.service.impl;


import com.beyondid.scimConnector.jfgcp.service.GetUserService;
import com.beyondid.scimConnector.jfgcp.service.GetUserServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class GetUserServiceImpl implements GetUserService {

    @Autowired
    private GetUserServiceHelper getUserServiceHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserServiceImpl.class);

    @Override
    public Map processGetUserRequest(Map<String, String> params) throws IOException {
        LOGGER.info("EXTRACTING processGetUserRequest()");

        int count=0;
        if(params.get("count")!=null) {
            count = Integer.parseInt(params.get("count"));
        }

        int startIndex=0;
        if(params.get("startIndex")!=null) {
            startIndex = Integer.parseInt(params.get("startIndex"));
            if(startIndex<1){
                startIndex=1;
            }
        }

        String filter=null;
        if(params.get("filter")!=null) {
            filter = params.get("filter");

        }

        Map foundUsers=getUserServiceHelper.collectUsersData(count,startIndex,filter);
        LOGGER.debug("FOUND USERS: {}",foundUsers);
        LOGGER.info("EXTRACTED processGetUserRequest()");

        return foundUsers;
    }

}
