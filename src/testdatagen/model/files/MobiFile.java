package testdatagen.model.files;

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
		return "Mobi["+ISBN+"]";
	}
}