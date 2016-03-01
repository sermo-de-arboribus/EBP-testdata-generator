package testdatagen.templates;

import java.util.HashMap;
import java.util.Locale;

import testdatagen.config.ConfigurationRegistry;

/**
 * This template returns an HTML string representing an HTML cover page to be embededed in an e-book 
 * file in EPUB format.
 */
public class EPUBCoverPageTemplate extends TextTemplate
{
	private ConfigurationRegistry registry;
	private String coverPath;
	
	/**
	 * Constructor
	 * @param loc Locale to be used with this template
	 * @param coverPath The path to the cover image file
	 */
	public EPUBCoverPageTemplate(final Locale loc, final String coverPath)
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
