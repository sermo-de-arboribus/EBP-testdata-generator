package testdatagen.model.files;

import testdatagen.model.Title;

public abstract class EBookFile extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	protected boolean isDemo;

	public EBookFile(Title title, boolean demoFlag)
	{
		super(title);
		this.isDemo = demoFlag;
	}
	
	public boolean isDemoFile()
	{
		return isDemo;
	}
}