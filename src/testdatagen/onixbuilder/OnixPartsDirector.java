package testdatagen.onixbuilder;

import nu.xom.Attribute;
import nu.xom.Element;

import java.io.Serializable;
import java.util.*;

import testdatagen.model.*;
import testdatagen.utilities.*;

/**
 * The Onix parts director holds a list of required Onix elements for a certain product. The constructor
 * sets up an Onix file with default values. The OnixPartsDirector also offers methods to add, change or 
 * remove certain elements. The buildOnix2() and buildOnix3() methods order the OnixPartsBuilder objects
 * in Onix document sequence and then call each builder's build method. The returned XML elements are
 * then added to the XML DOM.
 */
public class OnixPartsDirector implements Serializable
{
	private static final long serialVersionUID = 2L;
	
	private LinkedList<OnixPartsBuilder> requiredElements;
	private Title title;
	
	/**
	 * Constructor
	 * The OnixPartsDirector class maintains a pattern for an ONIX file to be generated. 
	 * When an object of the OnixPartsDirector class is instantiated, a default pattern for an ONIX file
	 * is created. After creation of the default file, the default settings can be modified by
	 * calling specific methods on the OnixPartsDirector object.
	 * @title The product Title object that this Onix file belongs to. 
	 */
	public OnixPartsDirector(final Title title)
	{
		this();
		
		this.title = title;
		Random random = new Random();
		
		// add default header
		requiredElements.add(new OnixHeaderBuilder(new HashMap<String, String>()));
		
		// add record reference
		HashMap<String, String> recRefArgs = new HashMap<String, String>();
		recRefArgs.put("recordreference", "test-" + title.getIsbn13());
		requiredElements.add(new OnixRecordReferenceBuilder(recRefArgs));
		
		// add notification Type
		requiredElements.add(new OnixNotificationTypeBuilder(new HashMap<String, String>()));
		
		// add product composition
		requiredElements.add(new OnixProductCompositionBuilder(new HashMap<String, String>()));
		
		// add default product Identifier (type 15: ISBN-13)
		addProductIdentifier("15");
		
		// product form should be handled by the onix 2.1 / onix 3 build methods
		
		// add product form detail
		addProductFormDetail(title.getEpubTypeForProductFormDetail());

		// add primary content type
		requiredElements.add(new OnixPrimaryContentTypeBuilder(new HashMap<String, String>()));
		
		// add protection type
		HashMap<String, String> protectionArgs = new HashMap<>();
		protectionArgs.put("technicalprotection", title.getProtectionTypeForONIX());
		requiredElements.add(new OnixTechnicalProtectionBuilder(protectionArgs));
		
		// Series / Collection handled by addCollection() , if checkbox in GUI selected
		
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
		
		// add number of pages (in ONIX 2.1)
		HashMap<String, String> extentArgs = new HashMap<String, String>();
		extentArgs.put("numberofpages", "320");
		requiredElements.add(new OnixExtentBuilder(extentArgs));
		
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
		
		// add descriptive / advertising text content
		HashMap<String, String> textArgs = new HashMap<String, String>();
		textArgs.put("texttypecode", "03");
		textArgs.put("contentaudience", "00");
		textArgs.put("text", title.getLongBlurb());
		requiredElements.add(new OnixTextBuilder(textArgs));

		// add descriptive / advertising text content
		textArgs = new HashMap<String, String>();
		textArgs.put("texttypecode", "02");
		textArgs.put("contentaudience", "00");
		textArgs.put("text", title.getShortBlurb());
		requiredElements.add(new OnixTextBuilder(textArgs));
		
		// we don't need to set a default media resource, as the title's setMediaFileUrl() method is handling this
		
		// add an imprint node
		requiredElements.add(new OnixImprintBuilder(new HashMap<String,String>()));
		
		// add publisher information
		requiredElements.add(new OnixPublisherBuilder(new HashMap<String, String>()));
		
		// add place and country of publication
		requiredElements.add(new OnixCityOfPublicationBuilder(new HashMap<String, String>()));
		requiredElements.add(new OnixCountryOfPublicationBuilder(new HashMap<String, String>()));
		
		// add publishing status
		requiredElements.add(new OnixPublishingStatusBuilder(new HashMap<String, String>()));
		
		// add publication date
		HashMap<String, String> pubDateArgs = new HashMap<String, String>();
		long currentDateMillis = new Date().getTime();
		long randomPubDate = random.nextInt(10) * 86400000L;	
		randomPubDate = random.nextBoolean() ? randomPubDate : randomPubDate * -1;
		pubDateArgs.put("publishingdate", Utilities.getDateForONIX2(new Date(currentDateMillis + randomPubDate)).substring(0, 8));
		requiredElements.add(new OnixPublishingDateBuilder(pubDateArgs));
		
		// add sales rights
		HashMap<String, String> salesRightsArgs = new HashMap<String, String>();

		// build a random country list
		StringBuffer countryList = new StringBuffer("");
		int j = random.nextInt(6) + 1;
		for(int i = 0; i < j; i++)
		{
			countryList.append(Utilities.getCountryForONIX());
			if(i < j - 1)
			{		// Also note that we probably need an Extent builder to give us a b061 / NumberOfPages element
				countryList.append(" ");
			}
		}
		salesRightsArgs.put("salesrightstype", "0" + (random.nextInt(1) + 1));
		salesRightsArgs.put("countriesincluded", countryList.toString());
		requiredElements.add(new OnixSalesRightsBuilder(salesRightsArgs));
		
		// add some related product
		HashMap<String, String> relatedProductArgs = new HashMap<String, String>();
		// determine the ISBN to be used for a related product
		String ISBNString = Long.toString(title.getIsbn13());
		ISBNString = ISBNString.substring(0, 6) + "5" + ISBNString.substring(7, 12);
		String checkDigit = ISBNUtils.calculateCheckDigit(ISBNString);
		ISBNString = ISBNString + checkDigit;
		long relISBN = Long.parseLong(ISBNString);
		relatedProductArgs.put("productidvalue", Long.toString(relISBN));
		relatedProductArgs.put("productform", "BC");
		requiredElements.add(new OnixRelatedProductBuilder(relatedProductArgs));
		
		// add supply detail
		requiredElements.add(new OnixSupplierBuilder(new HashMap<String, String>()));
		
		// add product availability node
		requiredElements.add(new OnixProductAvailabilityBuilder(new HashMap<String, String>()));
		
		// add an expected ship date
		HashMap<String, String> expectedShipDateArgs = new HashMap<String, String>();
		// calculate a random expected ship date
		// Expected Ship Date, +/- 10 days from today
		currentDateMillis = new Date().getTime();
		long randomShipDate = random.nextInt(10) * 86400000L;
		randomShipDate = random.nextBoolean() ? randomShipDate : randomShipDate * -1;
		Date actualShipDate = new Date(currentDateMillis + randomShipDate);
		String shipDateString = Utilities.getDateForONIX2(actualShipDate).substring(0, 8);
		expectedShipDateArgs.put("supplydaterole", "08");
		expectedShipDateArgs.put("date", shipDateString);
		requiredElements.add(new OnixSupplyDateBuilder(expectedShipDateArgs));
		
		// add an on sale date
		HashMap<String, String> onSaleDateArgs = new HashMap<String, String>();
		long onSaleDate = random.nextInt(10) * 86400000L;
		onSaleDate = random.nextBoolean() ? onSaleDate : onSaleDate * -1;
		Date actualOnSaleDate = new Date(currentDateMillis + onSaleDate);
		shipDateString = Utilities.getDateForONIX2(actualOnSaleDate).substring(0, 8);
		onSaleDateArgs.put("supplydaterole", "02");
		onSaleDateArgs.put("date", shipDateString);
		requiredElements.add(new OnixSupplyDateBuilder(onSaleDateArgs));
		
		// add an 04-type price and a 42-type price for Germany - further prices should become a configuration option
		// determine random base price
		Price basePrice = title.getBasePrice();
		HashMap<String, String> priceArgs = new HashMap<String, String>();
		basePrice.addPriceArguments(priceArgs);
		requiredElements.add(new OnixPriceBuilder(priceArgs));
		
		Price fourtyTwoPrice = new Price("42", "EUR", "DE");
		HashMap<String, String> fourtyTwoPriceArgs = new HashMap<String, String>();
		fourtyTwoPrice.addPriceArguments(fourtyTwoPriceArgs);
		requiredElements.add(new OnixPriceBuilder(fourtyTwoPriceArgs));
	}
	
