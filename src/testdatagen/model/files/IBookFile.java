package testdatagen.model.files;

import java.io.File;

import testdatagen.model.Title;

public class IBookFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IBookFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public String toString()
	{
		return "iBook[" + title.getIsbn13() + "]";
	}

	@Override
	public File generate(final File destDir)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + ".ibook";
	}
}