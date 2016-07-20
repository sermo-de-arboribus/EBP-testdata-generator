package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * The <Contributor> node of an Onix file represents the information about the author or other 
 * persons who contributed to a book
 */
public class OnixContributorBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] CONTRIBUTOR_ELEMENT_DEFINITIONS = 
		{
		/* 0 */	{"contributor", "Contributor", "contributor", "Contributor", "", ""},
		/* 1 */	{"b034", "SequenceNumber", "b034", "SequenceNumber", "sequencenumber", "1"},
		/* 2 */	{"b035", "ContributorRole", "b035", "ContributorRole", "contributorrole", "A01"},
		/* 3 */	{"nameidentifier", "NameIdentifier", "personnameidentifier", "PersonNameIdentifier", "", ""},
		/* 4 */	{"x415", "NameIDType", "b390", "PersonNameIDType", "nameidtype", "16"},
		/* 5 */	{"b233", "IDTypeName", "b233", "IDTypeName", "idtypename", "ISNI"},
		/* 6 */	{"b244", "IDValue", "b244", "IDValue", "nameidvalue", "12345"},
		/* 7 */	{"b036", "PersonName", "b036", "PersonName", "fullname", ""},
		/* 8 */	{"b037", "PersonNameInverted", "b037", "PersonNameInverted", "invertedname", ""},
		/* 9 */	{"b039", "NamesBeforeKey", "b039", "NamesBeforeKey", "namesbeforekey", ""},
		/*10 */	{"b040", "KeyNames", "b040", "KeyNames", "keynames", ""},
		/*11 */	{"b047", "CorporateName", "b047", "CorporateName", "corporatename", "No corporate name"},
		/*12 */	{"contributordate", "ContributorDate", "persondate", "PersonDate", "", ""},
		/*13 */	{"x417", "ContributorDateRole", "b305", "PersonDateRole", "contributordaterole", "50"},
		/*14 */	{"j260", "DateFormat", "j260", "DateFormat", "persondateformat", "00"},
		/*15 */	{"b306", "Date", "b306", "Date", "birthdate", "{$randomDate}"},
		/*16 */	{"b306", "Date", "b306", "Date", "deathdate", "{$randomDate}"},
		/*17 */	{"professionalaffiliation", "ProfessionalAffiliation", "professionalaffiliation", "ProfessionalAffiliation", "professionalaffiliation", ""},
		/*18 */	{"b045", "ProfessionalPosition", "b045", "ProfessionalPosition", "professionalposition", "Managing director"},
		/*19 */	{"b046", "Affiliation", "b046", "Affiliation", "affiliation", "Koch, Neff & Oetinger GmbH"},
		/*20 */	{"b044", "BiographicalNote", "b044", "BiographicalNote", "biographicalnote", ""},
		/*21 */	{"website", "Website", "website", "Website", "", ""},		
		/*22 */	{"b295", "WebsiteLink", "b295", "WebsiteLink", "websitelink", "http://www.kno-va.de"}
		};
	private static final int SEQUENCE_NUMBER = 1000;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixContributorBuilder (final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = CONTRIBUTOR_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element contributorNode = new Element(getTagName(0));

		// Onix 2.1 and Onix 3.0 have different element sequences and nesting, so we need to distinguish
		// between the formats here. Within each format we also need to distinguish between personal
		// contributors and corporate contributors.
		if(onixVersion.equals("3.0"))
		{
			// append b034 & b035
			appendElement(1, contributorNode);
			appendElement(2, contributorNode);
			
			// add a nameidentifier composite
			Element nameIdentifier = getNameIdentifier();
			contributorNode.appendChild(nameIdentifier);
			
			if(hasArgument("corporatename")) // we're creating a contributor node for a corporation
			{
				addCorporateNameElements(contributorNode);
			}
			else // we're creating a contributor node for a person
			{
				// add main name elements
				addMainNameElements(contributorNode);

				// add contributordates only if arguments are defined
				if(hasArgument("birthdate"))
				{
					Element contributorDate = getBirthDateComposite();
					contributorNode.appendChild(contributorDate);
				}
				
				if(hasArgument("deathdate"))
				{
					Element contributorDate = getDeathDateComposite();
					contributorNode.appendChild(contributorDate);
				}
			}
			// add professionalaffiliation only if argument professionalaffiliation is defined
			if(hasArgument("professionalaffiliation"))
			{
				Element professionalAffiliation = getProfessionalAffiliationComposite();
				contributorNode.appendChild(professionalAffiliation);
			}
			
			// add biographical note
			appendElement(20, contributorNode);
			
			// add website node
			if(hasArgument("websitelink"))
			{
				Element website = new Element(getTagName(21));
				appendElement(22, website);
				contributorNode.appendChild(website);	
			}
		}
		else // build Onix 2.1 contributor node here
		{
			// append b034 & b035
			appendElement(1, contributorNode);
			appendElement(2, contributorNode);
			
			if(hasArgument("corporatename"))
			{
				addCorporateNameElements(contributorNode);
			}
			else
			{
				addMainNameElements(contributorNode);
				
				// add a person name identifier composite
				Element nameIdentifier = getNameIdentifier();
				contributorNode.appendChild(nameIdentifier);
				
				// add contributordates only if arguments are defined
				if(hasArgument("birthdate"))
				{
					Element contributorDate = getBirthDateComposite();
					contributorNode.appendChild(contributorDate);
				}
				
				if(hasArgument("deathdate"))
				{
					Element contributorDate = getDeathDateComposite();
					contributorNode.appendChild(contributorDate);
				}
				
				// add professionalaffiliation only if argument professionalaffiliation is defined
				if(hasArgument("professionalaffiliation"))
				{
					Element professionalAffiliation = getProfessionalAffiliationComposite();
					contributorNode.appendChild(professionalAffiliation);
				}
			}
			
			// add biographical note
			appendElement(20, contributorNode);
			
			// add website node
			if(hasArgument("websitelink"))
			{
				Element website = new Element(getTagName(21));
				appendElement(22, website);
				contributorNode.appendChild(website);	
			}
		}
		
		return contributorNode;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}

	private void addCorporateNameElements(Element parentNode)
	{
		appendElement(11, parentNode);
	}
	
	private void addMainNameElements(Element parentNode)
	{
		appendElement(7, parentNode);
		appendElement(8, parentNode);
		appendElement(9, parentNode);
		appendElement(10, parentNode);
	}
	
	// helper method for appending next required element
	private void appendElement(int index, Element appendToNode)
	{
		Element nextElement = new Element(getTagName(index));
		nextElement.appendChild(new Text(determineElementContent(index)));
		appendToNode.appendChild(nextElement);
	}
	
	private Element getBirthDateComposite()
	{
		Element contributorDate = new Element(getTagName(12));
		String roleCode;
		if(onixVersion.equals("2.1"))
		{
			roleCode = "007";
		}
		else
		{
			roleCode = "50";
		}
		arguments.put("contributordaterole", roleCode);
		appendElement(13, contributorDate);
		appendElement(14, contributorDate);
		appendElement(15, contributorDate);
		arguments.remove("contributordaterole");
		return contributorDate;
	}
	
	private Element getDeathDateComposite()
	{
		Element contributorDate = new Element(getTagName(12));
		String roleCode;
		if(onixVersion.equals("2.1"))
		{
			roleCode = "008";
		}
		else
		{
			roleCode = "51";
		}
		arguments.put("contributordaterole", roleCode);
		appendElement(13, contributorDate);
		appendElement(14, contributorDate);
		appendElement(16, contributorDate);
		arguments.remove("contributordaterole");
		return contributorDate;
	}
	
	private Element getNameIdentifier()
	{
		Element nameIdentifier = new Element(getTagName(3));
		appendElement(4, nameIdentifier);
		appendElement(5, nameIdentifier);
		appendElement(6, nameIdentifier);
		return nameIdentifier;
	}
	
	private Element getProfessionalAffiliationComposite()
	{
		Element professionalAffiliation = new Element(getTagName(17));
		appendElement(18, professionalAffiliation);
		appendElement(19, professionalAffiliation);
		return professionalAffiliation;
	}
}