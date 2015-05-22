package testdatagen.model.files;

import testdatagen.model.Title;

public class PDFCoverFile extends GraphicFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
		
	public PDFCoverFile(long ISBN)
	{
		super(Long.toString(ISBN) + "_cover.pdf", GraphicFile.Type.COVER);
		this.ISBN = ISBN;
	}
	
	public void generate(Title title, java.io.File destDir)
	{
		// TODO: implement PDF cover generation
	}
	
	public String toString()
	{
		return "CoverPDF["+ISBN+"]";
	}
}