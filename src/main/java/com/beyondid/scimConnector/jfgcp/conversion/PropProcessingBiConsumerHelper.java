package com.beyondid.scimConnector.jfgcp.conversion;

import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PropProcessingBiConsumerHelper {

    @Autowired
    private ApplicationConstants applicationConstants;

    public String replaceTokenandput(String token){
        return token.replace(applicationConstants.JSON_DOT_REPLACEMENT,applicationConstants.JSON_STRINGTOKENIZER_DELIM);
    }

    public Boolean patternMatch(String keyvalue){
        Pattern response = Pattern.compile(applicationConstants.regex);
        Matcher matchkeyOrValue = response.matcher(keyvalue);
        return matchkeyOrValue.find();

    }

    public void insertValuestoMap(String value, Map finalOutput, Object inputValue) {
        if(inputValue!=null) {
            finalOutput.put(value.substring(value.lastIndexOf(applicationConstants.JSON_STRINGTOKENIZER_DELIM) + 1), inputValue);
        }
    }

    public String type(String extractType){
        return extractType.substring(getIndexofArrayStartIdentifier(extractType)+1,getIndexofArrayIdentfier(extractType));
    }

    public String value(String extractValue){
        return extractValue.substring(getIndexofArrayIdentfier(extractValue)+1,getIndexofArrayEndIdentifier(extractValue));
    }

    public int getIndexofArrayIdentfier(String s) {
        return s.indexOf(applicationConstants.JSON_ARRAY_IDENTIFIER);
    }

    public int getIndexofArrayStartIdentifier(String s){
        return s.indexOf(applicationConstants.JSON_ARRAY_START_IDENTIFIER);
    }

    public int getIndexofArrayEndIdentifier(String s){
        return s.indexOf(applicationConstants.JSON_ARRAY_END_IDENTIFIER);
    }

    public int getIndexofKeyArrayIdentfier(String s){
        return s.indexOf(applicationConstants.JSON_KEY_ARRAY_IDENTIFIER);
    }

    public String getKeyParameterBeforeArraystarttoken(String orgToken){
        return orgToken.substring(0, getIndexofArrayStartIdentifier(orgToken));
    }

    public String getparamNameAfterArraystarttoken(String orgToken){
        return orgToken.substring(getIndexofArrayStartIdentifier(orgToken)+1,getIndexofKeyArrayIdentfier(orgToken));
    }

    public String getparamValueBeforeArrayendtoken(String orgToken){
        return orgToken.substring(orgToken.lastIndexOf(applicationConstants.JSON_KEY_ARRAY_IDENTIFIER)+1,getIndexofArrayEndIdentifier(orgToken));
    }

    public int getparamValueIntBeforeArrayendtoken(String orgToken){
        return Integer.parseInt(orgToken.substring(getIndexofArrayStartIdentifier(orgToken)+1,getIndexofArrayEndIdentifier(orgToken)));
    }

    public String getValueparambeforeArraystart(String token){
        return token.substring(0, getIndexofArrayStartIdentifier(token));
    }

}
