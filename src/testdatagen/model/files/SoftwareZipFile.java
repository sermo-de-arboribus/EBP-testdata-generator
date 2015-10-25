package testdatagen.model.files;

import java.io.File;

import testdatagen.model.Title;

public class SoftwareZipFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public SoftwareZipFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN)  + (demoFlag ? "_demosoftware" : "" ) + ".zip", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		return "SoftwareZip["+ISBN+"]";
	}

	@Override
	public File generate(Title title, File destDir) 
	{
		// TODO Auto-generated method stub
		return null;
	}
}