	// the private, parameter-less constructor is only to be used be the clone() method
	private OnixPartsDirector()
	{
		requiredElements = new LinkedList<OnixPartsBuilder>();
	}
	
	public void addCollection()
	{
		HashMap<String, String> collectionArgs = new HashMap<String, String>();
		collectionArgs.put("titletext", title.getSeriesTitle());
		collectionArgs.put("titlestatement", title.getSeriesTitle());
		requiredElements.add(new OnixCollectionSeriesBuilder(collectionArgs));
	}
	
	/**
	 * Add a media resource, usually a URL for downloading a cover file
	 * @param url The URL where the media resource can be downloaded
	 */
	public void addMediaResource(final String url)
	{
		HashMap<String, String> mediaArgs = new HashMap<String, String>();
		mediaArgs.put("resourcelink", url);
		requiredElements.add(new OnixMediaResourceBuilder(mediaArgs));
	}
	
	/**
	 * Add another <ProductFormDetail> element to the Onix tree
	 * @param code The code to be used inside the <ProductFormDetail> element
	 */
	public void addProductFormDetail(final String code)
	{
		HashMap<String, String> productFormDetailArgs = new HashMap<>();
		productFormDetailArgs.put("productformdetail", code);
		requiredElements.add(new OnixProductFormDetailBuilder(productFormDetailArgs));
	}
	
