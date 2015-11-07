package testdatagen.model;

import java.util.HashMap;

public class Price
{
	String type, amount, currency, country;
	
	public Price(String type, String amount, String currency, String country)
	{
		this.type = type;
		this.amount = amount;
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
		return type.equals(otherPrice.type) && country.equals(otherPrice.country);
	}
	
	public String getPriceCountry()
	{
		return country;
	}
	
	public String getPriceType()
	{
		return type;
	}
	
	public int hashCode()
	{
		String concatenatedValues = type + amount + currency + country;
		return concatenatedValues.hashCode();
	}
}