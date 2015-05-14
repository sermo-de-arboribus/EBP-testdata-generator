package testdatagen.model;

import java.io.Serializable;
import java.util.HashSet;

import testdatagen.model.files.File;

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
    
    public Title(long isbn13, String uid, String name, String author, boolean mediaFileLink)
    {
    	this.isbn13 = isbn13;
    	this.uid = uid;
    	this.name = name;
    	this.author = author;
    	this.mediaFileLink = mediaFileLink;
    	files = new HashSet<File>();
    }
    
    public void addFile(File newFile)
    {
    	files.add(newFile);
    }
    
    public boolean removeFile(File remFile)
    {
        return files.remove(remFile);
    }

	public long getIsbn13()
	{
		return isbn13;
	}

	public String getUid()
	{
		return uid;
	}

	public String getName()
	{
		return name;
	}

	public String getAuthor()
	{
		return author;
	}

	public boolean hasMediaFileLink()
	{
		return mediaFileLink;
	}

	public HashSet<File> getFiles()
	{
		return files;
	}
}