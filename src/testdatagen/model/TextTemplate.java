package testdatagen.model;

import java.util.Locale;
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
	
	protected void replaceVars(Title title, StringBuffer resultBuffer, String unprocessedText)
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Random random = new Random();
		if(unprocessedText.contains("{$"))
		{
			int varStart = unprocessedText.indexOf("{$");
			int varEnd = unprocessedText.indexOf("}");
			
			resultBuffer.append(unprocessedText.substring(0, varStart));
			String variable = unprocessedText.substring(varStart + 2, varEnd);
			
			if(variable.equals("authorname"))
			{
				resultBuffer.append(title.getAuthor());
			}
			else if(variable.equals("birthyear"))
			{
				int year = random.nextInt(80) + 1930;
				resultBuffer.append(year);
			}
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
			replaceVars(title, resultBuffer, unprocessedText.substring(varEnd + 1));
		}
		else
		{
			resultBuffer.append(unprocessedText);
		}
	}
}