package com.beyondid.scimConnector.jfgcp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConstants {

    @Value("${json.custom.prop.file}")
    public String JSON_CUSTOM_PROP_FILE;

    @Value ("${regex}")
    public String regex;

    @Value("${json.stringtokenizer.delim}")
    public String JSON_STRINGTOKENIZER_DELIM;

    @Value("${json.array.start.token}")
    public String JSON_ARRAY_START_IDENTIFIER;

    @Value("${json.array.end.token}")
    public String JSON_ARRAY_END_IDENTIFIER;

    @Value("${json.array.item.identfier}")
    public String JSON_ARRAY_IDENTIFIER;

    @Value("${json.prop.key.array.item.identfier}")
    public String JSON_KEY_ARRAY_IDENTIFIER;

    @Value("${json.dot.replacement}")
    public String JSON_DOT_REPLACEMENT;

    @Value("${json.underscore.representor}")
    public String JSON_UNDERSCORE_REPRESENTOR;

    @Value("${schemas.core}")
    public String CORE_SCHEMA;

    @Value("${schemas.extension}")
    public String EXTENSION_SCHEMA;

    @Value("${schemas.custom}")
    public String CUSTOM_SCHEMA;
    
    @Value("${json.conversion.prop.file}")
    String JSON_CONVERSION_PROP_FILE;
    
    @Value("${json.conversion.extAppToScim.prop.file}")
    String JSON_CONVERSION_EXTAPPTOSCIM_FILE;

    @Value("${json.prop.key.array.item.special.identfier}")
    public String JSON_KEY_ARRAY_SPECIAL_IDENTIFIER;

    @Value("${json.prop.key.array.item.special.identfiers}")
    public String JSON_KEY_ARRAY_SPECIAL_IDENTIFIERS;

    @Value("${json.space.representor}")
    public String SPACE_REPRESENTOR;

    @Value("${json.conversion.scimtoExtapp.prop}")
    String JSON_CONVERSION_SCIMTOEXTAPP_PROP_FILE;
    

    @Value("${johnson.prop.file}")
    public String JOHNSON_APP_INFO;

}
