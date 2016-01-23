package testdatagen.model.files;

import java.io.Serializable;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

public abstract class File implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	protected Title title;
	
	public File(Title title)
	{
		if(title == null)
		{
			NullPointerException exc = new NullPointerException();
			Utilities.showErrorPane("Contructor tried to instantiate a File object, but the title parameter passed in was a null reference" + exc.getStackTrace() + "\n", exc);
		}
		else
		{
			this.title = title;	
		}
	}
	
	/*
	 * generates a file and saves it to disc
	 * @title Title: the title object to create the graphic file for
	 * @destFolder File: the destination directory for saving the file
	 * @return File: the File object of the stored file.
	 */
	public abstract java.io.File generate(java.io.File destFolder);
	protected abstract String buildFileName();
}