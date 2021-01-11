package com.beyondid.scimConnector.jfgcp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PropLoadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropLoadUtil.class);

    @Autowired
    private ApplicationConstants applicationConstants;


    private  Properties jsonConversionProp;

    public Properties loadJsonConversionProperties(){
        if(this.jsonConversionProp == null){
            jsonConversionProp = loadMappingProp(applicationConstants.JSON_CONVERSION_PROP_FILE);
        }
        return jsonConversionProp;
    }

    private Properties jsonConversionScimToExtAppProperties;
    public Properties loadJsonConversionScimToExtAppProperties(){

        if(this.jsonConversionScimToExtAppProperties == null){
            jsonConversionScimToExtAppProperties = loadMappingProp(applicationConstants.JSON_CONVERSION_SCIMTOEXTAPP_PROP_FILE);
        }
        return jsonConversionScimToExtAppProperties;
    }
    private  Properties jsonCustomMappingProp;

    public Properties loadJsonCustomMappingProperties(){
        if(this.jsonCustomMappingProp == null){
            jsonCustomMappingProp = loadMappingProp(applicationConstants.JSON_CUSTOM_PROP_FILE);
        }
        return jsonCustomMappingProp;
    }


    public Properties loadMappingProp(String fileName) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(fileName);
            prop.load(input);
        } catch (FileNotFoundException e) {
            LOGGER.error("ERROR Reading Input file"+fileName,e);
           throw new RuntimeException("Input file not found" + fileName);
        } catch (IOException e) {
            LOGGER.error("ERROR Reading Input file"+fileName,e);
            throw new RuntimeException("Input file not found" + fileName);
        }

        return prop;
    }
}