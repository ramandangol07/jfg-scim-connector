package com.beyondid.scimConnector.jfgcp.service.impl;

import com.beyondid.scimConnector.jfgcp.conversion.JSONTransformation;
import com.beyondid.scimConnector.jfgcp.service.GetUserServiceHelper;
import com.beyondid.scimConnector.jfgcp.service.restApiHandler.OktaApi;
import com.beyondid.scimConnector.jfgcp.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GetUserServiceHelperImpl implements GetUserServiceHelper {



	@Autowired
	private ApplicationConstants applicationConstants;

	@Autowired
	private PropLoadUtil propLoadUtil;

	@Autowired
	private JsonConversionUtil jsonConversionUtil;

	@Autowired
	@Qualifier("JSONTransformation")
	private JSONTransformation jsonTransformation;

	@Autowired
	GetUserServiceHelperImpl getUserServiceHelper;

	@Autowired
	JsonLoadUtil jsonLoadUtil;

	@Autowired
	private OktaApi oktaApi;

	private static final Logger LOGGER = LoggerFactory.getLogger(GetUserServiceHelperImpl.class);

	@Override
	public Map collectUsersData(int count, int startIndex, String filter) throws IOException {
		LOGGER.info("STARTING collectUsersData()");

		Map<String, Object> users = null;
		Map<String, Object> returndata = null;
		String jfgguid = null;
		String customerId = null;
		List foundUsers = null;
		if (!isNullOrBlank(filter)) {
			LOGGER.info("Starting single user search()");
			Map userEntity = apiCall(filter);

			if (userEntity != null) {
				if (userEntity.containsKey("profile")){
					Map userProfile = (Map) userEntity.get("profile");
					if(userProfile.containsKey("jfgguid")){
						jfgguid = userProfile.get("jfgguid").toString();
					}
					if (userProfile.containsKey("customerId")){
						customerId = userProfile.get("customerId").toString();
					}
					if (jfgguid != null){
						foundUsers = toScimStandard(userEntity);
						returndata = returnData(foundUsers, 1, 1, foundUsers.size());
						LOGGER.info("single user search() :: user found");
					}else {
						foundUsers = new ArrayList();
						returndata = returnData(foundUsers, 0, 0, foundUsers.size());
						LOGGER.info("single user search() :: user not found");
					}

				}

			/*	foundUsers = toScimStandard(userEntity);
				returndata = returnData(foundUsers, 1, 1, foundUsers.size());
				LOGGER.info("single user search() :: user found");*/
			} else {
				foundUsers = new ArrayList();
				returndata = returnData(foundUsers, 0, 0, foundUsers.size());
				LOGGER.info("single user search() :: user not found");
			}
			LOGGER.info("Ending single user search()");
		}
		/*else {
			LOGGER.info("Begin Import Block");
			int totalPage = count/100;
			try {
				List<JSONObject> totalImport = new ArrayList<JSONObject>();
				int initialStartIndex = startIndex;
				for (int i = 0; i < totalPage; i++) {
					startIndex = startIndex;
					List<JSONObject> allUsers = classyApi.getAllMembers(token.get("access_token").toString(), startIndex, 100);
					totalImport.addAll(allUsers);
					startIndex += 1;
				}
				List<Map<String, Object>> usr = jsonConversionUtil.convertStringtoList(totalImport.toString());
				foundUsers = toScimStandard(usr);
				returndata = returnData(foundUsers, initialStartIndex, count, totalImport.size());
				LOGGER.info("Ending Import Block");
			}
			catch (Exception ex){
				LOGGER.error("Error on Import Block::"+ex.getMessage());
			}
		}*/
		LOGGER.info("ENDED collectUsersData() {}");
		return returndata;
	}

	@Override
	public List toScimStandard(Object users) throws IOException {
		LOGGER.info("STARTING adptoScimStandard(Map users)");
		List<Map> scimUsers = new ArrayList();
		Properties prop = propLoadUtil.loadJsonConversionProperties();
		if (users != null) {

			if (users instanceof Map) {
				Map user = (Map) users;
				Map output = jsonTransformation.processMap(user, prop);
				addOrModifyAttributes(output);
				scimUsers.add(output);
			} else if (users instanceof List) {

				((List) users).forEach(o -> {
					Map output = jsonTransformation.processMap( (Map<String, Object>)  o, prop);
					addOrModifyAttributes(output);
					scimUsers.add(output);
				});

			}
		}
		LOGGER.info("ENDED toScimStandard(Map users)");
		return scimUsers;
	}

	private boolean isNullOrBlank(String s) {
		return (s == null || s.trim().equals(""));
	}

	public void addOrModifyAttributes(Map output) {

		output.put("externalId", output.get("id"));
		output.put("active", output.get("active").equals("active") ? true : false);
		if (output.containsKey("emails")) {
			List<Map<String, Object>> emails = (List<Map<String, Object>>) output.get("emails");
			output.put("userName", emails.get(0).get("value"));
			emails.forEach(stringObjectMap -> {
				if (stringObjectMap.get("type").equals("business")) {
					stringObjectMap.put("type", "work");
					stringObjectMap.put("primary", true);
				}
			});
		}
		if (output.containsKey("phoneNumbers")) {
			List<Map<String, Object>> phoneNumbers = (List<Map<String, Object>>) output.get("phoneNumbers");
			phoneNumbers.forEach(stringObjectMap -> {
				if (stringObjectMap.get("type").equals("mobile")) {
					stringObjectMap.put("primary", true);
				}
			});
		}
		output.put("schemas", buildSchema());

	}

	public Map apiCall(String filter) {
		String userid = null;
		if (filter != null && filter.contains("eq")) {
			String regex = "(\\w+) eq \"([^\"]*)\"";
			Pattern response = Pattern.compile(regex);
			Matcher match = response.matcher(filter);
			Boolean found = match.find();
			if (found) {
				match.regionStart();
				String searchKeyName = match.group(1);
				String searchValue = match.group(2);
				switch (searchKeyName) {
				case "userName":
					userid = searchValue;
					userid = userid.replace("+", "%2B");
					break;

				}
			}
		}
		Map user = null;
		try {
			user = oktaApi.checkExistingMember(userid);

		}catch (Exception ex){
			LOGGER.error("Error on getting existing member::"+ex.getMessage());
		}

		if (user == null) {
			return null;
		} 
		return user;
	}

	public List buildSchema() {
		List schema = new ArrayList();
		schema.add(applicationConstants.CORE_SCHEMA);
		schema.add(applicationConstants.CUSTOM_SCHEMA);

		return schema;
	}

	@Override
	public Map returnData(List<Map> foundUsers, int initialIndex, int itemsPerPage, int totalUsers) {
		LOGGER.info("STARTING returnData()");
		List listresponseschemas = new ArrayList();
		listresponseschemas.add(applicationConstants.CORE_SCHEMA);

		List<Map> users = null;
		Map<String, Object> returndata = new HashMap();
		returndata.put("schemas", listresponseschemas);
		returndata.put("totalResults", totalUsers);
		returndata.put("startIndex", initialIndex);
		returndata.put("itemsPerPage", itemsPerPage);
		if (foundUsers.size() > 1) {
			users = foundUsers;
		} else if (foundUsers.size() == 1) {
			users = foundUsers;
		} else {
			users = new ArrayList<>();
		}
		returndata.put("Resources", users);
		LOGGER.debug("returndata: {}", returndata);
		LOGGER.info("ENDED returnData()");
		return returndata;
	}

}
