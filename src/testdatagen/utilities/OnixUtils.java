package testdatagen.utilities;

import java.util.Map;

import testdatagen.config.ConfigurationRegistry;

/**
 * Class with static helper methods concerning Onix technologies
 */
public class OnixUtils
{
	/**
	 * A lookup function to return a desciptive String about a certain Onix code list
	 * @param codeListNumber The Onix code list number
	 * @param value The code from the given requested code list
	 * @return A String with a description of the code value.
	 */
	public static String getCodeListDescription(int codeListNumber, String value)
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Map<Integer, Map<String, String>> codeMap = registry.getOnixCodeMap();
		Map<String, String> codeList = codeMap.get(codeListNumber);
		
		return codeList.get(value);
	}
}