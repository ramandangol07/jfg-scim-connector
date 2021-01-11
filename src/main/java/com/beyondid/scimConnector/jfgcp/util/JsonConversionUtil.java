package com.beyondid.scimConnector.jfgcp.util;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface JsonConversionUtil {


    public Map convertStringtoMap(String json);

    public String converMaptoString(Object map);

    public List convertStringtoList(String json);

}
