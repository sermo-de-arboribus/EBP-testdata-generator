package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixSubjectBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] SUBJECT_ELEMENT_DEFINITIONS = 
		{
			// ONIX 2.1 only MainSubject 
			{"", "", "mainsubject", "MainSubject", "", ""},
			{"", "", "b191", "MainSubjectSchemeIdentifier", "mainsubjectschemeidentifier", "26"},
			// general and ONIX 3.0 subject elements
			{"subject", "Subject", "subject", "Subject", "", ""},
			{"x425", "MainSubject", "", "", "", ""},
			{"b067", "SubjectSchemeIdentifier", "b067", "SubjectSchemeIdentifier", "subjectschemeidentifier", "26"},
			{"b068", "SubjectSchemeVersion", "b068", "SubjectSchemeVersion", "subjectschemeversion", "2.0"},
			{"b069", "SubjectCode", "b069", "SubjectCode", "subjectcode", "9120"},
			{"b070", "SubjectHeadingText", "b070", "SubjectHeadingText", "subjectheadingtext", " "},
		};
	private static final int SEQUENCE_NUMBER = 1500;

	// constants defined for ONIX tag types
	public static final int SUBJECTTYPE_MAIN = 1;
	public static final int SUBJECTTYPE_NORMAL = 2;
	
	private int subjectType;
	
	public OnixSubjectBuilder(int subjectType, HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = SUBJECT_ELEMENT_DEFINITIONS;
		this.subjectType = subjectType;
	}
	
	public OnixSubjectBuilder(HashMap<String, String> args)
	{
		this(SUBJECTTYPE_NORMAL, args);
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element subject;
		Element subjectIdentifier;
		
		if(subjectType == SUBJECTTYPE_MAIN && onixVersion.equals("2.1"))
		{
			subject = new Element(getTagName(0));
			subjectIdentifier = new Element(getTagName(1));
			subjectIdentifier.appendChild(new Text(determineElementContent(1)));
		}
		else
		{
			subject = new Element(getTagName(2));
			if(subjectType == SUBJECTTYPE_MAIN && onixVersion.equals("3.0"))
			{
				subject.appendChild(new Element(getTagName(3)));
			}
			subjectIdentifier = new Element(getTagName(4));
			subjectIdentifier.appendChild(new Text(determineElementContent(4)));
		}
		subject.appendChild(subjectIdentifier);
		
		for (int i = 5; i < 8; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			subject.appendChild(nextElement);	
		}
		
		return subject;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER + subjectType;
	}
}
