package com.beyondid.scimConnector.jfgcp.conversion;

import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import com.beyondid.scimConnector.jfgcp.util.JsonConversionUtilImpl;
import com.beyondid.scimConnector.jfgcp.util.PropLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Qualifier("JSONTransformationScimToExternalApp")
public class JSONTransformationScimToExternalApp{
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONTransformationScimToExternalApp.class);

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private PropProcessingBiConsumerHelper propProcessingBiConsumerHelper;

    @Autowired
    private PropLoadUtil propLoadUtil;

    @Autowired
    private JsonConversionUtilImpl jsonConversionUtil;

    private List finalOut =new ArrayList();
    public Map transformJson(Object obj,Properties prop) throws IOException {
        Map<String, Object> map;
        if (obj instanceof Map) {
            map = processMap((Map)obj,prop);
        } else {
            map = processList((List)obj,prop);
        }
        return map;
    }
    public Map processList(List input,Properties prop) throws   IOException {
        LOGGER.info("PROCESSING LIST");
        Map mapinList=null;
        for (Object elementinList:input){
            mapinList= (Map) elementinList;
            mapinList=processMap(mapinList,prop);
            finalOut.add(mapinList);
        }
        LOGGER.debug("FINAL LIST RESOURCES : {}.", finalOut);
        LOGGER.info("PROCESSED LIST");
        return mapinList;
    }


    public Map processMap(Map<String,Object> input,Properties prop){
        LOGGER.info("PROCESSING MAP");
        Map<String,Object> output = new HashMap<>();
        PropProcessingBiConsumer propProcessingBiConsumer = new PropProcessingBiConsumer(input,output,applicationConstants,propProcessingBiConsumerHelper);
        prop.forEach(propProcessingBiConsumer);
        LOGGER.debug("PROCESS OUTPUT : {0}",output);
        LOGGER.info("PROCESSED MAP");
        return output;

    }
}