	/**
	 * Add an additional <Price> node to the Onix XML tree
	 * @param newPrice The Price object that represents the additional price data
	 */
	public void addPrice(final Price newPrice)
	{
		HashMap<String, String> newPriceArgs = new HashMap<>();
		newPrice.addPriceArguments(newPriceArgs);
		requiredElements.add(new OnixPriceBuilder(newPriceArgs));
	}
	
	/**
	 * Add an additional <ProductContentType> element to the Onix XML tree
	 * @param typeCode The typeCode from Onix List 81
	 */
	public void addProductContentType(final String typeCode)
	{
		HashMap<String, String> productContentTypeArgs = new HashMap<>();
		productContentTypeArgs.put("productcontenttype", typeCode);
		requiredElements.add(new OnixProductContentTypeBuilder(productContentTypeArgs));
	}
	/**
	 * Add a <ProductIdentifier> node to the Onix XML tree
	 * @param type The additional <ProductIdentifier> value as a String
	 */
	public void addProductIdentifier(final String type)
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
	
	/**
	 * Add a <Subject> node to the Onix XML tree
	 * @param newSubject The additional Subject data, wrapped in a Subject object
	 */
	public void addSubject(final Subject newSubject)
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
	
	/**
	 * Add a <Title> node to the Onix XML tree. The node has no <Subtitle> child element.
	 * @param titleType The additional Onix title type code as a String - the rest of the title
	 *    information will be drawn from the product Title object
	 */
	public void addTitle(final String titleType)
	{
		HashMap<String, String> titleArgs = new HashMap<>();
		titleArgs.put("titletext", determineTitleStringByType(titleType, title));
		titleArgs.put("titletype", titleType);
		OnixTitleBuilder otb = new OnixTitleBuilder(titleArgs);
		requiredElements.add(otb); 
	}
	
	/**
	 * Add a <Title> node to the Onix XML tree. The node has a <Subtitle> child element. 
	 * @param titleType The additional Onix title type code as a String - the rest of the title
	 *    information will be drawn from the product Title object
	 */
	public void addTitleWithSubtitle(final String titleType)
	{
		HashMap<String, String> titleArgs = new HashMap<>();
		titleArgs.put("titletext", determineTitleStringByType(titleType, title));
		titleArgs.put("subtitle", determineTitleStringByType("subtitle", title));
		titleArgs.put("titletype", titleType);
		OnixTitleBuilder otb = new OnixTitleBuilder(titleArgs);
		requiredElements.add(otb); 
	}
	
