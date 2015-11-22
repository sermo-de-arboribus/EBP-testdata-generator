package testdatagen.templates;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import nu.xom.*;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.utilities.Utilities;

public class EBookChapterTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	int chapterNumber;
	
	public EBookChapterTemplate(Locale loc, int chapterNumber)
	{
		super(loc);
		registry = ConfigurationRegistry.getRegistry();
		templateText = registry.getLocalizedText(loc, "chapterTemplate")[0];
		this.chapterNumber = chapterNumber;
	}
	
	@Override
	public String fillWithText()
	{
		Random random = new Random();
		StringBuffer templateBuffer = new StringBuffer();
		HashMap<String, String> predefinedValues = new HashMap<>();
		predefinedValues.put("chapternumber","" + chapterNumber);
		
		// open, read and parse XML document with quotes
		Builder XmlBuilder = new Builder();
		Document quotesDoc = null;
		try
		{
			URL quotesDocUrl = getClass().getClassLoader().getResource("testdatagen/config/chapterSentences.xml");
			InputStream inputStream = quotesDocUrl.openStream();
			quotesDoc = XmlBuilder.build(inputStream);
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Could not open 'chapterSentences.xml'", exc);
		}
		catch (ParsingException exc)
		{
			Utilities.showErrorPane("The file 'chapterSentences.xml' is not well-formed", exc);
		}
		String language = locale.getLanguage();

		Nodes allQuotes = quotesDoc.query("/quotes/quote[version[starts-with(@xml:lang,'" + language + "')]]");

		StringBuffer chaptertextBuffer = new StringBuffer();
		int runs = random.nextInt(10) + 1;
		for(int i = 0; i < runs; i++)
		{
			System.out.println("Run no. " + i);
			Node quote = allQuotes.get(random.nextInt(allQuotes.size()));
			Nodes quoteTexts = quote.query("version[starts-with(@xml:lang,'" + language + "')]/text");
			Node quoteText = quoteTexts.get(0);
			System.out.println("Quote text: " + quoteText.getValue());
			chaptertextBuffer.append(quoteText.getValue() + " ");
		}
		predefinedValues.put("chaptertext", chaptertextBuffer.toString());
		
		replaceVars(templateBuffer, templateText, predefinedValues);
		return templateBuffer.toString();
	}

}