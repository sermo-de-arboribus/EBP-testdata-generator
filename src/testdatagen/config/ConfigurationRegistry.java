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
		 regMap.put("currency.codes", "EUR GBP USD HKD CZK");
		 regMap.put("currency.country.EUR", "DE AT FR GR LU NL BE ES IT");
		 regMap.put("currency.country.USD", "US");
		 regMap.put("currency.country.GBP", "GB");
		 regMap.put("currency.country.HKD", "HK");
		 regMap.put("currency.country.CZK", "CZ");
		 regMap.put("currency.rate.USD", "1.1244");
		 regMap.put("currency.rate.GBP", "0.7352");
		 regMap.put("currency.rate.HKD", "8.7171");
		 regMap.put("currency.rate.CZK", "27.3654");
		 regMap.put("currency.rate.EUR", "1.0");
		 regMap.put("file.tempFolder", "temp");
		 regMap.put("ssl.pathToCacerts", "R:\\git\\EBP-testdata-generator\\lib\\cacerts");
		 regMap.put("ssl.pathToCacerts", "R:\\git\\EBP-testdata-generator\\lib\\cacerts");
		 regMap.put("net.useProxy", "yes");
		 regMap.put("net.proxy.host", "proxy.knonet.de");
		 regMap.put("net.proxy.port", "8080");
		 regMap.put("onix.WarengruppeCodes", "110 112 113 114 115 116 117 118 120 121 122 123 130 131 132 133 150 151 152 180 182 183 185 190 210 211 212 213 214 240 250 260 270 280 284 285 310 311 312 313 314 315 324 410 411 412 413 414 415 421 418 444 456 461 470 473 476 480 481 484 490 491 520 527 530 533 534 535 540 550 551 552 553 554 555 556 557 558 559 560 561 562 563 568 570 574 577 578 579 580 585 590 610 630 632 633 640 642 643 690 691 692 693 694 695 697 698 699 720 730 740 750 770 771 772 773 774 775 778 781 782 783 787 910 914 930 931 932 933 937 945 950 960 961 970");
		 regMap.put("onix.minNumberOfPriceNodes", "3");
		 regMap.put("onix.maxNumberOfPriceNodes", "12");
		 regMap.put("iso3166-1.countryCodes", "AD AE AF AG AI AL AM AN AO AQ AR AS AT AU AW AX AZ BA BE BF BG CA CH CM CN CR CU CV CY CZ DE DK EC EE EG FR GB GR GT HK HR HU JP LB LI LT LU LY MA MC MD ME MG MT NL PK PR PT PW RW SA SD SN SR TG TR TW VA YE ZW");
		 
		 localeTextMap = new ConcurrentHashMap<Locale, Map<String, String[]>>();
		 Map<String, String[]> germanTextMap = new ConcurrentHashMap<String, String[]>();
		 Locale deLocale = new Locale("de");
		 localeTextMap.put(deLocale, germanTextMap);
		
		 // values for the AuthorBlurbTemplate
		 putLocalizedText(deLocale, "authorBlurbTemplate", "{$authorname} wurde {$birthyear} in {$birthplace} geboren. Mit {$bookgenre} über {$topic} eroberte sie oder er eine riesige Fangemeinde und wurde {$when} mit „{$booktitle}“ für {$prize} nominiert. Die Autorin oder der Autor lebt {$residence}.");
		 putLocalizedText(deLocale, "birthplace", new String[]{"Ransbach-Baumbach", "Bietigheim-Bissingen", "Pirna-Niedervogelgesang", "Kobern-Gondorf", "Treis-Karden", "Solingen-Ohligs", "Bernkastel-Kues", "Traben-Trarbach", "Castrop-Rauxel", "Mettlach-Orscholz", "Moers", "Unna", "Dessau", "Dresden", "Mauritius", "Dnjepropetrowsk", "Jindřichův Hradec", "České Budějovice", "Falun", "Ouagadougou", "Shangri-La", "Madras", "Isfahan", "Kuşadası", "Horní Plana", "Wladiwostok", "Harbin", "einer Almhütte", "einem Flugzeug auf dem Weg von Chicago nach San Antonio", "Cartagena", "Guatemala", "Luanda", "einem Bergsteigerzelt auf dem Kilimandscharo", "Pleurtuit in der Bretagne", "der Nähe des Polarkreises bei Sonnenaufgang"});
		 putLocalizedText(deLocale, "bookgenre", new String[]{"Romanen", "Gedichten", "Ratgebern", "Reiseführern", "Hörbüchern", "Computerprogrammen", "Spielen", "Büchern", "E-Mails", "opulenten Booklets"});
		 putLocalizedText(deLocale, "topic", new String[]{"Oasen", "Schnittblumen", "Sonnuntergänge", "die Psychologie der Jahreszeiten", "fernöstliche Esstechniken", "das Reparieren von Kugelschreibern", "die Schatzkammer von Dagobert Duck", "die schönsten Zuflüsse von Saale und Unstrut", "die polnisch-deutsche Versöhnung", "die Beulwerte ausgesteifter Rechteckplatten", "den Anbau von Zitruspflanzen in Südostanatolien", "Veganismus", "meteorologische Phänomene", "die Pferdemast"});
		 putLocalizedText(deLocale, "when", new String[]{"gestern", "letztes Jahr", "2014", "im Sommer", "zur Sternzeit 32634,9"});
		 putLocalizedText(deLocale, "booktitle", new String[]{"100 Tage Heimat", "Wer den Taler sehr ehrt, ist des Mitleids nicht wert", "Der Zahlenteufel", "Barça - Die großen Jahre", "Dicke Beine trotz Diät", "The Big Lewanstki", "Garga und Gurgelstrozza", "Die Poesie der Asphaltbrüche", "Adam und Eva treffen Harold und Maude", "Eine einzelne, von der aufgehenden Sonne rosa gefärbte Quellwolke", "Die wahre Geschichte des B Q", "Affentheuerlich naupengeheuerliche Textilklitterung"});
		 putLocalizedText(deLocale, "prize", new String[]{"die Financial-Times-Ehrennadel 2012", "den Wolfdietrich-Schnurre-Gedächtnispreis", "den Pervitinküchen-Chefkochtitel 2013", "den MXSW-Preis 2015", "die Virenschleuder 2011", "die Pommesgabel der Wackengeschädigten 2014", "Kachelfrau-Preis 2015"});
		 putLocalizedText(deLocale, "residence", new String[]{"in einer WG mit Ellen Frauenknecht", "in einer Düne an der Nordsee", "tief im Wald, gleich neben Walder (sic!) Moers", "heute hier, morgen dort", "in Schneckenfischbach im Allgäu", "unter dem Neckarstadion", "im Beutel eines Känguruhs", "nahe der Stratosphäre in einem hohen Gebirge", "auf einem Hindernisparcours in Südfrankreich"});
		 
		 // values for the TitleBlurbTemplate
		 putLocalizedText(deLocale, "titleBlurbTemplate", "Dieses Buch ist {$bam} zwischen {$tbt-adj1} {$tbt-compound1}{$tbt-compound2} und {$tbt-adj2} {$tbt-compound1}{$tbt-compound2}. {$tbt-adj3} {$tbt-imperative}!");
		 putLocalizedText(deLocale, "bam", new String[]{"der Brückenschlag", "der Lückenfüller", "der Missing Link", "ein unbeschreibliches Phänomen", "Augenwischerei", "ein Knaller"});
		 putLocalizedText(deLocale, "tbt-adj1", new String[]{"wertorientierter", "unerschütterlicher", "erhabener", "tiefer", "nationaler", "historischer", "integrierter", "kreativer", "permanenter", "echter", "konstruktiver", "emanizpatorischer", "ambivalenter", "qualifizierter"});
		 putLocalizedText(deLocale, "tbt-adj2", new String[]{"permanenter", "systematisierter", "funktionaler", "integrierter", "kreativer", "bedenklicher", "machtvoller", "dieser unserer", "freudiger", "abendländischer", "morgenländischer", "nordamerikanischer", "georgischer", "gregorianischer", "altruistischer", "evolutionsbiologischer"});
		 putLocalizedText(deLocale, "tbt-adj3", new String[]{"Unbedingt", "Auf jeden Fall", "Unmissverständlich", "Absolut", "Heftig"});
		 putLocalizedText(deLocale, "tbt-compound1", new String[]{"Fluktuations-", "Beziehungs-", "Innovations-", "Koalitions-", "Motivations-", "Aktions-", "Identifikations-", "Kommunikations-", "Interpretations-", "Erinnerungs-", "Seins-", "Wesens-", "Zukunfts-", "Java-"});
		 putLocalizedText(deLocale, "tbt-compound2", new String[]{"Gemeinschaft", "Gläubigkeit", "Verstrickung", "Gewißheit", "Verpflichtung", "Bewahrung", "Aussage", "Phase", "Potenz", "Flexibilität", "Akzeleration", "Defenestration", "Problematik", "Tendenz", "Konzeption", "Präferenz"});
		 putLocalizedText(deLocale, "tbt-imperative", new String[]{"lesen", "anschauen", "herunterladen", "begrüßen", "gebrauchen", "kaufen", "verschlingen"});
		 
		 // value for the ONIXTitlePageTemplate
		 putLocalizedText(deLocale, "ONIXTitlePageTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Titelseite {$title}</title></head><body style=\"text-align:center\"><h1>{$title}</h1><div><br/></div><h2>von {$author}</h2><div><br/></div><div>erschienen im</div><div><br/></div><div><br/></div><div>{$publishername}</div><div>{$publisherlocation}</div><div>{$year}</div><div><br/></div><div>ISBN {$isbn}</div></body></html>");
		 putLocalizedText(deLocale, "publisherlocation", new String[]{"Stuttgart", "Stuttgart-Vaihingen", "Stuttgart-Möhringen", "Stuttgart-Kaltental"});
		 putLocalizedText(deLocale, "publishername", new String[]{"KNV IT-E-Books-Verlag", "Koch, Neff & Volckmar IT-E-Books-Verlag", "KNO DiVA & KNV IT-E-Books-Verlag"});
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