	/**
	 * The main worker method for generating the XML tree for Onix 2.1 messages
	 * @param tagType Indicates which element naming style to use: SHORTTAG / REFERENCETAG
	 * @return Returns the root element of the Onix XML tree: <ONIXMessage> / <ONIXmessage>
	 */
	public Element buildOnix2(final int tagType)
	{		
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		Element parent = root;
		root.addAttribute(new Attribute("release", "2.1"));

		// first add specific ONIX 2.1 builders to the required elements configuration
		if(ProductType.valueOf(title.getFormat()) == ProductType.AUDIO)
		{
			replaceProductForm("AJ");
		}
		else
		{
			replaceProductForm("DG");
		}
		
		for(OnixPartsBuilder builder : requiredElements)
		{
			builder.initialize("2.1", tagType);
		}
		
		// then sort the list of required elements
		Collections.sort(requiredElements);
		
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		while(iterator.hasNext())
		{
			OnixPartsBuilder builder = iterator.next();
			
			// We need to distinguish between two styles of appending elements:
			// Elements created with direct subclasses of OnixPartsBuilder use the build() method,
			// elements created with subclasses of OnixSupplyDetailPartsBuilder use the appendElementsTo() method.
			// The first element that uses an OnixSupplyDetailPartsBuilder has the static sequence number 3000.
			if(builder.getSequenceNumber() < 3000)
			{
				Element nextElement = builder.build();
				// Builders that product elements that are only valid in ONIX 3 might return null
				// when called on with onixVersion 2.1
				if(nextElement != null)
				{
					parent.appendChild(nextElement);	
				}
				
				// if we have just built the header, then create a <product> node and make it the
				// parent element for all the following elements
				if(builder.getSequenceNumber() == 100)
				{
					Element productElement = createElement(tagType, "Product");
					parent.appendChild(productElement);
					parent = productElement;
				}
			}
			else if (builder.getSequenceNumber() == 3000)
			{
				Element supplyDetailNode = builder.build();
				parent.appendChild(supplyDetailNode);
				parent = supplyDetailNode;
				
				OnixSupplyDetailPartsBuilder supplyDetailPartsBuilder = (OnixSupplyDetailPartsBuilder) builder;
				supplyDetailPartsBuilder.appendElementsTo(parent, "2.1", tagType);
			}
			else // here we handle the <supplydetail> child elements
			{
				OnixSupplyDetailPartsBuilder supplyDetailPartsBuilder = (OnixSupplyDetailPartsBuilder) builder;
				supplyDetailPartsBuilder.appendElementsTo(parent, "2.1", tagType);
			}
		}
		
		return root;
	}
	
	/**
	 * The main worker method for generating the XML tree for Onix 3.0 messages
	 * @param tagType Indicates which element naming style to use: SHORTTAG / REFERENCETAG
	 * @return Returns the root element of the Onix XML tree: <ONIXMessage> / <ONIXmessage>
	 */
	public Element buildOnix3(final int tagType)
	{	
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		root.addAttribute(new Attribute("release", "3.0"));
		Element parent = root;
		Element product = null;
		
		// first add specific ONIX 3.0 builders to the required elements configuration
		if(ProductType.valueOf(title.getFormat()) == ProductType.AUDIO)
		{
			replaceProductForm("AJ");
		}
		else
		{
			replaceProductForm("ED");
		}
		
		for(OnixPartsBuilder builder : requiredElements)
		{
			builder.initialize("3.0", tagType);
		}
		
		// then sort the list of required elements
		Collections.sort(requiredElements);
		
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		
		// when iterating through the OnixPartsBuilders, we use an integer flag to determine if we need to 
		// create a new parent node for the following elements.
		// 
		int flag = 0;
		
		while(iterator.hasNext())
		{
			OnixPartsBuilder builder = iterator.next();
			// we've reached the first element after the header, so instantiate a product node and increment the flag
			if(flag == 0 && builder.getSequenceNumber() > 100)
			{
				product = createElement(tagType, "Product");
				parent.appendChild(product);
				parent = product;
				flag++;
			}
			// we've reached the first element for the <descriptivedetail> composite
			if(flag == 1 && builder.getSequenceNumber() >= 450)
			{
				Element descriptiveDetail = createElement(tagType, "DescriptiveDetail");
				product.appendChild(descriptiveDetail);
				parent = descriptiveDetail;
				flag++;
			}
			// we've reached the first element for the <collateraldetail> composite
			if(flag == 2 && builder.getSequenceNumber() >= 1800)
			{
				Element collateralDetail = createElement(tagType, "CollateralDetail");
				product.appendChild(collateralDetail);
				parent = collateralDetail;
				flag++;
			}
			// we've reached the first element for the <publishingdetail> composite
			if(flag == 3 && builder.getSequenceNumber() >= 2000)
			{
				Element publishingDetail = createElement(tagType,"PublishingDetail");
				product.appendChild(publishingDetail);
				parent = publishingDetail;
				flag++;
			}
			// we've reached the first element for the <relatedmaterial> composite
			if(flag == 4 && builder.getSequenceNumber() > 2600)
			{
				Element relatedMaterial = createElement(tagType, "RelatedMaterial");
				product.appendChild(relatedMaterial);
				parent = relatedMaterial;
				flag++;
			}
			// we've reached the first element for the <productsupply> composite
			if(flag == 5 && builder.getSequenceNumber() > 2800)
			{
				Element productSupply = createElement(tagType, "ProductSupply");
				product.appendChild(productSupply);
				parent = productSupply;
				flag++;
			}
			
			// here we're adding the next element that is not a predecessor of <supplydetail>
			if(builder.getSequenceNumber() < 3000)
			{
				Element nextElement = builder.build();
				if(nextElement != null)
				{
					parent.appendChild(nextElement);	
				}
			}
			// this is the <supplydetail> node
			else if (builder.getSequenceNumber() == 3000)
			{
				Element supplyDetailNode = builder.build();
				parent.appendChild(supplyDetailNode);
				parent = supplyDetailNode;
				
				OnixSupplyDetailPartsBuilder supplyDetailPartsBuilder = (OnixSupplyDetailPartsBuilder) builder;
				supplyDetailPartsBuilder.appendElementsTo(parent, "3.0", tagType);
			}
			else // here we handle the <supplydetail> child elements
			{
				OnixSupplyDetailPartsBuilder supplyDetailPartsBuilder = (OnixSupplyDetailPartsBuilder) builder;
				supplyDetailPartsBuilder.appendElementsTo(parent, "3.0", tagType);
			}
		}
		
		return root;
	}

