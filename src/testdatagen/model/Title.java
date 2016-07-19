package testdatagen.model;

import java.io.Serializable;
import java.util.*;

import testdatagen.model.files.EBookFile;
import testdatagen.model.files.File;
import testdatagen.model.files.GraphicFile;
import testdatagen.onixbuilder.*;
import testdatagen.templates.*;
import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;

/**
 * The model class for a product, which is the basic test entity of the E-Book-Plant
 */
public class Title implements Serializable
{
	private static final long serialVersionUID = 2L;
	private static final String[] validFormats = ProductType.productTypeNames();
	
	private long isbn13;
    private String uid, name, author; // TODO: a book could have several authors with different author types. Expand model to reflect that
    private String mediaFileUrl;
    private HashSet<File> files;
    private String epubType;
    private String format; // format = protection + epubType
    private String protection; // WM = watermarked, ND = no protection, DRM = hard DRM
    private OnixPartsDirector onixPartsDirector;
    private Set<Subject> subjects;
    private Price basePrice;
    private Set<Price> additionalPrices;
    
    /**
     * Constructor
     * @param isbn13 The 13-digit ISBN of this product
     * @param uid The UID (RecordReference in Onix) of this product
     * @param name The title of this product
     * @param author The (primary) author of this product
     * @param format The product format, in the E-Book-Plant style String
     */
    public Title(final long isbn13, final String uid, final String name, final String author, final String format)
    {
    	this.isbn13 = isbn13;
    	this.uid = uid;
    	this.name = name;
    	this.author = author;
    	setFormat(format);
    	basePrice = getRandomBasePrice();
    	files = new HashSet<File>();
    	onixPartsDirector = new OnixPartsDirector(this);
    	subjects = new HashSet<Subject>();
    	additionalPrices = new HashSet<Price>();
    }
    
    /**
     * Add a file object that needs to be generated for this product
     * @param newFile The file object to add to this product
     */
    public synchronized void addFile(final File newFile)
    {
    	files.add(newFile);
    }
	
    /**
     * Add a Price object to this product (a product can have several prices for different type-currency-country combinations).
     * @param newPrice
     */
    public synchronized void addPrice(final Price newPrice)
    {
    	additionalPrices.add(newPrice);
    	onixPartsDirector.addPrice(newPrice);
    }
    
    /**
     * Add a Subject object to this product
     * @param newSubject
     */
    public synchronized void addSubject(final Subject newSubject)
    {
    	subjects.add(newSubject);
    	onixPartsDirector.addSubject(newSubject);
    }
    
    /**
     * get a clone of this Title object
     * @return: The cloned Title object
     */
    public synchronized Title clone()
    {
		Title clonedTitle = new Title(
				this.getIsbn13(),
				this.getUid(),
				this.getName(),
				this.getAuthor(),
				this.getFormat()
				);
		
		for(File origFile : this.getFiles())
		{
			// TODO: It would be better to make a deep file copy rather than a flat reference copy
			// However, currently File objects are not modified after being instantiated, they are read-only.
			// Therefore it is ok to use flat copies for the moment.
			clonedTitle.addFile(origFile);
		}
		
		// TODO: It would be better to make a deep copy of the OnixPartsDirector, too. See comment above.
		clonedTitle.onixPartsDirector = this.onixPartsDirector;
		
		clonedTitle.basePrice = this.basePrice;
		for(Price price : this.additionalPrices)
		{
			clonedTitle.additionalPrices.add(price.clone());
		}
		for(Subject subject : this.subjects)
		{
			clonedTitle.subjects.add(subject.clone());
		}
		
		return clonedTitle;
    }
    
    /**
     * Get the author of this product
     * @return String of author's complete name
     */
	public synchronized String getAuthor()
	{
		return author;
	}
	
	/**
	 * Get a promo text about the author of this product
	 * @return String of a random promo text about the author of this product
	 */
	public synchronized String getAuthorBlurb()
	{
		AuthorBlurbTemplate template = new AuthorBlurbTemplate(new Locale("de"), this);
		return template.fillWithText();
	}
	
	/**
	 * Get the author's first name
	 * @return String of the author's first name.
	 */
	public synchronized String getAuthorFirstName()
	{
		if(author.contains(" "))
		{
			return author.substring(0, author.lastIndexOf(' '));	
		}
		else
		{
			return author;
		}
	}
	
	/**
	 * Get the author's family name
	 * @return String of the author's family name
	 */
	public synchronized String getAuthorLastName()
	{
		if(author.contains(" "))
		{
			return author.substring(author.lastIndexOf(' ') + 1);
		}
		else
		{
			return author;
		}
	}
	
