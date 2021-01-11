package com.beyondid.scimConnector.jfgcp.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

@Component
public class JsonLoadUtil {
    @Autowired
    private ApplicationConstants applicationConstants;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLoadUtil.class);

    Map jsonload(String fileName){
        Gson gson = new Gson();
        JsonReader reader = null;
        Map data=null;
        try {
            reader = new JsonReader(new FileReader(fileName));
            data = gson.fromJson(reader, Map.class);
        } catch (FileNotFoundException e) {
            LOGGER.error("ERROR Reading Input file {} {}",fileName,e);
            throw new RuntimeException("Input file not found: "+fileName);
        }
        return data;
    }
}

