package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;

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
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		HashMap<String, String> predefinedValues = new HashMap<>();
		predefinedValues.put("authorname", title.getAuthor());
		int year = random.nextInt(80) + 1930;
		predefinedValues.put("birthyear", "" + year);
		
		replaceVars(sb, templateText, predefinedValues);
		return sb.toString();
	}
}