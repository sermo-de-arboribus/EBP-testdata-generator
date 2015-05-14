package testdatagen.model.files;

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
		
	public String toString()
	{
		return "CoverPDF["+ISBN+"]";
	}
}