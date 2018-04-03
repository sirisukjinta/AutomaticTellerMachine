package main.atm.configuration;

import java.util.ArrayList;
import java.util.List;

import main.atm.util.KeyValuePair;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseDataAccessConfig {
	
	protected static final char PROPERTY_DELIMITER = '|';
	protected static final char PROPERTY_KEY_SEPARATOR = ':';

	protected List<KeyValuePair<String, String>> buildPropertiesFromString(String stringOfProps) {
		if (StringUtils.isBlank(stringOfProps)) {
			return null;
		}

		List<KeyValuePair<String, String>> propList = new ArrayList<KeyValuePair<String,String>>();
		String[] propStrings = StringUtils.split(stringOfProps, BaseDataAccessConfig.PROPERTY_DELIMITER);
		for (String item : propStrings) {
			String[] propParts = StringUtils.split(item, BaseDataAccessConfig.PROPERTY_KEY_SEPARATOR);
			if (propParts != null && propParts.length == 2) {
				propList.add(new KeyValuePair<String, String>(propParts[0], propParts[1]));
			}
		}
		
		return propList;
	}
}
