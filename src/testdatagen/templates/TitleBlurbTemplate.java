package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;

/**
 * The TitleBlurbTemplate produces promotional texts about e-book products in three configurable lengths.
 */
public class TitleBlurbTemplate extends TextTemplate
{
	private ConfigurationRegistry registry;
	
	/**
	 * Constructor
	 * @param loc The Locale to be used for the template
	 * @param type The TitleBlurbTeplateType (LONG / MEDIUM / SHORT)
	 */
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