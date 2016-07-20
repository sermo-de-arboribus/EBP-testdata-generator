package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;

public class SeriesTemplate extends TextTemplate
{
	private ConfigurationRegistry registry;
	
	/**
	 * Constructor
	 * @param loc The Locale to be used for the template
	 */
	public SeriesTemplate(Locale loc)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		templateText = registry.getLocalizedText(loc, "seriesTemplate")[0];
	}
	
	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		replaceVars(sb, templateText, new HashMap<String, String>());
		return sb.toString();
	}
}