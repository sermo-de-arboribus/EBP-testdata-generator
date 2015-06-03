package testdatagen.model;

import java.util.Locale;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;

public class AuthorBlurbTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	Title title;
	
	public AuthorBlurbTemplate(Locale loc, Title title)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		templateText = registry.getLocalizedText(loc, "authorBlurbTemplate")[0];
		this.title = title;
	}
	
	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		replaceVars(title, sb, templateText);
		return sb.toString();
	}
	
}