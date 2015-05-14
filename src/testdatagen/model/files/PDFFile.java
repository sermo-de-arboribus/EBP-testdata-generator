package testdatagen.model.files;

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
		return "PDF["+ISBN+"]";
	}
}