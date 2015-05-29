package testdatagen.model.files;

import testdatagen.model.Title;

public abstract class GraphicFile extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Type type;

	public GraphicFile(String pathname, Type type)
	{
		super(pathname);
		this.type = type;
	}
	
	/*
	 * generates a graphic file and saves it to disc
	 * @title Title: the title object to create the graphic file for
	 * @destDir File: the destination directory for saving the file
	 * @return File: the file path of the stored file.
	 */
	public abstract java.io.File generate(Title title, java.io.File destDir);
	
	public Type getType()
	{
		return type;
	}
	
	public boolean isCover()
	{
		return (type == Type.COVER | type == Type.SQUARECOVER | type == Type.BACKCOVER);
	}
	
	public enum Type
	{
		COVER, SCREENSHOT, PACKSHOT, SQUARECOVER, BACKCOVER
	}
}