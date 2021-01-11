package com.beyondid.scimConnector.jfgcp.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface GetUserServiceHelper {

    public Map collectUsersData(int count, int StartIndex, String filter) throws IOException;

    public List toScimStandard(Object users) throws IOException;

    public Map returnData(List<Map> foundUsers, int initialIndex, int itemsPerPage, int totalUsers);
}
