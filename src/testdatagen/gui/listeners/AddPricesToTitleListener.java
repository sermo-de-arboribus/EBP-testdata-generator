package testdatagen.gui.listeners;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import testdatagen.model.Price;

public class AddPricesToTitleListener implements ActionListener
{
	private JTextArea pricesField;
	private Set<Price> pricesSet;
	
	public AddPricesToTitleListener(final JTextArea pricesField, final Set<Price> pricesSet)
	{
		this.pricesField = pricesField;
		this.pricesSet = pricesSet;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getActionCommand().equals("open prices dialog"))
		{
			new PricesDialog(this);
		}
		else if(evt.getActionCommand().equals("price submitted"))
		{
			JComponent pricesDialog = (JComponent) evt.getSource();
			Container pricesContainer = pricesDialog.getTopLevelAncestor();
			PricesDialog submittedDialog = (PricesDialog) pricesContainer;
			
			String priceTypeCode = submittedDialog.getPriceTypeCode();
			String countryCode = submittedDialog.getCountryCode();
			String currencyCode = submittedDialog.getCurrencyCode();
			
			Price newPrice = new Price(priceTypeCode, currencyCode, countryCode);
			pricesSet.add(newPrice);
			
			StringBuffer labelText = new StringBuffer("");
			Iterator<Price> iterator = pricesSet.iterator();
			while(iterator.hasNext())
			{
				labelText.append(iterator.next().toString());
				if(iterator.hasNext())
				{
					labelText.append(", ");
				}
			}
			pricesField.setText(labelText.toString());
			pricesContainer.setVisible(false);
		}
	}
}

class PricesDialog extends JDialog
{
	private static final long serialVersionUID = 2L;
	private JTextField currencyCode, priceTypeCode, countryCode;
	
	public PricesDialog(AddPricesToTitleListener actionListener)
	{
		this.setTitle("Add a price");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		// centerPanel is for entering data
		JPanel centerPanel = new JPanel(new GridLayout(0,2));
		// southPanel is for the submit button
		JPanel southPanel = new JPanel(new FlowLayout());
		
		JLabel priceTypeLabel = new JLabel("Price Type");
		JLabel currencyCodeLabel = new JLabel("Currency Code");
		JLabel countryCodeLabel = new JLabel("Country Code");
		
		currencyCode = new JTextField("");
		currencyCode.setName("currencycode");
		currencyCode.setColumns(8);
		priceTypeCode = new JTextField("");
		priceTypeCode.setName("pricetypecode");
		priceTypeCode.setColumns(8);
		countryCode = new JTextField("");
		countryCode.setName("countrycode");
		countryCode.setColumns(8);
		
		JButton submitButton = new JButton("submit");
		submitButton.setActionCommand("price submitted");
		submitButton.addActionListener(actionListener);
		
		priceTypeLabel.setLabelFor(priceTypeCode);
		countryCodeLabel.setLabelFor(countryCode);
		currencyCodeLabel.setLabelFor(currencyCode);
		
		centerPanel.add(priceTypeLabel);
		centerPanel.add(priceTypeCode);
		centerPanel.add(countryCodeLabel);
		centerPanel.add(countryCode);
		centerPanel.add(currencyCodeLabel);
		centerPanel.add(currencyCode);
		
		southPanel.add(submitButton);
		
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		setLocation(200, 200);
		setSize(500, 200);
		setVisible(true);
	}
	
	public String getCurrencyCode()
	{
		return currencyCode.getText();
	}
	
	public String getPriceTypeCode()
	{
		return priceTypeCode.getText();
	}
	
	public String getCountryCode()
	{
		return countryCode.getText();
	}
}