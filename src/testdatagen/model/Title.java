package testdatagen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import testdatagen.model.files.File;
import testdatagen.model.files.GraphicFile;

public class Title implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long isbn13;
    private String uid, name, author;
    private boolean mediaFileLink;
    private HashSet<File> files;
    
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

	public String getAuthor()
	{
		return author;
	}
	
	/**
	 * getCoverFiles returns all GraphicFiles that are of the front cover, square cover or back cover type.
	 * Other types of GraphicFiles are not considered to be cover files.
	 * @return an ArrayList of cover files, an empty list if the title has no cover files.
	 */
	public ArrayList<GraphicFile> getCoverFiles()
	{
		ArrayList<GraphicFile> returnList = new ArrayList<>();
		for( File file : files)
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
}