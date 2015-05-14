package testdatagen.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
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
	
	public TitleTableModel(final List<Title> titles)
	{
		this.titles = new ArrayList<>(titles);
	}
	
	public void addTitle(Title newTitle)
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
	
	public List<Title> getListOfTitles()
	{
		// TODO: to make sure the internal list is immutable, return a deep copy of the listOfTitles
		// rather than the currently given flat one.
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
	
	public boolean removeTitle(Title remTitle)
	{
		int index = titles.indexOf(remTitle);
		return removeTitle(index);
	}
	
	public boolean removeTitle(int row)
	{
		if(row < 0)
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