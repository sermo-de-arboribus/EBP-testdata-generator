package testdatagen.model;

import java.util.Locale;

public abstract class TextTemplate
{
	protected Locale locale;
	protected String templateText;
	
	TextTemplate(Locale loc)
	{
		locale = loc;
	}
	
	public abstract String fillWithText();
}