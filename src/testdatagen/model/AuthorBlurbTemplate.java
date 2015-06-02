package testdatagen.model;

import java.util.Locale;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;

public class AuthorBlurbTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	Random random;
	Title title;
	
	public AuthorBlurbTemplate(Locale loc, Title title)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		random = new Random();
		templateText = registry.getLocalizedText(loc, "authorBlurbTemplate")[0];
		this.title = title;
	}
	
	@Override
	public String fillWithText()
	{
		StringBuffer sb = new StringBuffer();
		replaceVars(sb, templateText);
		return sb.toString();
	}
	
	private void replaceVars(StringBuffer resultBuffer, String unprocessedText)
	{
		if(unprocessedText.contains("{$"))
		{
			int varStart = unprocessedText.indexOf("{$");
			int varEnd = unprocessedText.indexOf("}");
			System.out.println("varStart = " + varStart + ", varEnd = " + varEnd);
			
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
				String replacement = replacementOptions[random.nextInt(replacementOptions.length)];
				resultBuffer.append(replacement);
			}
			replaceVars(resultBuffer, unprocessedText.substring(varEnd + 1));	
		}
		else
		{
			resultBuffer.append(unprocessedText);
		}
	}
}