package testdatagen.onixbuilder;

import nu.xom.Attribute;
import nu.xom.Element;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import testdatagen.model.Subject;
import testdatagen.model.Title;
import testdatagen.utilities.TitleUtils;

public class OnixPartsDirector
{
	// constants defined for ONIX tag types
	public static final int SHORTTAG = 1;
	public static final int REFERENCETAG = 2;
	
	private LinkedList<OnixPartsBuilder> requiredElements;
	private Title title;
	/*
	 * The OnixPartsDirector class maintains a pattern for an ONIX file to be generated. 
	 * When an object of the OnixPartsDirector class is instantiated, a default pattern for an ONIX file
	 * is created. After creation of the default file, the default settings can be modified by
	 * calling specific methods on the OnixPartsDirector object. 
	 */
	public OnixPartsDirector(Title title)
	{
		this.title = title;
		
		requiredElements = new LinkedList<OnixPartsBuilder>();
		
		// add default header
		requiredElements.add(new OnixHeaderBuilder(new HashMap<String, String>()));
		
		// add record reference
		requiredElements.add(new OnixRecordReferenceBuilder(new HashMap<String, String>()));
		
		// add notification Type
		requiredElements.add(new OnixNotificationTypeBuilder(new HashMap<String, String>()));
		
		// add product composition
		requiredElements.add(new OnixProductCompositionBuilder(new HashMap<String, String>()));
		
		// add default product Identifier (type 15: ISBN-13)
		addProductIdentifier("15");
		
		// add product form
		requiredElements.add(new OnixProductFormBuilder(new HashMap<String, String>()));
		
		// add product form detail
		addProductFormDetail(title.getEpubTypeForProductFormDetail());

		// add protection type
		HashMap<String, String> protectionArgs = new HashMap<>();
		protectionArgs.put("technicalprotection", title.getProtectionTypeForONIX());
		requiredElements.add(new OnixTechnicalProtectionBuilder(protectionArgs));
		
		// TODO: implement Series / Collection 
		
		// add a title
		addTitleWithSubtitle("01");
		
		// add (first / main) author
		HashMap<String, String> authorArgs = new HashMap<>();
		authorArgs.put("fullname", title.getAuthor());
		authorArgs.put("invertedname", title.getAuthorLastName() + ", " + title.getAuthorFirstName());
		authorArgs.put("namesbeforekey", title.getAuthorFirstName());
		authorArgs.put("keynames", title.getAuthorLastName());
		authorArgs.put("biographicalnote", title.getAuthorBlurb());
		authorArgs.put("birthdate", "19440704");
		authorArgs.put("deathdate", "20131228");
		requiredElements.add(new OnixContributorBuilder(authorArgs));
		
		// TODO: add a corporate contributor? Add the option of adding further authors?
		// TODO: do we want to implement EditionNumber / EditionStatement?
		
		// add a default language node
		requiredElements.add(new OnixLanguageBuilder(new HashMap<String, String>()));
		
		// add a default extent element
		requiredElements.add(new OnixExtentBuilder (new HashMap<String, String>()));
		
		// add main subject element
		HashMap<String, String> mainSubjectArgs = new HashMap<String, String>();
		mainSubjectArgs.put("subjectcode", TitleUtils.getRandomWarengruppeCode());
		mainSubjectArgs.put("subjectheadingtext", TitleUtils.getRandomTopic(new Locale("de")));
		requiredElements.add(new OnixSubjectBuilder(OnixSubjectBuilder.SUBJECTTYPE_MAIN, mainSubjectArgs));
		
		// add "normal" subject element
		HashMap<String, String> subjectArgs = new HashMap<String, String>();
		subjectArgs.put("subjectheadingtext", TitleUtils.getRandomTopic(new Locale("de")));
		requiredElements.add(new OnixSubjectBuilder(subjectArgs));
		
		
	}
	
	public void addProductFormDetail(String code)
	{
		HashMap<String, String> productFormDetailArgs = new HashMap<>();
		productFormDetailArgs.put("productformdetail", code);
		requiredElements.add(new OnixProductFormDetailBuilder(productFormDetailArgs));
	}
	
