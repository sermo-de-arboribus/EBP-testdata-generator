package testdatagen.model.files;

import testdatagen.model.Title;

public class EpubMobiFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public EpubMobiFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + "_EpubMobi.epub", demoFlag);
		this.ISBN = ISBN;
	}
	
	public java.io.File generate(Title title, java.io.File destPath)
	{
		EpubFile epubFile = new EpubFile(ISBN, this.isDemoFile());
		java.io.File epubMobiFile = epubFile.generate(title, destPath);
		return epubMobiFile;
	}
	
	public String toString()
	{
		String fileString = "EpubMobi";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" +ISBN+ "]";
	}
}