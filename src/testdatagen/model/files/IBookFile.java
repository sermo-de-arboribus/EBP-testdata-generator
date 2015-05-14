package testdatagen.model.files;

public class IBookFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public IBookFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + ".ibook", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		return "iBook["+ISBN+"]";
	}
}