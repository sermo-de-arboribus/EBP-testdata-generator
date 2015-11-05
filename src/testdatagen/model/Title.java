package testdatagen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import testdatagen.model.files.EBookFile;
import testdatagen.model.files.File;
import testdatagen.model.files.GraphicFile;
import testdatagen.onixbuilder.*;
import testdatagen.templates.AuthorBlurbTemplate;
import testdatagen.templates.TitleBlurbTemplate;
import testdatagen.templates.TitleBlurbTemplateType;
import testdatagen.utilities.Utilities;

public class Title implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] validEpubTypes = {"Epub", "PDF", "Mobi", "EpubMobi", "IBook", "SoftwareZip", "AudioZip"};
	private static final String[] validFormats = ProductType.productTypeNames();
	
	private long isbn13;
    private String uid, name, author; // TODO: a book could have several authors with different author types. Expand model to reflect that
    private boolean mediaFileLink;
    private String mediaFileUrl;
    private HashSet<File> files;
    private String epubType;
    private String format; // format = protection + epubType
    private String protection; // WM = watermarked, ND = no protection, DRM = hard DRM
    private OnixPartsDirector onixPartsDirector;
    
    public Title(final long isbn13, final String uid, final String name, final String author, final boolean mediaFileLink)
    {
    	this.isbn13 = isbn13;
    	this.uid = uid;
    	this.name = name;
    	this.author = author;
    	this.mediaFileLink = mediaFileLink;
    	files = new HashSet<File>();
    	onixPartsDirector = new OnixPartsDirector(this);
    }
    
    public synchronized void addFile(final File newFile)
    {
    	files.add(newFile);
    }


	public synchronized void addMainProductFile(EBookFile ebookFile, String format)
	{
		files.add(ebookFile);
		setFormat(format);
	}
	
	public synchronized String getAuthor()
	{
		return author;
	}
	
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
	
	public synchronized String getAuthorBlurb()
	{
		AuthorBlurbTemplate template = new AuthorBlurbTemplate(new Locale("de"), this);
		return template.fillWithText();
	}
	
	public synchronized String getEpubType()
	{
		if(epubType == null)
		{
			return "Unknown";
		}
		return epubType;
	}
	
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
				return "127";
				//TODO: how to represent software / audio products in ProductFormDetail?
			case "099":
				return "WARNING! Software / Audio Products not implemented yet";
			default:
				return "Unknown";
		}
	}
	
	public String getFormat()
	{
		return format;
	}
	
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
		}
		return returnList;
	}
	
	public synchronized OnixPartsDirector getOnixPartsDirector()
	{
		return onixPartsDirector;
	}
	
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
	
	public synchronized String getShortBlurb()
	{
		TitleBlurbTemplate template = new TitleBlurbTemplate(new Locale("de"), this, TitleBlurbTemplateType.SHORT);
		return template.fillWithText();
	}
	
	public synchronized HashSet<File> getFiles()
	{
		return files;
	}
	
	public long getIsbn13()
	{
		return isbn13;
	}

	public synchronized String getMediaFileUrl()
	{
		return mediaFileUrl;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getUid()
	{
		return uid;
	}

	public boolean hasMediaFileLink()
	{
		return mediaFileLink;
	}
    
    public synchronized boolean removeFile(final File remFile)
    {
        return files.remove(remFile);
    }

	private boolean isValidFormat(String formatString)
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
	private void setEpubType(String epubType)
	{	
		this.epubType = epubType;
	}
	
    /*
     * setFormat takes in an EBP format string like "WMPDF" or "NDEPUB", validates it and stores file format,
     * protection type and the format string itself as object attributes
     */
    private void setFormat(String formatString)
    {
    	if(isValidFormat(formatString))
    	{
    		format = formatString;
    		setEpubType(Utilities.formatToFileType(formatString));
    		if(formatString.startsWith("ND"))
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
    
    public synchronized void setMediaFileUrl(String url)
    {
    	mediaFileUrl = url;
    }
}
