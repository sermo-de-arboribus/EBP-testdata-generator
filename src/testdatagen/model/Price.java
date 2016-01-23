package testdatagen.model;

import java.io.Serializable;
import java.util.HashMap;

public class Price implements Serializable
{
	private static final long serialVersionUID = 2L;
	String type, amount, currency, country;
	
	public Price(String type, String amount, String currency, String country)
	{
		this.type = type;
		this.amount = amount;
		this.currency = currency;
		this.country = country;
	}
	
	public Price(String type, String currency, String country)
	{
		this.type = type;
		this.amount = "9.90";
		this.currency = currency;
		this.country = country;
	}
	
	public void addPriceArguments(HashMap<String, String> argumentsMap)
	{
		argumentsMap.put("pricetype", type);
		argumentsMap.put("priceamount", amount);
		argumentsMap.put("currencycode", currency);
		argumentsMap.put("countriesincluded", country);
	}
	
	public Price clone()
	{
		return new Price(type, amount, currency, country);
	}
	
	public boolean equals(Price otherPrice)
	{
		return type.equals(otherPrice.type) && country.equals(otherPrice.country) && currency.equals(otherPrice.currency);
	}
	
	@Override
	public boolean equals(Object otherObject)
	{
		if(otherObject != null && !(otherObject instanceof Price))
			return false;
		
		return this.equals((Price) otherObject);
	}
	
	public String getPriceCountry()
	{
		return country;
	}
	
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