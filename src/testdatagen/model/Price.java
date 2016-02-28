package testdatagen.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Model class to represent price information; based on the Onix price model
 */
public class Price implements Serializable
{
	private static final long serialVersionUID = 2L;
	private String type, amount, currency, country;
	
	/**
	 * Constructor
	 * @param type String for price type
	 * @param amount String for price amount
	 * @param currency String for currency code
	 * @param country String for ISO country code
	 */
	public Price(final String type, final String amount, final String currency, final String country)
	{
		this.type = type;
		this.amount = amount;
		this.currency = currency;
		this.country = country;
	}

	/**
	 * Convenience constructor which sets the amount to a default value
	 * @param type String for price type
	 * @param currency String for currency code
	 * @param country String for ISO country code
	 */
	public Price(final String type, final String currency, final String country)
	{
		this(type, "9.90", currency, country);
	}
	
	/**
	 * Add a price to a HashMap. (This HashMap can be used as an argument for an OnixPriceBuilder object.)
	 * @param argumentsMap The map to add the price to.
	 */
	public void addPriceArguments(final HashMap<String, String> argumentsMap)
	{
		argumentsMap.put("pricetype", type);
		argumentsMap.put("priceamount", amount);
		argumentsMap.put("currencycode", currency);
		argumentsMap.put("countriesincluded", country);
	}
	
	/**
	 * Clone the price object
	 * @return A new price object with the same values as the original one
	 */
	public Price clone()
	{
		return new Price(type, amount, currency, country);
	}

	/**
	 * Function to compare two price objects
	 * @param otherPrice price to compare
	 * @return boolean
	 */
	public boolean equals(final Price otherPrice)
	{
		return type.equals(otherPrice.type) && country.equals(otherPrice.country) && currency.equals(otherPrice.currency);
	}

	/**
	 * Function to compare two price objects
	 */
	@Override
	public boolean equals(final Object otherObject)
	{
		if(otherObject != null && !(otherObject instanceof Price))
			return false;
		
		return this.equals((Price) otherObject);
	}
	
	/**
	 * Get the country code that this price is meant for
	 * @return String with ISO country code
	 */
	public String getPriceCountry()
	{
		return country;
	}

	/**
	 * Get the Onix price type code associated with this price
	 * @return String with price type code
	 */
	public String getPriceType()
	{
		return type;
	}
	
	@Override
	public int hashCode()
	{
		String concatenatedValues = type + amount + currency + country;
		return concatenatedValues.hashCode();
	}
	
	@Override
	public String toString()
	{
		return type + "-" + country + ": " + amount + " " + currency;
	}
}