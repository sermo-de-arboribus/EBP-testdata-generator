package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;

public class TitleBlurbTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	private TitleBlurbTemplateType type; // TODO: make use of the value to create blurb text of different length
	private Title title;
	
	public TitleBlurbTemplate(Locale loc, Title title, TitleBlurbTemplateType type)
	{
		super(loc);
		this.title = title;
		this.type = type;
		registry = ConfigurationRegistry.getRegistry();
		templateText = registry.getLocalizedText(loc, "titleBlurbTemplate")[0];
	}

	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		replaceVars(sb, templateText, new HashMap<String, String>());
		return sb.toString();
	}
}