package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixIllustrationsNoteBuilder extends OnixPartsBuilder
{
	private static final String[][] ILLUSTRATIONS_NOTE_DEFINITIONS = 
	{
		{"b062", "IllustrationsNote", "b062", "IllustrationsNote", "illustrationsnote", "64 Abbildungen, darunter zahlreiche Kupferstiche, Radierungen, Gouachen und Lithographien"},
	};
	private static final int SEQUENCE_NUMBER = 1450;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixIllustrationsNoteBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = ILLUSTRATIONS_NOTE_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element illustrationStatement = new Element(getTagName(0));
		illustrationStatement.appendChild(new Text(determineElementContent(0)));
		return illustrationStatement;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}