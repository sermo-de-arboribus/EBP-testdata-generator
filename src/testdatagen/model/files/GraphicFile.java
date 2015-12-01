package testdatagen.model.files;

import testdatagen.model.Title;

public abstract class GraphicFile extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Type type;

	public GraphicFile(Title title, Type type)
	{
		super(title);
		this.type = type;
	}
	
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