	public synchronized String getCorporateContributor()
	{
		CorporateNameTemplate template = new CorporateNameTemplate(new Locale("de"));
		return template.fillWithText();
	}
	
	/**
	 * getCoverFiles returns all GraphicFiles that are of the front cover, square cover or back cover type.
	 * Other types of GraphicFiles are not considered to be cover files.
	 * @return an ArrayList of cover files, an empty list if the title has no cover files.
	 */
	public synchronized ArrayList<GraphicFile> getCoverFiles()
	{
		ArrayList<GraphicFile> returnList = new ArrayList<>();
		for(File file : files)
		{
			if(file instanceof GraphicFile)
			{
				GraphicFile graphicFile = (GraphicFile) file;
				if(graphicFile.isCover())
				{
					returnList.add(graphicFile);
				}
			}
		}
		return returnList;
	}
	
	/**
	 * Get the base price of the product, i.e. the price that is used for calculating other prices.
	 * @return Price object with the product's base price.
	 */
	public synchronized Price getBasePrice()
	{
		// we don't want the base price to be changed externally, so we only hand out a clone
		return basePrice.clone();
	}

	/**
	 * Get the EpubType for this product
	 * @return An EpubType String as it is used in the E-Book-Plant
	 */
	public synchronized String getEpubType()
	{
		if(epubType == null)
		{
			return "Unknown";
		}
		return epubType;
	}
	
	/**
	 * Get the EpubType code for Onix 2.1
	 * @return An Onix code String for the EpubType element
	 */
	public synchronized String getEpubTypeForONIX()
	{
		if(epubType == null) return "Unknown";
		
		switch(epubType)
		{
			case "PDF":
				return "002";
			case "Epub":
				return "029";
			case "IBook":
				return "044";
			case "SoftwareZip":
			case "AudioZip":
				return "099";
			case "Mobi":
				return "022";
			default:
				return "Unknown";
		}
	}
	
	/**
	 * Get the EpubType for Onix 3.0
	 * @return The EpubType string for Onix 3.0
	 */
	public synchronized String getEpubTypeForProductFormDetail()
	{
		if(epubType == null) return "Unknown";
		
		switch(getEpubTypeForONIX())
		{
			case "002":
				return "E107";
			case "029":
				return "E101";
			case "044":
				return "E141";
			case "022":
				return "E127";
				//TODO: how to represent software / audio products in ProductFormDetail?
			case "099":
				return "WARNING! Software / Audio Products not implemented yet";
			default:
				return "Unknown";
		}
	}
	
	/**
	 * Get the product format String
	 * @return String representing the product format
	 */
	public String getFormat()
	{
		return format;
	}
	
	/**
	 * Get the main product file object for this product (e.g. the e-book in EPUB or PDF format)
	 * @return EBookFile object representing the main file for this product
	 */
	public EBookFile getMainProductFile()
	{
		for(File file : files)
		{
			if(file instanceof EBookFile)
			{
				EBookFile mainProductFile = (EBookFile) file;
				if(!mainProductFile.isDemoFile())
				{
					return mainProductFile;
				}
			}
		}
		return null;
	}
	
	/**
	 * getNonCoverFiles returns all Files that are not a cover file.
	 * @return an ArrayList of non-cover files, an empty list if the title has no non-cover files.
	 */
	public synchronized ArrayList<File> getNonCoverFiles()
	{
		ArrayList<File> returnList = new ArrayList<>();
		for(File file : files)
		{
			if(!(file instanceof GraphicFile))
			{
				returnList.add(file);
			}
			else
			{
				GraphicFile graphicFile = (GraphicFile) file;
				if(!graphicFile.isCover())
				{
					returnList.add(file);
				}
			}
		}
		return returnList;
	}
	
	/**
	 * Get the OnixPartsDirector that holds this product's Onix configuration
	 * @return OnixPartsDirector object
	 */
	public synchronized OnixPartsDirector getOnixPartsDirector()
	{
		return onixPartsDirector;
	}

	/**
	 * Get the protection type (hard DRM, watermarking, no protection) for this product
	 * @return A String representing this product's protection type
	 */
	public synchronized String getProtectionTypeForONIX()
	{
		if(protection == null)
		{
			Utilities.showErrorPane("Something went wrong with title " + this.isbn13 + ": protection type is not initialized", new NullPointerException());
			return "";
		}
		else 
		{
			switch(protection)
			{
			case("WM"):
				return "02";
			case("ND"):
				return "00";
			case("DRM"):
				return "03";
			default:
				Utilities.showErrorPane("Something went wrong with title " + this.isbn13 + ": protection has unsupported value", new IllegalArgumentException());
				return "";
			}
		}
	}
	
