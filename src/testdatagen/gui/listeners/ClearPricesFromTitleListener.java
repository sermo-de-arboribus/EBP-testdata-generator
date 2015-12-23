package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JTextArea;

import testdatagen.model.Price;

public class ClearPricesFromTitleListener implements ActionListener
{
	private JTextArea pricesField;
	private Set<Price> pricesSet;
	
	public ClearPricesFromTitleListener(final JTextArea pricesField, final Set<Price> pricesSet)
	{
		this.pricesField = pricesField;
		this.pricesSet = pricesSet;
	}

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("clear subjects"))
		{
			pricesSet.clear();
			pricesField.setText("");
			pricesField.repaint();
		}
	}
}