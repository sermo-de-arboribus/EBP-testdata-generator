package testdatagen;

import testdatagen.model.Title;
import testdatagen.model.files.File;

public interface FileFactory
{
	/**
	 * The generateFile method instantiates a file object (see the File sub-classes in the testdatagen.model.files
	 * package).
	 * @param format The requested file format.
	 * @param title The product Title object that this file belongs to.
	 * @return A File object.
	 */
	 public File generateFile(String format, Title title);
}