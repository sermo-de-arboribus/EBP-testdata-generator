package testdatagen.model.files;

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
}