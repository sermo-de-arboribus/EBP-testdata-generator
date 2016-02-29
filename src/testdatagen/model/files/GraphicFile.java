package testdatagen.model.files;

import testdatagen.model.Title;

/**
 * An abstract class that is the base class for all files that represent graphic material relating to a product.
 * Note that the distinction between GraphicFile and EBookFile is conceptual, not technical. A PDF file can 
 * represent an e-book (then it is an EBookFile) or a book cover (then it is a GraphicFile). 
 */
public abstract class GraphicFile extends File
{
	private static final long serialVersionUID = 2L;
	protected Type type;

	/**
	 * Constructor
	 * @param title The product Title object that this graphic file belongs to
	 * @param type
	 */
	public GraphicFile(final Title title, final Type type)
	{
		super(title);
		this.type = type;
	}
	
	/**
	 * @return Returns the type of the graphic file (e.g. COVER, SCREENSHOT, BACKCOVER, ...) 
	 */
	public Type getType()
	{
		return type;
	}
	
	/**
	 * @return Boolean flag, returns true is the file is a cover file or false if it is a non-cover graphical file
	 */
	public boolean isCover()
	{
		return (type == Type.COVER | type == Type.SQUARECOVER | type == Type.BACKCOVER);
	}

	/**
	 * A helping enumeration of all types / roles a graphic file could fulfill in the E-Book-Plant
	 * This is also used in the concrete classes for file name conventions
	 */
	public enum Type
	{
		COVER, SCREENSHOT, PACKSHOT, SQUARECOVER, BACKCOVER;
		
		@Override
		public String toString()
		{
			switch(this)
			{
				case COVER: return "COVER";
				case SCREENSHOT: return "SCREEN";
				case PACKSHOT: return "3DPACKSHOT";
				case SQUARECOVER: return "SQUARED";
				case BACKCOVER: return "BC";
				default: return "";
			}
		}
	}
}
