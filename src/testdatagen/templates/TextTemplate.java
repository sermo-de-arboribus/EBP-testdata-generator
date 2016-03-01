package testdatagen.templates;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.utilities.Utilities;

/**
 * This abstract class is the base class for all concrete templates in the testdatagen.templates package.
 * The base templates are to be found in the configuration registry. They consist of literal text with variables 
 * in curly brackets, like this: 
 * "Dies ist ein Template mit {$Variable}."
 */
public abstract class TextTemplate
{
	protected Locale locale;
	protected String templateText;
	
	/**
	 * Constructor
	 * @param loc The Locale to be used with this template
	 */
	TextTemplate(final Locale loc)
	{
		locale = loc;
	}
	
	/**
	 * This method takes the template, runs through the variable replacement and then returns the 
	 * finished text as a String.
	 * @return A String of the text with variables replaced by actual text
	 */
	public abstract String fillWithText();
	
	/**
	 * Recursive scanner of template text which finds all variables and replaces them based on the parameters
	 * @param resultBuffer A buffer holding the temporary result, i.e. the text processed recursively so far
	 * @param unprocessedText A String of the template text that has not been processed yet
	 * @param predefinedValues a key-value map of arguments that can be used for the template variables
	 */
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