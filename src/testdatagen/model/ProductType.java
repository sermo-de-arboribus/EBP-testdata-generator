package testdatagen.model;

/**
 * This enumeration lists all valid product types
 */
public enum ProductType
{
	PDF, EPUB, WMPDF, WMEPUB, NDPDF, NDEPUB, ZIP, iBOOK, NDMOBI, WMMOBI, AUDIO;
	
	/**
	 * Get the valid product types as a String array
	 * @return String[] with all valid product types
	 */
	public static String[] productTypeNames()
	{
		ProductType[] productTypes = values();
		String[] names = new String[productTypes.length];
		
		for(int i = 0; i < productTypes.length; i++)
		{
			names[i] = productTypes[i].name();
		}
		return names;
	}
}
