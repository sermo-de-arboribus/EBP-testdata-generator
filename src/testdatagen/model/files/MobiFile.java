package testdatagen.model.files;

import java.io.File;

import testdatagen.model.Title;

public class MobiFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public MobiFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + ".mobi", demoFlag);
	}
	
	public String toString()
	{
		String fileString = "Mobi";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" +ISBN+ "]";
	}

	@Override
	public File generate(Title title, File destDir) {
		// TODO Auto-generated method stub
		return null;
	}
}