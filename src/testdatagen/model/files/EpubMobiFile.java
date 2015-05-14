package testdatagen.model.files;

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
	
	public String toString()
	{
		return "EpubMobi["+ISBN+"]";
	}
}