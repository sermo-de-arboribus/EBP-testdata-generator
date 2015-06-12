package testdatagen.model.files;

import java.io.File;

import testdatagen.model.Title;

public class IBookFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public IBookFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + ".ibook", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		return "iBook["+ISBN+"]";
	}

	@Override
	public File generate(Title title, File destDir)
	{
		// TODO Auto-generated method stub
		return null;
	}
}