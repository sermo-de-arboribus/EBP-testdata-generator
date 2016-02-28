package testdatagen.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * The TitleTableModel is used to display the titles belonging to a scenario within a JTable
 */
public class TitleTableModel extends AbstractTableModel
{
	// names of the columns
	private static final String[] COLUMN_NAMES = {"UID", "ISBN", "Name", "Author", "MediaFileLink", "Files"};
	
	// indexes of the columns
	private static final int COLUMN_IDX_UID = 0;
	private static final int COLUMN_IDX_ISBN = 1;
	private static final int COLUMN_IDX_NAME = 2;
	private static final int COLUMN_IDX_AUTHOR = 3;
	private static final int COLUMN_IDX_MEDIAFILELINK = 4;
	private static final int COLUMN_IDX_FILES = 5;

	// list of titles
	private final List<Title> titles;
	
	/**
	 * Constructor
	 * @param titles A List of Title objects that will be maintained by this table model
	 */
	public TitleTableModel(final List<Title> titles)
	{
		this.titles = new ArrayList<>(titles);
	}
	
	/**
	 * Add a product Title object to this table model
	 * @param newTitle The Title object to be added
	 */
	public void addTitle(final Title newTitle)
	{
		int listSize = titles.size();
		titles.add(listSize, newTitle);
		fireTableRowsInserted(listSize, listSize);
	}
	
	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(final int colIndex)
	{
		return COLUMN_NAMES[colIndex];
	}
	
	/**
	 * Returns a list of titles that currently belong to this title model
	 * @return A copy of the Title objects list.
	 */
	public List<Title> getListOfTitles()
	{
		// To make sure the internal list is immutable, return a deep copy of the listOfTitles
		// instead of a reference to this object's internal list.
		List<Title> copiedTitleList = new ArrayList<Title>();
		for(Title origTitle : titles)
		{
			copiedTitleList.add(origTitle.clone());
		}
		return new ArrayList<Title>(titles);
	}
	
	@Override
	public int getRowCount()
	{
		return titles.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int colIndex)
	{
		final Title title = titles.get(rowIndex);
		
		switch(colIndex)
		{
			case COLUMN_IDX_UID:
				return title.getUid();
			case COLUMN_IDX_ISBN:
				return title.getIsbn13();
			case COLUMN_IDX_NAME:
				return title.getName();
			case COLUMN_IDX_AUTHOR:
				return title.getAuthor();
			case COLUMN_IDX_MEDIAFILELINK:
				return title.hasMediaFileLink();
			case COLUMN_IDX_FILES:
				return title.getFiles();
			default:
				throw new IndexOutOfBoundsException("Invalid columnIndex: " + colIndex);
		}
	}
	
	/**
	 * Remove a title from the model
	 * @param remTitle The Title object to be removed
	 * @return true, if the object has been found and removed
	 */
	public boolean removeTitle(final Title remTitle)
	{
		int index = titles.indexOf(remTitle);
		return removeTitle(index);
	}
	
	/**
	 * Remove a title from the model based on the table's row index
	 * @param integer row index
	 * @return true, if the row index was valid and the title has been removed
	 */
	public boolean removeTitle(int row)
	{
		if(row < 0 || row > titles.size())
		{
			return false;
		}
		else
		{
			titles.remove(row);
			fireTableRowsDeleted(row, row);
			return true;
		}
	}
}