	/**
	 * If the Onix message represents a downloadable audio product, we need to exchange / remove the 
	 * <EpubType> and the technical protection info.
	 */
	public void changeFormatToAudio()
	{
		replaceOnix2EpubType(null, true);
		removeOnixTechnicalProtectionBuilders();
	}
	
	/**
	 * If the Onix message represents a downloadable software product in a Zip container, we need to exchange 
	 * the <EpubType> and add <EpubTypeDescription>
	 */
	public void changeFormatToZip()
	{
		replaceOnix2EpubType("099", false);
		
		// add EpubTypeDescription for Onix 2.1
		HashMap<String, String> epubTypeDescArgs = new HashMap<>();
		requiredElements.add(new OnixEpubTypeDescriptionBuilder(epubTypeDescArgs));
	}

	/**
	 * The method produces a clone of the current OnixPartsDirector object. 
	 * The requiredElements List is copied flatly, which is acceptable as OnixPartsBuilder objects are
	 * not modified anymore after instantiation. 
	 */
	public OnixPartsDirector clone()
	{
		// use the private, parameter-less constructor
		OnixPartsDirector clonedDirector = new OnixPartsDirector();
		clonedDirector.title = this.title;
		
		// TODO: it would be better to make deep copies instead of flat ones of the OnixPartsBuilder objects
		for (OnixPartsBuilder builder : this.requiredElements)
		{
			clonedDirector.requiredElements.add(builder);
		}
		
		return clonedDirector;
	}
	
	/**
	 * Removes information that is associated with technical protection.
	 */
	public void removeOnixTechnicalProtectionBuilders()
	{
		removeBuilders(OnixTechnicalProtectionBuilder.class);
	}
	
	/**
	 * Remove a <ProductFormDetail> element from the Onix tree
	 * @param code The code that is used inside the <ProductFormDetail> element which is meant to be removed
	 */
	public void removeProductFormDetail(final String code)
	{
		HashMap<String, String> productFormDetailArgs = new HashMap<>();
		productFormDetailArgs.put("productformdetail", code);
		requiredElements.remove(new OnixProductFormDetailBuilder(productFormDetailArgs));
	}
	
	/**
	 * Replaces the <MediaFile> / <SupportingResource> element builders
	 * @param url The new URL to be used for pointing to a media file / supporting resource in the generated Onix
	 */
	public void replaceMediaResource(final String url)
	{
		removeBuilders(OnixMediaResourceBuilder.class);
		addMediaResource(url);
	}
	
	/**
	 * Replaces the OnixPartsBuilder that is responsible for generating <NotificationType> elements
	 * @param newType The new <NotificationType> value to replace the old one.
	 */
	public void replaceNotificationType(final String newType)
	{
		removeBuilders(OnixNotificationTypeBuilder.class);
		
		HashMap<String, String> notiTypeArgs = new HashMap<String, String>();
		notiTypeArgs.put("notificationtype", newType);
		requiredElements.add(new OnixNotificationTypeBuilder(notiTypeArgs));
	}
	
