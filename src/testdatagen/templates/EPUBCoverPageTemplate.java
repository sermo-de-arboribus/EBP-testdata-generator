package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;

public class EPUBCoverPageTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	String coverPath;
	
	public EPUBCoverPageTemplate(Locale loc, String coverPath)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		templateText = registry.getLocalizedText(loc, "EPUBCoverPageTemplate")[0];
		this.coverPath = coverPath;
	}
	
	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		
		HashMap<String, String> predefinedValues = new HashMap<>();
		predefinedValues.put("coverjpgpath", "" + coverPath);
		
		replaceVars(sb, templateText, predefinedValues);
		return sb.toString();
	}
}
