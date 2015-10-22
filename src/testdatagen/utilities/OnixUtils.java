package testdatagen.utilities;

import java.util.Map;

import testdatagen.config.ConfigurationRegistry;

public class OnixUtils
{
	public static String getCodeListDescription(int codeListNumber, String value)
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Map<Integer, Map<String, String>> codeMap = registry.getOnixCodeMap();
		Map<String, String> codeList = codeMap.get(codeListNumber);
		
		return codeList.get(value);
	}
}