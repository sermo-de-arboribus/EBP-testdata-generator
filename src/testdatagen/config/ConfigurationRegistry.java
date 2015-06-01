package testdatagen.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationRegistry
{
	// At this stage of development we just give some configuration values as hard-coded literals here (see constructor)
	// This could be evolved into a more flexible configuration system later.
	
	private static ConfigurationRegistry registry;
	private Map<String, String> regMap;
	
	private ConfigurationRegistry()
	{
		 regMap = new HashMap<String, String>();
		 
		 // add cover generation data to register
		 regMap.put("cover.maxWidth", "2400");
		 regMap.put("cover.minWidth", "300");
		 regMap.put("cover.minXYRatio", "1.28");
		 regMap.put("cover.maxXYRatio", "1.48");
		 regMap.put("cover.likelinessOfPDFCovers", "0.2");
		 regMap.put("file.tempFolder", "temp");
		 regMap.put("ssl.pathToCacerts", "R:\\git\\EBP-testdata-generator\\lib\\cacerts");
		 regMap.put("ssl.pathToCacerts", "R:\\git\\EBP-testdata-generator\\lib\\cacerts");
		 regMap.put("net.useProxy", "yes");
		 regMap.put("net.proxy.host", "proxy.knonet.de");
		 regMap.put("net.proxy.port", "8080");
	}
	
	public static synchronized ConfigurationRegistry getRegistry()
	{
		if(registry == null)
		{
			registry = new ConfigurationRegistry();
		}
		return registry;
	}
	
	public boolean getBooleanValue(String key)
	{
		String value = regMap.get(key);
		if(value == null) return false;
		if(value.equals("yes") | value.equals("y") | value.equals("j") | value.equals("ja")) return true;
		return false;
	}
	
	public double getDoubleValue(String key)
	{
		double value;
		try
		{
			value = Double.parseDouble(regMap.get(key));
		}
		catch(NumberFormatException nfe)
		{
			value = Double.MIN_VALUE;
		}
		return value;
	}
	
	public int getIntValue(String key)
	{
		int value;
		try
		{
			value = Integer.parseInt(regMap.get(key));
		}
		catch(NumberFormatException nfe)
		{
			value = Integer.MIN_VALUE;
		}
		return value;
	}
	
	public String getString(String key)
	{
		return regMap.get(key);
	}
}