	/**
	 * Replaces the <EpubType> and <ProductFormDetail> elements of Onix 2.1 with a new value (or uses the default value for <EpubType>, if the epubType parameter is null). 
	 * @param epubType The new value as a String
	 */
	public void replaceOnix2EpubType(final String epubType, boolean isAudioProduct)
	{
		removeBuilders(OnixProductFormDetailBuilder.class);
		
		HashMap<String, String> productFormArgs = new HashMap<String, String>();
		if(epubType != null)
		{
			productFormArgs.put("epubtype", epubType);	
		}
		if(isAudioProduct)
		{
			productFormArgs.put("productformdetail", "A103");
		}
		requiredElements.add(new OnixProductFormDetailBuilder(productFormArgs));
	}
	
	/**
	 * Replaces the <ProductAvailability> element builder
	 * @param availCode The new <AvailabilityCode> to be used
	 * @param prodAvail The new <ProductAvailability> code to be used
	 */
	public void replaceProductAvailability(final String availCode, final String prodAvail)
	{
		removeBuilders(OnixProductAvailabilityBuilder.class);
		
		HashMap<String, String> prodAvailArgs = new HashMap<String, String>();
		prodAvailArgs.put("availabilitycode", availCode);
		prodAvailArgs.put("productavailability", prodAvail);
		requiredElements.add(new OnixProductAvailabilityBuilder(prodAvailArgs));
	}
	
	/**
	 * Replaces the builder that is responsible for the <ProductForm> code in the output Onix
	 * @param productFormCode The new <ProductForm> code as a String
	 */
	public void replaceProductForm(final String productFormCode)
	{
		removeBuilders(OnixProductFormBuilder.class);

		HashMap<String, String> productFormArgs = new HashMap<String, String>();
		productFormArgs.put("productform", productFormCode);
		requiredElements.add(new OnixProductFormBuilder(productFormArgs));
	}
	
	/**
	 * Replace the builder that is responsible for the <Publisher> node in the generated Onix
	 * @param namecodetype The code type of the new publisher to be used
	 * @param namecodevalue The actual code of the publisher
	 * @param publishername The name of the publisher
	 */
	public void replacePublisher(String namecodetype, String namecodevalue, String publishername)
	{
		removeBuilders(OnixPublisherBuilder.class);

		HashMap<String, String> publisherArgs = new HashMap<String, String>();
		publisherArgs.put("publisheridtype", namecodetype);
		publisherArgs.put("idvalue", namecodevalue);
		publisherArgs.put("publishername", publishername);
		requiredElements.add(new OnixPublisherBuilder(publisherArgs));
	}
	
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		for(OnixPartsBuilder nextBuilder : requiredElements)
		{
			buffer.append(nextBuilder.getClass() + "[" + nextBuilder.getSequenceNumber() + "]\n");
		}
		return buffer.toString();
	}
	
	// Helper method to instantiate a new XML element with a given element name
	private Element createElement(final int tagType, String name)
	{
		if(tagType == OnixPartsBuilder.SHORTTAG)
		{
			name = name.toLowerCase();
		}
		return new Element(name);
	}
	
	// Private helper method; returns the name of the ONIX file's root element based on the tag type (short tag / reference tag)
	private String getRootName(final int tagType)
	{
		String rootElementName;
		switch(tagType)
		{
			case OnixPartsBuilder.SHORTTAG:
				rootElementName = "ONIXmessage";
				break;
			case OnixPartsBuilder.REFERENCETAG:
				rootElementName = "ONIXMessage";
				break;
			default:
				rootElementName = "ONIXmessage";
		}
		return rootElementName;
	}
	
	// Helper for building product title Strings
	private String determineTitleStringByType(final String titleType, final Title title)
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
			case "05":
			case "abbreviatedtitle": return abbrevTitle;
			case "01":
			case "title":
			case "distinctivetitle":
			default: return mainTitle;
		}
	}
	
	private List<Integer> getIndicesOfProductFormDetail()
	{
		List<Integer> indices = new ArrayList<Integer>();
		
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		while(iterator.hasNext())
		{
			OnixPartsBuilder currentElement = iterator.next();
			if(currentElement instanceof OnixProductFormDetailBuilder)
			{
				indices.add(requiredElements.indexOf(currentElement));
			}
		}
		
		return indices;
	}
	
	// Helper for removing OnixPartsBuilders of a certain type
	private void removeBuilders(Class<?> builderClass)
	{
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		while(iterator.hasNext())
		{
			OnixPartsBuilder nextBuilder = iterator.next();
			if(builderClass.isInstance(nextBuilder))
			{
				iterator.remove();
			}
		}
	}
}