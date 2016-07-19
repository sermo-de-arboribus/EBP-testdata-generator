package testdatagen.onixbuilder;

import java.util.HashMap;
import nu.xom.Element;
import nu.xom.Text;

public class OnixEditionStatementBuilder extends OnixPartsBuilder
{
	private static final String[][] EDITION_STATEMENT_DEFINITIONS = 
	{
		{"b058", "EditionStatement", "b058", "EditionStatement", "editionstatement", "Zweite, revidierte Auflage"},
	};
	private static final int SEQUENCE_NUMBER = 1240;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixEditionStatementBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = EDITION_STATEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element editionStatement = new Element(getTagName(0));
		editionStatement.appendChild(new Text(determineElementContent(0)));
		return editionStatement;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}