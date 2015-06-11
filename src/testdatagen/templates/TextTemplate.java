package testdatagen.templates;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.utilities.Utilities;

public abstract class TextTemplate
{
	protected Locale locale;
	protected String templateText;
	
	TextTemplate(Locale loc)
	{
		locale = loc;
	}
	
	public abstract String fillWithText();
	
	protected void replaceVars(StringBuffer resultBuffer, String unprocessedText, Map<String, String> predefinedValues)
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Random random = new Random();
		if(unprocessedText.contains("{$"))
		{
			int varStart = unprocessedText.indexOf("{$");
			int varEnd = unprocessedText.indexOf("}");
			
			resultBuffer.append(unprocessedText.substring(0, varStart));
			String variable = unprocessedText.substring(varStart + 2, varEnd);
			
			// first check if the variable is defined in the predefined values map...
			if(predefinedValues.containsKey(variable))
			{
				resultBuffer.append(predefinedValues.get(variable));
			}
			// ... if not, take the variable value from the configuration registry
			else
			{
				String[] replacementOptions = registry.getLocalizedText(locale, variable);
				if(replacementOptions == null)
				{
					Utilities.showErrorPane("Error creating text from template. Variable " + variable + " missing!", new NullPointerException());
				}
				else
				{
					String replacement = replacementOptions[random.nextInt(replacementOptions.length)];
					resultBuffer.append(replacement);	
				}
			}
			replaceVars(resultBuffer, unprocessedText.substring(varEnd + 1), predefinedValues);
		}
		else
		{
			resultBuffer.append(unprocessedText);
		}
	}
}