	public void addProductIdentifier(String type)
	{
		String productIdValue;
		switch(type)
		{
			case "06": // DOI
				productIdValue = "10.2379/" + title.getIsbn13();
				break;
			case "22": // URN
				productIdValue = "urn:isbn:" + title.getIsbn13();
				break;
			default:
				productIdValue = Long.toString(title.getIsbn13());
		}
		
		HashMap<String, String> productIdArgs = new HashMap<>();
		productIdArgs.put("productidtype", type);
		productIdArgs.put("productidvalue", productIdValue);
		requiredElements.add(new OnixProductIdentifierBuilder(productIdArgs));
	}
	
	public void addSubject(Subject newSubject)
	{
		HashMap<String, String> subjectArgs = new HashMap<String, String>();
		subjectArgs.put("subjectcode", newSubject.getSubjectcode());
		subjectArgs.put("subjectheadingtext", newSubject.getSubjectheadingtext());
		subjectArgs.put("subjectschemeidentifier", newSubject.getSubjectschemeidentifier());
		subjectArgs.put("subjectheadingtext", newSubject.getSubjectheadingtext());
		if(newSubject.isMainSubject())
		{
			subjectArgs.put("mainsubjectschemeidentifier", newSubject.getSubjectschemeidentifier());
			requiredElements.add(new OnixSubjectBuilder(OnixSubjectBuilder.SUBJECTTYPE_MAIN, subjectArgs));
		}
		else
		{
			requiredElements.add(new OnixSubjectBuilder(OnixSubjectBuilder.SUBJECTTYPE_NORMAL, subjectArgs));
		}
		
	}
	
	public void addTitle(String titleType)
	{
		HashMap<String, String> titleArgs = new HashMap<>();
		titleArgs.put("titletext", determineTitleStringByType("01", title));
		OnixTitleBuilder otb = new OnixTitleBuilder(titleArgs);
		requiredElements.add(otb); 
	}
	
	public void addTitleWithSubtitle(String titleType)
	{
		HashMap<String, String> titleArgs = new HashMap<>();
		titleArgs.put("titletext", determineTitleStringByType("01", title));
		titleArgs.put("subtitle", determineTitleStringByType("subtitle", title));
		OnixTitleBuilder otb = new OnixTitleBuilder(titleArgs);
		requiredElements.add(otb); 
	}
	
	public Element buildOnix2(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		root.addAttribute(new Attribute("release", "2.1"));

		// TODO: build the header and product elements and append them to the root
		// Note that the ProductForm must be set to 'DG' (or 'AJ')
		// Also note that we probably need an Extent builder to give us a b061 / NumberOfPages element 
		
		return root;
	}
	
	public Element buildOnix3(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		root.addAttribute(new Attribute("release", "3.0"));
		
		// TODO: build the header and product elements and append them to the root
		// Note that the ProductForm must be set to 'EA' / 'DG' (or maybe 'AJ' later) 
		
		return root;
	}
	
	/*
	 * Private helper method; returns the name of the ONIX file's root element based on the tag type (short tag / reference tag)
	 */
	private String getRootName(int tagType)
	{
		String rootElementName;
		switch(tagType)
		{
			case SHORTTAG:
				rootElementName = "ONIXmessage";
			case REFERENCETAG:
				rootElementName = "ONIXMessage";
			default:
				rootElementName = "ONIXmessage";
		}
		return rootElementName;
	}
	
	private String determineTitleStringByType(String titleType, Title title)
	{
		// split titleString into main title and subtitle, if titleString contains a full-stop.
		String titleString = title.getName();
		String mainTitle = "";
		String subTitle = "";
		String abbrevTitle = "";
		if(titleString.contains("."))
		{
			mainTitle = titleString.substring(0, titleString.lastIndexOf('.'));
			subTitle = titleString.substring(titleString.lastIndexOf('.') + 1).trim();
		}
		else 
		{
			mainTitle = titleString;
		}
		// if titleType is "05", this is meant to be a shortened title
		if(titleType.equals("05"))
		{
			abbrevTitle = mainTitle;
			if(mainTitle.length() > 12)
			{
				abbrevTitle = mainTitle.substring(0, 10);
			}
		}
		if(subTitle.equals(""))
		{
			subTitle = "Kein Untertitel";
		}
		switch(titleType)
		{
			case "subtitle": return subTitle;
			case "01":
			case "title":
			case "distinctivetitle": return mainTitle;
			case "05":
			case "abbreviatedtitle": return abbrevTitle;
			default: return "";
		}
	}
}