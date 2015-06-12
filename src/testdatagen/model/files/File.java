package testdatagen.model.files;

import testdatagen.model.Title;

public abstract class File extends java.io.File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public File(String pathname)
	{
		super(pathname);
	}
	
	/*
	 * generates a graphic file and saves it to disc
	 * @title Title: the title object to create the graphic file for
	 * @destDir File: the destination directory for saving the file
	 * @return File: the file path of the stored file.
	 */
	public abstract java.io.File generate(Title title, java.io.File destPath);
}