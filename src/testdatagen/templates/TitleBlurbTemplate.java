package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;

public class TitleBlurbTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	
	public TitleBlurbTemplate(Locale loc, TitleBlurbTemplateType type)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		switch(type)
		{
		case LONG:
			templateText = registry.getLocalizedText(loc, "longTitleBlurbTemplate")[0];
			break;
		case MEDIUM:
			templateText = registry.getLocalizedText(loc, "mediumTitleBlurbTemplate")[0];
			break;
		case SHORT:
		default:
			templateText = registry.getLocalizedText(loc, "titleBlurbTemplate")[0];
		}
		
	}

	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		replaceVars(sb, templateText, new HashMap<String, String>());
		return sb.toString();
	}
}