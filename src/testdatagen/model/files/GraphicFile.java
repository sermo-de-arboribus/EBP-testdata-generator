package testdatagen.model.files;

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
