package testdatagen.config;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationRegistry
{
	// At this stage of development we just give some configuration values as hard-coded literals here (see constructor)
	// This could be evolved into a more flexible configuration system later.
	
	private static ConfigurationRegistry registry;
	private Map<String, String> regMap;
	private Map<Locale, Map<String, String[]>> localeTextMap;
	
	private ConfigurationRegistry()
	{
		 regMap = new ConcurrentHashMap<String, String>();
		 
		 // add cover generation data to register
		 regMap.put("cover.maxWidth", "2400");
		 regMap.put("cover.minWidth", "300");
		 regMap.put("cover.minXYRatio", "1.28");
		 regMap.put("cover.maxXYRatio", "1.48");
		 regMap.put("cover.likelinessOfPDFCovers", "0.2");
		 regMap.put("file.tempFolder", "temp");
		 regMap.put("ssl.pathToCacerts", "R:\\git\\EBP-testdata-generator\\lib\\cacerts");
		 regMap.put("ssl.pathToCacerts", "R:\\git\\EBP-testdata-generator\\lib\\cacerts");
		 regMap.put("net.useProxy", "yes");
		 regMap.put("net.proxy.host", "proxy.knonet.de");
		 regMap.put("net.proxy.port", "8080");
		 
		 localeTextMap = new ConcurrentHashMap<Locale, Map<String, String[]>>();
		 Map<String, String[]> germanTextMap = new ConcurrentHashMap<String, String[]>();
		 Locale deLocale = new Locale("de");
		 localeTextMap.put(deLocale, germanTextMap);
		
		 putLocalizedText(deLocale, "authorBlurbTemplate", "{$authorname} wurde {$birthyear} in {$birthplace} geboren. Mit seinen {$bookgenre} über {$topic} eroberte sie oder er eine riesige Fangemeinde und wurde {$when} mit „{$booktitle}“ für {$prize} nominiert. Die Autorin oder der Autor lebt {$residence}.");
		 putLocalizedText(deLocale, "birthplace", new String[]{"Ransbach-Baumbach", "Bietigheim-Bissingen", "Pirna-Niedervogelgesang", "Kobern-Gondorf", "Treis-Karden", "Solingen-Ohligs", "Bernkastel-Kues", "Traben-Trarbach", "Moers", "Unna", "Dessau", "Dresden", "Mauritius", "Dnjepropetrowsk", "Jindřichův Hradec", "České Budějovice", "Falun", "Ouagadougou", "Shangri-La", "Madras", "Isfahan", "Kuşadası", "Horní Plana", "Wladiwostok", "Harbin", "einer Almhütte", "einem Flugzeug auf dem Weg von Chicago nach San Antonio", "Cartagena", "Guatemala", "Luanda"});
		 putLocalizedText(deLocale, "bookgenre", new String[]{"Romanen", "Gedichten", "Ratgebern", "Reiseführern", "Hörbüchern", "Computerprogrammen", "Spielen"});
		 putLocalizedText(deLocale, "topic", new String[]{"Oasen", "Schnittblumen", "Sonnuntergänge", "die Psychologie der Jahreszeiten", "fernöstliche Esstechniken", "das Reparieren von Kugelschreibern", "die Schatzkammer von Dagobert Duck", "die schönsten Zuflüsse von Saale und Unstrut", "die polnisch-deutsche Versöhnung", "die Beulwerte ausgesteifter Rechteckplatten"});
		 putLocalizedText(deLocale, "when", new String[]{"gestern", "letztes Jahr", "2014", "im Sommer"});
		 putLocalizedText(deLocale, "booktitle", new String[]{"100 Tage Heimat", "Wer den Taler sehr ehrt, ist des Mitleids nicht wert", "Der Zahlenteufel", "Barça - Die großen Jahre", "Dicke Beine trotz Diät", "The Big Lewanstki", "Garga und Gurgelstrozza", "Die Poesie der Asphaltbrüche"});
		 putLocalizedText(deLocale, "prize", new String[]{"die Financial-Times-Ehrennadel 2012", "den Wolfdietrich-Schnurre-Gedächtnispreis", "den Pervitinküchen-Meistertitel 2013", "den MXSW-Preis 2015", "die Virenschleuder 2011"});
		 putLocalizedText(deLocale, "residence", new String[]{"in einer WG mit Ellen Frauenknecht", "in einer Düne an der Nordsee", "tief im Wald, gleich neben Walder (sic!) Moers", "heute hier, morgen dort", "in Schneckenfischbach im Allgäu", "unter den Neckarstadion"});
	}
	
	public static synchronized ConfigurationRegistry getRegistry()
	{
		if(registry == null)
		{
			registry = new ConfigurationRegistry();
		}
		return registry;
	}
	
	public boolean getBooleanValue(String key)
	{
		String value = regMap.get(key);
		if(value == null) return false;
		if(value.equals("yes") | value.equals("y") | value.equals("j") | value.equals("ja")) return true;
		return false;
	}
	
	public double getDoubleValue(String key)
	{
		double value;
		try
		{
			value = Double.parseDouble(regMap.get(key));
		}
		catch(NumberFormatException nfe)
		{
			value = Double.MIN_VALUE;
		}
		return value;
	}
	
	public int getIntValue(String key)
	{
		int value;
		try
		{
			value = Integer.parseInt(regMap.get(key));
		}
		catch(NumberFormatException nfe)
		{
			value = Integer.MIN_VALUE;
		}
		return value;
	}
	
	public String[] getLocalizedText(Locale loc, String key)
	{
		Map<String, String[]> innerMap = localeTextMap.get(loc);
		return innerMap.get(key);
	}
	
	public String getString(String key)
	{
		return regMap.get(key);
	}
	
	/*
	 * Convenience function: If a single String is to be stored in the locale map, wrap it into an array of length 1
	 */
	public void putLocalizedText(Locale loc, String key, String value)
	{
		String[] sarr = new String[1];
		sarr[0] = value;
		putLocalizedText(loc, key, sarr);
	}
	
	public void putLocalizedText(Locale loc, String key, String[] sarr)
	{
		Map<String, String[]> innerMap = localeTextMap.get(loc);
		innerMap.put(key, sarr);
	}
}