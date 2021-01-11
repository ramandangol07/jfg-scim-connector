package com.beyondid.scimConnector.jfgcp.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public interface GetUserService {
    /**
     * Retrive the user from Target system
     * input map contains startIndex, count and filter
     * **/
    public Map processGetUserRequest(Map<String, String> params) throws IOException;


}
