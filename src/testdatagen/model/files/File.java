package testdatagen.model.files;

import java.io.Serializable;

import testdatagen.model.Title;

public abstract class File implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Title title;
	
	public File(Title title)
	{
		this.title = title;
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