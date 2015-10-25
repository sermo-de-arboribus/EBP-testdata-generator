package testdatagen.model.files;

import java.io.File;

import testdatagen.model.Title;

public class PDFFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public PDFFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + ".pdf", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		String fileString = "PDF";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" +ISBN+ "]";
	}

	@Override
	public File generate(Title title, File destDir)
	{
		System.out.println("Generating PDF e-book file in folder: " + destDir);
		PDFCoverFile pcf = new PDFCoverFile(title.getIsbn13());
		java.io.File pdf = pcf.generate(title, destDir);
		System.out.println("File saved in " + pdf.getAbsolutePath());
		return pdf;
	}
}