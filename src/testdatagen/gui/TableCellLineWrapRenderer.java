package testdatagen.gui;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * The TableCellLineWrapRenderer class is responsible for text wrapping and optimal cell height display
 * in the program's JTable cells. 
 */
public class TableCellLineWrapRenderer extends JTextArea implements TableCellRenderer, Serializable
{

	private static final long serialVersionUID = 2L;

	/**
	 * Constructor, configures wrapping styles
	 */
	public TableCellLineWrapRenderer()
	{
		setLineWrap(true);
		setWrapStyleWord(true);
	}
	
	/**
	 * Method for getting the table renderer component, which ensures the table cell to have the required 
	 * height to display all the text in the cell.
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		setText(value.toString());
		setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
		if(table.getRowHeight(row) != getPreferredSize().height)
		{
			table.setRowHeight(row, getPreferredSize().height);
		}
		return this;
	}
}
