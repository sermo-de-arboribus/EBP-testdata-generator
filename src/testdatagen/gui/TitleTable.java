package testdatagen.gui;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * This class represents the JTable that is used for displaying all titles within a scenario dialog.
 */
public class TitleTable extends JTable
{
	private static final long serialVersionUID = 2L;
	// preferred table column width
	private static final int[] COLUMN_WIDTH = {65, 65, 120, 120, 45, 400}; 
	
	/**
	 * Constructor
	 * @param tm The table model that represents the table's content data
	 */
	public TitleTable(final TableModel tm)
	{
		super(tm);
		TableColumnModel tcm = this.getColumnModel();
		// set column width
		for(int i = 0; i < COLUMN_WIDTH.length; i++)
		{
			tcm.getColumn(i).setPreferredWidth(COLUMN_WIDTH[i]);
		}
		// set line wrap and height-adjustment for the last column
		tcm.getColumn(COLUMN_WIDTH.length - 1).setCellRenderer(new TableCellLineWrapRenderer());
	}
}