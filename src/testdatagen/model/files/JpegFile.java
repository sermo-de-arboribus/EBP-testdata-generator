package testdatagen.model.files;

public class JpegFile extends GraphicFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public JpegFile(long ISBN, GraphicFile.Type type)
	{
		super(Long.toString(ISBN) + "_" + type.toString().toLowerCase() + ".jpg", type);
		this.ISBN = ISBN;
	}
		
	public String toString()
	{
		return type.toString().toLowerCase() + "JPG["+ISBN+"]";
	}
}