package testdatagen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import testdatagen.model.files.EBookFile;
import testdatagen.model.files.File;
import testdatagen.model.files.GraphicFile;
import testdatagen.utilities.Utilities;

public class Title implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] validEpubTypes = {"Epub", "PDF", "Mobi", "EpubMobi", "IBook", "SoftwareZip", "AudioZip"};
	private static final String[] validFormats = {"PDF", "WMPDF", "NDPDF", "EPUB", "WMPEUB", "NDEPUB", "ZIP", "IBOOOK", "NDMOBI", "WMMOBI", "AUDIO"};
	
	private long isbn13;
    private String uid, name, author; // TODO: a book could have several authors with different author types. Expand model to reflect that
    private boolean mediaFileLink;
    private HashSet<File> files;
    private String epubType;
    private String format; // format = protection + epubType
    private String protection; // WM = watermarked, ND = no protection, DRM = hard DRM
    
    public Title(final long isbn13, final String uid, final String name, final String author, final boolean mediaFileLink)
    {
    	this.isbn13 = isbn13;
    	this.uid = uid;
    	this.name = name;
    	this.author = author;
    	this.mediaFileLink = mediaFileLink;
    	files = new HashSet<File>();
    }
    
    public void addFile(final File newFile)
    {
    	files.add(newFile);
    }


	public void addMainProductFile(EBookFile ebookFile, String format)
	{
		files.add(ebookFile);
		setFormat(format);
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public String getAuthorFirstName()
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
	
	public String getAuthorLastName()
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
	public ArrayList<GraphicFile> getCoverFiles()
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
	
	public String getEpubType()
	{
		if(epubType == null)
		{
			return "Unknown";
		}
		return epubType;
	}
	
	public String getEpubTypeForONIX()
	{
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
	
	public String getFormat()
	{
		return format;
	}
	
	public String getProtectionTypeForONIX()
	{
		if(protection == null)
		{
			Utilities.showErrorPane("Something went wrong with title " + this.isbn13 + ": protection type is not initialized", new NullPointerException());
		}
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
	
	public HashSet<File> getFiles()
	{
		return files;
	}
	
	public long getIsbn13()
	{
		return isbn13;
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
    
    public boolean removeFile(final File remFile)
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
    }
}