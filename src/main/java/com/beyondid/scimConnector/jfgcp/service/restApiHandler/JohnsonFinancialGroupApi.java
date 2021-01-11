package com.beyondid.scimConnector.jfgcp.service.restApiHandler;

import com.beyondid.scimConnector.jfgcp.service.implHttpHelper.urlbuild.HttpService;
import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import com.beyondid.scimConnector.jfgcp.util.JsonConversionUtil;
import com.beyondid.scimConnector.jfgcp.util.PropLoadUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Component
@PropertySource("classpath:application.properties")
public class JohnsonFinancialGroupApi {

	@Autowired
	private JsonConversionUtil jsonConversionUtil;

	@Autowired
	private PropLoadUtil propLoadUtil;

	@Autowired
	private ApplicationConstants applicationConstants;

	@Autowired
	@Qualifier("johnsonFinancialRestCall")
	private HttpService johnsonServiceRestApiHttpService;

	 @Autowired
	 private Environment env;


	public Map creatMember(Map user, String immutableId) {
		Map nonCustomer = new HashMap();
		nonCustomer.put("non-customer", user);

		String payload = jsonConversionUtil.converMaptoString(nonCustomer);

		Properties apiInfo = propLoadUtil.loadMappingProp(applicationConstants.JOHNSON_APP_INFO);
		String url = apiInfo.getProperty("baseUrl") + apiInfo.getProperty("createMember") ;
		String response = johnsonServiceRestApiHttpService.doRequest(url, HttpMethod.POST, payload, immutableId).getBody();

		Map createResponse = jsonConversionUtil.convertStringtoMap(response);

		return createResponse;

	}

	public Map updateMember(Map user, String immutableId, String jfgId) {
		Map nonCustomer = new HashMap();
		nonCustomer.put("non-customer", user);

		String payload = jsonConversionUtil.converMaptoString(nonCustomer);

		Properties apiInfo = propLoadUtil.loadMappingProp(applicationConstants.JOHNSON_APP_INFO);
		String url = apiInfo.getProperty("baseUrl") + apiInfo.getProperty("createMember") + "/" + jfgId;
		String response = johnsonServiceRestApiHttpService.doRequest(url, HttpMethod.PUT, payload, immutableId).getBody();

		Map createResponse = jsonConversionUtil.convertStringtoMap(response);

		return createResponse;

	}






}
