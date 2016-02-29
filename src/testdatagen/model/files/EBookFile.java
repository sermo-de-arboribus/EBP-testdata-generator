package testdatagen.model.files;

import testdatagen.model.Title;

/**
 * This abstract class is the root for all files that represent E-Book-Plant products, e.g. EPUB, PDF, 
 * digital audiobooks or Zip-containers that contain downloadable software products.
 */
public abstract class EBookFile extends File
{
	private static final long serialVersionUID = 2L;
	
	/**
	 * The demo flag is true, if the file is (conceptually) used as a sample or extract instead of the whole
	 * product.
	 */
	protected boolean isDemo;

	/**
	 * Concrete constructor
	 * @param title The product Title object that this file is belonging to.
	 * @param demoFlag The flag to indicate if this file is a sample (true) or the complete product (false).
	 */
	public EBookFile(final Title title, final boolean demoFlag)
	{
		super(title);
		this.isDemo = demoFlag;
	}
	
	/**
	 * @return Returns boolean true, if the file is representing a demo or sample file instead of the complete product.
	 */
	public boolean isDemoFile()
	{
		return isDemo;
	}
}