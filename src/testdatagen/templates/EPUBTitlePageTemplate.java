package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;

public class EPUBTitlePageTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	Title title;
	
	public EPUBTitlePageTemplate(Locale loc, Title title)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		templateText = registry.getLocalizedText(loc, "ONIXTitlePageTemplate")[0];
		this.title = title;
	}
	
	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		
		HashMap<String, String> predefinedValues = new HashMap<>();
		predefinedValues.put("title", title.getName());
		predefinedValues.put("author", title.getAuthor());
		predefinedValues.put("year", "2015");
		predefinedValues.put("isbn", "" + title.getIsbn13());
		
		replaceVars(sb, templateText, predefinedValues);
		return sb.toString();
	}
}
