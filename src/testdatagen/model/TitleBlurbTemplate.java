package testdatagen.model;

import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;

public class TitleBlurbTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	private TitleBlurbTemplateType type; // TODO: make use of the value to create blurb text of different length
	private Title title;
	
	TitleBlurbTemplate(Locale loc, Title title, TitleBlurbTemplateType type)
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
		replaceVars(title, sb, templateText);
		return sb.toString();
	}
}

enum TitleBlurbTemplateType
{
	SHORT, MEDIUM, LONG;
}