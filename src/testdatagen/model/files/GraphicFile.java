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
	
	public enum Type
	{
		COVER, SCREENSHOT, PACKSHOT, SQUARECOVER, BACKCOVER
	}
}