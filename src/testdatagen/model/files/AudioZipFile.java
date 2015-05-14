package testdatagen.model.files;


public class AudioZipFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public AudioZipFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + "_audio.zip", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		return "AudioZip["+ISBN+"]";
	}
}