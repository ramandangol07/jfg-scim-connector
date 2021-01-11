package com.beyondid.scimConnector.jfgcp.controller;


import com.beyondid.scimConnector.jfgcp.util.UserManagementCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/ServiceProviderConfigs"})
public class SCIMServiceProviderConfigsController {
    @Autowired
    private SCIMControllerImpl service;
    private static final String PROVIDER_CONFIG_PREFIX = "{\n  \"schemas\": [\"urn:scim:schemas:core:1.0\",\"urn:okta:schemas:scim:providerconfig:1.0\"],\n  \"documentationUrl\":\"https://support.okta.com/scim-fake-page.html\",\n  \"patch\": {\n    \"supported\":false\n  },\n  \"bulk\": {\n    \"supported\":false\n  },\n  \"filter\": {\n    \"supported\":true,\n    \"maxResults\": 100\n  },\n  \"changePassword\" : {\n    \"supported\":true\n  },\n  \"sort\": {\n    \"supported\":false\n  },\n  \"etag\": {\n    \"supported\":false\n  },\n  \"authenticationSchemes\": [],\n  \"urn:okta:schemas:scim:providerconfig:1.0\": {    \"userManagementCapabilities\": [";
    private static final String PROVIDER_CONFIG_SUFFIX = "    ]  }}";



    @GetMapping
    public @ResponseBody String getProviderConfig() {
        StringBuilder response = new StringBuilder(PROVIDER_CONFIG_PREFIX);
        UserManagementCapabilities[] userManagementCapabilities = this.service.getImplementedUserManagementCapabilities();
        if (userManagementCapabilities != null) {
            for(int i = 0; i < userManagementCapabilities.length; ++i) {
                if (i > 0) {
                    response.append(", ");
                }

                response.append("\"");
                response.append(userManagementCapabilities[i].toString());
                response.append("\"");
            }
        }

        response.append(PROVIDER_CONFIG_SUFFIX);
        return response.toString();
    }
}
