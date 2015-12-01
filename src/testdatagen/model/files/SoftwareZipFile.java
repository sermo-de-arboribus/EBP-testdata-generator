package testdatagen.model.files;

import java.io.File;

import testdatagen.model.Title;

public class SoftwareZipFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SoftwareZipFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public String toString()
	{
		return "SoftwareZip["+ title.getIsbn13() +"]";
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
		return Long.toString(title.getIsbn13())  + (isDemoFile() ? "_demosoftware" : "" ) + ".zip";
	}
}