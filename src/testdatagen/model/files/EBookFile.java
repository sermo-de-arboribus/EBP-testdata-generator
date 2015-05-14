package testdatagen.model.files;

public abstract class EBookFile extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean isDemo;

	public EBookFile(String pathname, boolean demoFlag)
	{
		super(pathname);
		this.isDemo = demoFlag;
	}
	
	public boolean isDemoFile()
	{
		return isDemo;
	}
}