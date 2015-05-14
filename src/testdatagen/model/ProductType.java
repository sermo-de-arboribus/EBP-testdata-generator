package testdatagen.model;

public enum ProductType
{
	PDF, EPUB, WMPDF, WMEPUB, NDPDF, NDEPUB, ZIP, iBOOK, NDMOBI, WMMOBI, AUDIO;
	
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