	public synchronized String getSeriesTitle()
	{
		SeriesTemplate template = new SeriesTemplate(new Locale("de"));
		return template.fillWithText();
	}
	
	/**
	 * Get a short promo text for this product
	 * @return A String with a short random promo text
	 */
	public synchronized String getShortBlurb()
	{
		TitleBlurbTemplate template = new TitleBlurbTemplate(new Locale("de"), TitleBlurbTemplateType.SHORT);
		return template.fillWithText();
	}
	
	/**
	 * Get a medium length promo text for this product
	 * @return A String with a medium length promo text
	 */
	public synchronized String getMediumBlurb()
	{
		TitleBlurbTemplate template = new TitleBlurbTemplate(new Locale("de"), TitleBlurbTemplateType.MEDIUM);
		return template.fillWithText();
	}
	
	/**
	 * Get a long promo text for this product
	 * @return A String with a long promo text for this product
	 */
	public synchronized String getLongBlurb()
	{
		TitleBlurbTemplate template = new TitleBlurbTemplate(new Locale("de"), TitleBlurbTemplateType.LONG);
		return template.fillWithText();
	}
	
	/**
	 * Get all files that need to be generated for this product
	 * @return A HashSet of all file objects
	 */
	public synchronized HashSet<File> getFiles()
	{
		return files;
	}
	
	/**
	 * Get the 13-digit ISBN (EAN) for this product
	 * @return long ISBN
	 */
	public long getIsbn13()
	{
		return isbn13;
	}

	/**
	 * Get the mediaFileUrl (i.e. a Dropbox download URL for the cover image of this product)
	 * @return The mediaFileUrl as a String
	 */
	public synchronized String getMediaFileUrl()
	{
		return mediaFileUrl;
	}
	
	/**
	 * Get the title of this product
	 * @return String title of this product
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get the UID (in Onix the RecordReference element) for this product
	 * @return A String representing the UID of this product
	 */
	public String getUid()
	{
		return uid;
	}

	/**
	 * Flag to check if this product has a mediaFileUrl (i.e. a Dropbox download URL for the product's cover image)
	 */
	public boolean hasMediaFileLink()
	{
		return mediaFileUrl != null;
	}
    
	/**
	 * Remove a file object from the set of file that need to be generated for this product.
	 * @param remFile The File object to be removed
	 * @return true, if this file was found and removed from the set
	 */
    public synchronized boolean removeFile(final File remFile)
    {
        return files.remove(remFile);
    }

    // Helper method to generate a random base price within certain bounds
    private Price getRandomBasePrice()
    {
    	Random random = new Random();
		double basePrice = Math.round((random.nextDouble() * 28.99 + 1) * 100) / 100.0;
		return new Price("04", "" + basePrice, "EUR", "DE");
    }

	private boolean isValidFormat(final String formatString)
	{
		boolean valid = false;
		for(String format : validFormats)
		{
			if(format.equals(formatString))
			{
				valid = true;
			}
		}
		return valid;
	}
	
    // this is private, because epubType is set based on the format String -> see setFormat()
	private void setEpubType(final String epubType)
	{	
		this.epubType = epubType;
	}
	
    /*
     * setFormat takes in an EBP format string like "WMPDF" or "NDEPUB", validates it and stores file format,
     * protection type and the format string itself as object attributes
     */
    private void setFormat(final String formatString)
    {
    	if(isValidFormat(formatString))
    	{
    		format = formatString;
    		setEpubType(TitleUtils.formatToFileType(formatString));
    		if(formatString.startsWith("ND") || formatString.equals("ZIP"))
    		{
    			protection = "ND";
    		}
    		else if(formatString.startsWith("WM"))
    		{
    			protection = "WM";
    		}
    		else 
    		{
    			protection = "DRM";
    		}
    	}
    	else
    	{
    		Utilities.showWarnPane("The format for this E-Book product is not valid: " + formatString + " (title is " + isbn13);
    	}
    }
    
    /**
     * Set the mediaFileUrl (i.e. the Dropbox URL for downloading this product's cover image)
     * @param url The Dropbox URL as a String
     */
    public synchronized void setMediaFileUrl(String url)
    {
    	mediaFileUrl = url;
    	onixPartsDirector.replaceMediaResource(url);
    }
}
