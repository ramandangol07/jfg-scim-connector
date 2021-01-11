package com.beyondid.scimConnector.jfgcp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonConversionUtilImpl implements JsonConversionUtil {

    public final Gson gson = new GsonBuilder().registerTypeAdapterFactory(CustomizedObjectTypeAdapter.FACTORY).create();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConversionUtilImpl.class);

    @Override
    public Map convertStringtoMap(String json) {


        Map map=null;
        try {
            map=gson.fromJson(json,Map.class);
        } catch (Exception e) {
            LOGGER.error("ERROR Converting JSON String to MAP {}",json,e);
            throw new RuntimeException("ERROR Converting JSON String to MAP" + json);
        }
        return map;
    }

    @Override
    public String converMaptoString(Object map) {

        String s=null;
        try {
            s=gson.toJson(map);
        } catch (Exception e) {
            LOGGER.error("ERROR Converting MAP to JSON String  {}",map,e);
            throw new RuntimeException("ERROR Converting MAP to JSON STRING" + map);
        }
        return s;
    }

    @Override
    public List convertStringtoList(String json){

        List list=null;
        try {
            list=gson.fromJson(json,List.class);
        } catch (Exception e) {
            LOGGER.error("ERROR Converting JSON String to LIST {}",json,e);
            throw new RuntimeException("ERROR Converting JSON String to LIST" + json);
        }
        return list;
    }

}
