package testdatagen.gui;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class TableCellLineWrapRenderer extends JTextArea implements TableCellRenderer, Serializable
	{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableCellLineWrapRenderer()
	{
		setLineWrap(true);
		setWrapStyleWord(true);
	}
	
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
