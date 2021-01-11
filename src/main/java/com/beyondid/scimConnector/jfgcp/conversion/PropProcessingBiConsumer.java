package com.beyondid.scimConnector.jfgcp.conversion;


import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;

public class PropProcessingBiConsumer implements BiConsumer<Object, Object> {

    Map<String, Object> input;
    Map<String, Object> output;

    private static final Logger LOGGER = LoggerFactory.getLogger(PropProcessingBiConsumer.class);

    private ApplicationConstants applicationConstants;
    private PropProcessingBiConsumerHelper propProcessingBiConsumerHelper;
    public PropProcessingBiConsumer(Map<String, Object> input, Map<String, Object> output,ApplicationConstants applicationConstants, PropProcessingBiConsumerHelper propProcessingBiConsumerHelper) {
        this.input = input;
        this.output = output;
        this.applicationConstants=applicationConstants;
        this.propProcessingBiConsumerHelper=propProcessingBiConsumerHelper;
    }
    @Override
    public void accept(Object key, Object value) {
        LOGGER.info("STARTING ACCEPT()");
        LOGGER.info("EXTRACTING VALUE FOR= {} AND CREATING FOR {}",key,value);
        Object inputValue=null;
        Boolean foundkey = propProcessingBiConsumerHelper.patternMatch(key.toString());
        Boolean foundValue = propProcessingBiConsumerHelper.patternMatch(value.toString());

        if(!foundkey && !foundValue){
            inputValue = readvalueFromInput(key);//extract the value
            if(((String) value).contains(":1--0:user:custom")){
                String custom=applicationConstants.CUSTOM_SCHEMA;
                String appname=custom.substring(0,custom.indexOf(":1.0:user:custom"));
                value=appname+value;
                Map finalOutput=createFinalMap((String) value);//create final map
                propProcessingBiConsumerHelper.insertValuestoMap((String)value,finalOutput,inputValue);//insert values to final map
            }else{
                Map finalOutput=createFinalMap((String) value);//create final map
                propProcessingBiConsumerHelper.insertValuestoMap((String)value,finalOutput,inputValue);//insert values to final map
            }

        }else{
            LOGGER.error("KEY and VALUE NOT SUPPORTED BY OUR CODEBASE {} : {}",key,value);
        }
        LOGGER.info("ENDING ACCEPT()");
    }


    public Map createNewMapAndList(Map out, String s) {
        LOGGER.info("STARTING createNewMapAndList(Map out, String s)");
        String token=s;
        String derivedtoken=propProcessingBiConsumerHelper.getValueparambeforeArraystart(token);
        List<Map> l = (List<Map>) out.get(derivedtoken);
        Map m;
        if (l == null) {
            l = new ArrayList<>();
            m=createMapAndPutType(propProcessingBiConsumerHelper.type(s),propProcessingBiConsumerHelper.value(s),l);
        } else{
            m = createMapAndPutType(propProcessingBiConsumerHelper.type(s), propProcessingBiConsumerHelper.value(s),l);
        }
        out.put(derivedtoken, l);
        LOGGER.info("ENDING createNewMapAndList(Map out, String s)");
        return m;
    }

    public Map createMapAndPutType(String type, String value, List<Map> l){
        LOGGER.info("STARTING createMapAndPutType(String type, String value, String token, List<Map> l)");
        Map result = new HashMap();
        boolean flag = true;
        for (Map tempMap:
                l) {
            if(tempMap.get(type)!=null && value.equals(tempMap.get(type))){
                result=tempMap;
                flag = false;
            }
        }
        if(flag)
            l.add(result);
        result.put(type,value);
        LOGGER.info("ENDING createMapAndPutType(String type, String value, String token, List<Map> l)");
        return result;
    }

    public Map createFinalMap(String value) {
        LOGGER.info("STARTING createFinalMap(String value)");
        StringTokenizer kst = new StringTokenizer(value, applicationConstants.JSON_STRINGTOKENIZER_DELIM);
        Map out=output;
        if(kst.countTokens() !=1) {
            while (kst.hasMoreElements()) {
                String token = kst.nextToken();
                if (kst.countTokens() != 0) {
                    if(token.contains(applicationConstants.JSON_ARRAY_START_IDENTIFIER)) {
                        out = createNewMapAndList(out, token);
                    }
                    else if(out.get(token)==null){
                        out=insertKeyValueOperation(out,token);
                    }else{
                        out= (Map) out.get(token);
                    }
                }
            }
        }
        LOGGER.info("ENDING createFinalMap(String value)");
        return out;
    }

