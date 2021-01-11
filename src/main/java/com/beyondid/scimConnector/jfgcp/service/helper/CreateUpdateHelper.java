package com.beyondid.scimConnector.jfgcp.service.helper;

import com.beyondid.scimConnector.jfgcp.conversion.JSONTransformationScimToExternalApp;
import com.beyondid.scimConnector.jfgcp.util.ApplicationConstants;
import com.beyondid.scimConnector.jfgcp.util.JsonLoadUtil;
import com.beyondid.scimConnector.jfgcp.util.PropLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

@Component
public class CreateUpdateHelper {
	@Autowired
	@Qualifier("JSONTransformationScimToExternalApp")
	private JSONTransformationScimToExternalApp jsonTransformationScimToExternalApp;

	@Autowired
	private ApplicationConstants applicationConstants;

	@Autowired
	PropLoadUtil propLoadUtil;

	@Autowired
	JsonLoadUtil jsonLoadUtil;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public Map processMapping(Map<String, Object> scimuser) {
		Map nonCustomer = null;
		LOGGER.info("PROCESSING MAPPINGS");
		Properties propertiesPayload = propLoadUtil.loadJsonConversionScimToExtAppProperties();
		Map out = jsonTransformationScimToExternalApp.processMap(scimuser, propertiesPayload);
		/*String status = out.get("status").equals(true) ? "active" : "deleted";
		out.put("status", status);*/

		if (scimuser.containsKey(applicationConstants.CUSTOM_SCHEMA)) {
			Map<String, Object> custom = (Map<String, Object>) scimuser.get(applicationConstants.CUSTOM_SCHEMA);
			Properties propertiesCustom = propLoadUtil.loadJsonCustomMappingProperties();
			custom.forEach((sa, o) -> {
				if (propertiesCustom.containsKey(sa)) {

					process(propertiesCustom.getProperty(sa), out, o);

				}
			});
		}
		LOGGER.info("PROCESSED MAPPINGS");

		return out;
	}

	public void process(String value, Map output, Object insertValue) {
		insertValue = insertValue instanceof String && insertValue != null ? ((String) insertValue).trim()
				: insertValue;
		if (!"".equals(insertValue) && insertValue != null) {
			StringTokenizer tkn = new StringTokenizer(value, ".");
			Map out = output;
			while (tkn.hasMoreElements()) {
				String dtkn = tkn.nextToken();
				if (out.get(dtkn) == null && tkn.countTokens() != 0) {
					out.put(dtkn, new HashMap());
					out = (Map) out.get(dtkn);
				}
			}
			String derived = value.substring(value.lastIndexOf('.') + 1, value.length());
			String key = derived.contains("=") ? derived.substring(0, derived.indexOf("=")) : derived;
			String prefix = derived.contains("=")
					? derived.substring(derived.indexOf("=") + 1, derived.indexOf("'") + 1)
					: null;
			String suffix = derived.contains("=") ? derived.substring(derived.lastIndexOf("'"), derived.length())
					: null;
			Object createValue = prefix != null && suffix != null ? prefix + insertValue + suffix : insertValue;
			out.put(key, createValue);
		}
	}
}
