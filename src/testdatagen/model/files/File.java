package testdatagen.model.files;

import java.io.Serializable;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

/**
 * This abstract class is the root for all concrete classes that the TDG uses to generate the required output files.
 * To distinguish testdatagen.model.files.File from java.io.File, the latter one is always used with its fully qualified name
 * within this package.
 */
public abstract class File implements Serializable
{
	private static final long serialVersionUID = 2L;
	protected Title title;

	/**
	 * Base constructor
	 * @param title The product Title object that this file belongs to.
	 */
	public File(final Title title)
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
	
	/**
	 * Generates a file and saves it to disk
	 * @destFolder java.io.File: the destination directory for saving the file
	 * @return java.io.File: the File object representing the stored file.
	 */
	public abstract java.io.File generate(final java.io.File destFolder);
	
	/**
	 * Every file type for the E-Book-Plant has to follow certain naming conventions. So every sub-class
	 * needs to implement the buildFileName() method to return a conventionalized and parametrized file name.  
	 * @return The file name for the concrete File object.
	 */
	protected abstract String buildFileName();
}