    Map insertKeyValueOperation(Map out,String token){
        Map m=new HashMap();
        if(out.get(propProcessingBiConsumerHelper.replaceTokenandput(token))!=null){
            out= (Map) out.get(propProcessingBiConsumerHelper.replaceTokenandput(token));
        }else {
            out.put(propProcessingBiConsumerHelper.replaceTokenandput(token), m);
            out = m;
        }
        return out;
    }

    public Object readvalueFromInput(Object key) {
        LOGGER.info("STARTING readvalueFromInput(Object key)");
        StringTokenizer kst = new StringTokenizer((String) key, applicationConstants.JSON_STRINGTOKENIZER_DELIM);
        Object tempInput = input;

        while(kst.hasMoreElements()){
            String orgToken=kst.nextToken();
            String drivedToken = orgToken;
            if(orgToken.contains(applicationConstants.JSON_ARRAY_START_IDENTIFIER)) {
                drivedToken = propProcessingBiConsumerHelper.getKeyParameterBeforeArraystarttoken(orgToken);
            }
            if(orgToken.contains(":1--0:user:custom")) {
                String custom = applicationConstants.CUSTOM_SCHEMA;
                String appname = custom.substring(0, custom.indexOf(":1.0:user:custom"));
                drivedToken = appname + propProcessingBiConsumerHelper.replaceTokenandput(orgToken);
            }
            if(orgToken.contains(applicationConstants.JSON_UNDERSCORE_REPRESENTOR)){
                drivedToken=orgToken.replace(applicationConstants.JSON_UNDERSCORE_REPRESENTOR,applicationConstants.SPACE_REPRESENTOR+applicationConstants.SPACE_REPRESENTOR);
            }
            if(tempInput instanceof Map) {
                tempInput = ((Map) tempInput).get(drivedToken);
                if(tempInput instanceof List){
                    tempInput=instanceOfList(tempInput,orgToken);
                }
            }
        }
        LOGGER.info("ENDING readvalueFromInput(Object key)");
        return tempInput;
    }

    Object adpCaseI(Object tempInput,String paramValue,String paramName){
        for (Object out : (List) tempInput) {
            Map intMap = (Map) ((Map) out).get("typeCode");
            if (paramValue.equals(intMap.get(paramName))) {
                tempInput =((Map) out).get("nameCode");
                break;
            }else {
                tempInput=null;
            }
        }
        return tempInput;
    }

    Object adpCaseII(Object tempInput,String paramValue,String paramName){
        for (Object out : (List) tempInput) {
            Map intMap = (Map) ((Map) out).get("nameCode");
            if (paramValue.equals( intMap.get(paramName))) {
                tempInput =  out;
                break;
            }else{
                tempInput=null;
            }
        }
        return tempInput;
    }

    Object normalCase(Object tempInput,String paramValue,String paramName){
        for (Object out : (List) tempInput) {
            if (paramValue.equals(((Map) out).get(paramName))) {
                tempInput = out;
                break;
            }
        }
        return tempInput;
    }

    Object instanceOfList(Object tempInput,String orgToken){
        if(orgToken.contains(applicationConstants.JSON_KEY_ARRAY_IDENTIFIER)){
            String paramName = propProcessingBiConsumerHelper.getparamNameAfterArraystarttoken(orgToken);
            String paramValue = propProcessingBiConsumerHelper.getparamValueBeforeArrayendtoken(orgToken);
            if(paramValue.contains(applicationConstants.SPACE_REPRESENTOR)){
                paramValue=paramValue.replace(applicationConstants.SPACE_REPRESENTOR," ");
            }
            //to handle special case in ADP JSON format
            if(orgToken.indexOf(applicationConstants.JSON_KEY_ARRAY_SPECIAL_IDENTIFIER) > -1){
                tempInput=adpCaseI(tempInput,paramValue,paramName);
            }
            //to handle special case in ADP JSON format
            else if(orgToken.indexOf(applicationConstants.JSON_KEY_ARRAY_SPECIAL_IDENTIFIERS)>-1){
                tempInput=adpCaseII(tempInput,paramValue,paramName);
            }
            //for Normal cases
            else {
               tempInput=normalCase(tempInput,paramValue,paramName);
            }
        }else{
            int paramNameInt = propProcessingBiConsumerHelper.getparamValueIntBeforeArrayendtoken(orgToken);

            tempInput= ((List) tempInput).get(paramNameInt);
        }
        return  tempInput;
    }
}
