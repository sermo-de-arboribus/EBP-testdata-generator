package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JTextArea;

import testdatagen.model.Price;

/**
 * Listener for clearing the configured prices from a title configuration dialog
 */
public class ClearPricesFromTitleListener implements ActionListener
{
	private JTextArea pricesField;
	private Set<Price> pricesSet;

	/**
	 * Constructor
	 * @param pricesField The JTextArea displaying the currently configured prices
	 * @param pricesSet The Set representing the currently configured prices
	 */
	public ClearPricesFromTitleListener(final JTextArea pricesField, final Set<Price> pricesSet)
	{
		this.pricesField = pricesField;
		this.pricesSet = pricesSet;
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		if (evt.getActionCommand().equals("clear prices"))
		{
			pricesSet.clear();
			pricesField.setText("");
			pricesField.repaint();
		}
	}
}