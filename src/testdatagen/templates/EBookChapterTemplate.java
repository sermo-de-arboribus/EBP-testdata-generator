package testdatagen.templates;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import nu.xom.*;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.utilities.Utilities;

/**
 * This template class generates e-book chapter content strings. The template is expected to contain 
 * HTML tags within its text, which is useful for e-books in EPUB format, which uses HTML internally.
 * However, the class also offers a fillWithPlainText() method to strip the HTML tags.
 */
public class EBookChapterTemplate extends TextTemplate
{
	ConfigurationRegistry registry;
	int chapterNumber;

	/**
	 * Constructor
	 * @param loc Locale object for the template
	 * @param chapterNumber Number of the chapter to be generated
	 */
	public EBookChapterTemplate(final Locale loc, final int chapterNumber)
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
			Node quote = allQuotes.get(random.nextInt(allQuotes.size()));
			Nodes quoteTexts = quote.query("version[starts-with(@xml:lang,'" + language + "')]/text");
			Node quoteText = quoteTexts.get(0);
			chaptertextBuffer.append(quoteText.getValue() + " ");
		}
		predefinedValues.put("chaptertext", chaptertextBuffer.toString());
		
		replaceVars(templateBuffer, templateText, predefinedValues);
		return templateBuffer.toString();
	}
	
	/**
	 * Returns a chapter text without HTML text.
	 * @return chapter text as string without HTML tags.
	 */
	public String fillWithPlainText()
	{
		String htmlText = this.fillWithText();
		htmlText = htmlText.replaceFirst("\\<head>.+\\</head>", "");
		return htmlText.replaceAll("\\<[^>]*>","").trim();
	}
}