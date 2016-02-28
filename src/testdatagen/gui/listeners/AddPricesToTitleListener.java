package testdatagen.gui.listeners;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import testdatagen.model.Price;

/**
 * This ActionListener class handles clicks to the "Add price" and "Submit price" buttons in the TitleForm
 */
public class AddPricesToTitleListener implements ActionListener
{
	private JTextArea pricesField;
	private Set<Price> pricesSet;
	
	/**
	 * Constructor
	 * @param pricesField The JTextArea that displays the currently configured prices
	 * @param pricesSet The Set that holds the currently configured prices
	 */
	public AddPricesToTitleListener(final JTextArea pricesField, final Set<Price> pricesSet)
	{
		this.pricesField = pricesField;
		this.pricesSet = pricesSet;
	}
	
	@Override
	public void actionPerformed(final ActionEvent evt)
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

/**
 * Helper class for displaying a modal dialog window for price configuration
 */
class PricesDialog extends JDialog
{
	private static final long serialVersionUID = 2L;
	private JTextField currencyCode, priceTypeCode, countryCode;
	
	/**
	 * Constructor. Configures and displays the modal price configuration dialog
	 * @param actionListener The AddPricesToTitleListener that handles clicks to the "Submit" button
	 */
	public PricesDialog(final AddPricesToTitleListener actionListener)
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
	
	/**
	 * Return the newly entered Onix currency code 
	 * @return String representing Onix currency code
	 */
	public String getCurrencyCode()
	{
		return currencyCode.getText();
	}
	
	/**
	 * Return the newly entered Onix price type code
	 * @return String representing the Onix price type code
	 */
	public String getPriceTypeCode()
	{
		return priceTypeCode.getText();
	}

	/**
	 * Return the newly entered Onix (= ISO) country code
	 * @return String with ISO country code
	 */
	public String getCountryCode()
	{
		return countryCode.getText();
	}
}