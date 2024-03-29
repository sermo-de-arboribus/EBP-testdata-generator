package testdatagen.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * At this stage of development we just give some configuration values as hard-coded literals here (see constructor)
 * This could be evolved into a more flexible configuration system later.
 * The class is designed as a singleton.
 * 
 * The registry maintains two different data structures for key-value based look-up of configuration values.
 * One structure holds single strings, integers or double values.
 * The other structure can hold localized texts, although at the current stage only the DE locale is used.
 * Keys in the locale registry point to a string array and can therefore return more than one strings. This 
 * feature is mainly used for the template system, where the templates are randomly filled with text values,
 * that means one string out of the string array is randomly picked.
 */
public class ConfigurationRegistry
{
	private static ConfigurationRegistry registry;
	private Map<String, String> regMap;
	private Map<Locale, Map<String, String[]>> localeTextMap;
	private Map<Integer, Map<String, String>> onixCodeListMap;
	
	private ConfigurationRegistry()
	{
		 regMap = new ConcurrentHashMap<String, String>();
		 
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
		 regMap.put("net.useProxy", "no");
		 regMap.put("net.proxy.host", "proxy.knonet.de");
		 regMap.put("net.proxy.port", "8080");
		 regMap.put("onix.WarengruppeCodes", "110 112 113 114 115 116 117 118 120 121 122 123 130 131 132 133 150 151 152 180 182 183 185 190 210 211 212 213 214 240 250 260 270 280 284 285 310 311 312 313 314 315 324 410 411 412 413 414 415 421 418 444 456 461 470 473 476 480 481 484 490 491 520 527 530 533 534 535 540 550 551 552 553 554 555 556 557 558 559 560 561 562 563 568 570 574 577 578 579 580 585 590 610 630 632 633 640 642 643 690 691 692 693 694 695 697 698 699 720 730 740 750 770 771 772 773 774 775 778 781 782 783 787 910 914 930 931 932 933 937 945 950 960 961 970");
		 regMap.put("onix.minNumberOfPriceNodes", "3");
		 regMap.put("onix.maxNumberOfPriceNodes", "12");
		 regMap.put("iso3166-1.countryCodes", "AD AE AF AG AI AL AM AN AO AQ AR AS AT AU AW AX AZ BA BE BF BG CA CH CM CN CR CU CV CY CZ DE DK EC EE EG FR GB GR GT HK HR HU JP LB LI LT LU LY MA MC MD ME MG MT NL PK PR PT PW RW SA SD SN SR TG TR TW VA YE ZW");
		 regMap.put("kindleGenPath", "");
		 
		 Locale deLocale = new Locale("de");
		 Locale enLocale = new Locale("en");
		 Locale frLocale = new Locale("fr");
		 Locale csLocale = new Locale("cs");
		 Locale zhLocale = new Locale("zh");
		 
		 localeTextMap = new ConcurrentHashMap<Locale, Map<String, String[]>>();
		 Map<String, String[]> germanTextMap = new ConcurrentHashMap<String, String[]>();
		 Map<String, String[]> englishTextMap = new ConcurrentHashMap<String, String[]>();
		 Map<String, String[]> frenchTextMap = new ConcurrentHashMap<String, String[]>();
		 Map<String, String[]> czechTextMap = new ConcurrentHashMap<String, String[]>();
		 Map<String, String[]> chineseTextMap = new ConcurrentHashMap<String, String[]>();
		 
		 localeTextMap.put(deLocale, germanTextMap);
		 localeTextMap.put(enLocale, englishTextMap);
		 localeTextMap.put(frLocale, frenchTextMap);
		 localeTextMap.put(csLocale, czechTextMap);
		 localeTextMap.put(zhLocale, chineseTextMap);
		 
		 // values for the AuthorBlurbTemplate
		 putLocalizedText(deLocale, "authorBlurbTemplate", "{$authorname} wurde {$birthyear} in {$birthplace} geboren. Mit {$bookgenre} über {$topic} eroberte sie oder er eine riesige Fangemeinde und wurde {$when} mit „{$booktitle}“ für {$prize} nominiert. Die Autorin oder der Autor lebt {$residence}.");
		 putLocalizedText(deLocale, "birthplace", new String[]{"Ransbach-Baumbach", "Bietigheim-Bissingen", "Pirna-Niedervogelgesang", "Kobern-Gondorf", "Treis-Karden", "Solingen-Ohligs", "Bernkastel-Kues", "Traben-Trarbach", "Castrop-Rauxel", "Mettlach-Orscholz", "Leinfelden-Echterdingen", "Villingen-Schwenningen", "Albstadt-Ebingen", "Filderstadt-Bernhausen", "Moers", "Unna", "Dessau", "Dresden", "Mauritius", "Dnjepropetrowsk", "Jindřichův Hradec", "České Budějovice", "Falun", "Ouagadougou", "Shangri-La", "Madras", "Isfahan", "Kuşadası", "Horní Plana", "Wladiwostok", "Harbin", "einer Almhütte", "einem Flugzeug auf dem Weg von Chicago nach San Antonio", "Cartagena", "Guatemala", "Luanda", "einem Bergsteigerzelt auf dem Kilimandscharo", "Pleurtuit in der Bretagne", "der Nähe des Polarkreises bei Sonnenaufgang", "in der Wortspielhölle", "in Metapontion", "bei Hempels unterm Sofa"});
		 putLocalizedText(deLocale, "bookgenre", new String[]{"Romanen", "Gedichten", "Ratgebern", "Reiseführern", "Hörbüchern", "Computerprogrammen", "Spielen", "Büchern", "E-Mails", "opulenten Booklets", "Coffee-Table-Büchern", "Folianten", "Schriftrollen", "Manuskripten", "Essays", "Artikeln", "Aufsätzen", "Traktaten", "Reflexionen", "Klageliedern", "Panegyriken", "Palimpsesten", "Rocksongs"});
		 putLocalizedText(deLocale, "topic", new String[]{"Oasen", "Schnittblumen", "Sonnuntergänge", "die Psychologie der Jahreszeiten", "fernöstliche Esstechniken", "das Reparieren von Kugelschreibern", "die Schatzkammer von Dagobert Duck", "die schönsten Zuflüsse von Saale und Unstrut", "die polnisch-deutsche Versöhnung", "die Beulwerte ausgesteifter Rechteckplatten", "den Anbau von Zitruspflanzen in Südostanatolien", "Veganismus", "meteorologische Phänomene", "die Pferdemast", "den Lebensraum Wald", "Propellerflugzeuge", "das Geweltetwordensein", "esoterische Anthropologie", "forstwirtschaftlich informierte Pilzkunde"});
		 putLocalizedText(deLocale, "when", new String[]{"gestern", "letztes Jahr", "2014", "im Sommer", "zur Sternzeit 32634,9", "neulich", "zurück in der Zukunft", "nie", "1998", "am Sankt-Nimmerleins-Tag"});
		 putLocalizedText(deLocale, "booktitle", new String[]{"100 Tage Heimat", "Wer den Taler sehr ehrt, ist des Mitleids nicht wert", "Der Zahlenteufel", "Barça - Die großen Jahre", "Dicke Lippe trotz Diät", "The Big Lewanstki", "Garga und Gurgelstrozza", "Die Poesie der Asphaltbrüche", "Adam und Eva treffen Harold und Maude", "Eine einzelne, von der aufgehenden Sonne rosa gefärbte Quellwolke", "Die wahre Geschichte des B Q", "Affentheuerlich naupengeheuerliche Textilklitterung", "Leidenschaftliche Naturerkundungen mit Hacke und Schaufel", "Die Kybernetik der Herrschaftslosigkeit", "Denn die Menschen singen nicht, sie lesen", "Tristan Schande", "Alemannische Drehwurst-Rezepte", "Fäkalkitsch", "Nicht immer, aber manchmal"});
		 putLocalizedText(deLocale, "prize", new String[]{"die Financial-Times-Ehrennadel 2012", "den Wolfdietrich-Schnurre-Gedächtnispreis", "den Pervitinküchen-Chefkochtitel 2013", "den MXSW-Preis 2015", "die Virenschleuder 2011", "die Pommesgabel der Wackengeschädigten 2014", "den Kachelfrau-Preis 2015", "den Johann-Gottfried-Säumnis-Preis 1998", "den Freddy-Schillert-Preis", "die Helmut-Schmidt-Medaille 2015", "den Fußball-Literatur-Preis 1974", "den Ritterorden der südwestdeutschen Rüstungsbauergilde", "den Schiftungsorden für Abbundspezialisten", "die Ramanujan-Medaille für Verdienste in der Popularisierung buchhalterischer Fachkenntnisse"});
		 putLocalizedText(deLocale, "residence", new String[]{"in einer WG mit Ellen Frauenknecht", "in einer Düne an der Nordsee", "tief im Wald, gleich neben Walder (sic!) Moers", "heute hier, morgen dort", "in Schneckenfischbach im Allgäu", "unter dem Neckarstadion", "im Beutel eines Känguruhs", "nahe der Stratosphäre in einem hohen Gebirge", "auf einem Hindernisparcours in Südfrankreich", "in einer Tonne, welche früher von Diogenes von Sinope bewohnt worden war", "auf dem Speicher von Fleischer Clapet", "mit Freundin Lena und Husky Aiko in einem Dorf im Tegernseer Tal", "als Taxifahrer, Anlageberater, Nachtportier und Jurist in einem indischen Ashram", "als einer der bekanntesten Naturheilmediziner der USA in Albuquerque", "im Epilimnion", "in einem palmblattgedeckten Häuschen an einer Biegung des Irawaddy", "im Wohnwagen von Jogi Löw", "als Untermieter von Markus Lanz in Hohenschönhausen", "in einem wohnlich eingerichteten Hügelgrab im Sauerland", "dort, wo die Postfrau drei mal klingelt"});
		 
		 // values for the (short) TitleBlurbTemplate
		 putLocalizedText(deLocale, "titleBlurbTemplate", "Dieses Buch ist {$bam} zwischen {$tbt-adj1} {$tbt-compound1}{$tbt-compound2} und {$tbt-adj2} {$tbt-compound1}{$tbt-compound2}. {$tbt-adj3} {$tbt-imperative}!");
		 putLocalizedText(deLocale, "bam", new String[]{"der Brückenschlag", "der Lückenfüller", "der Missing Link", "ein unbeschreibliches Phänomen", "Augenwischerei", "ein Knaller", "das Entfant Terrible", "wie eine Rakete", "Hasendreck", "eine Sensation", "wie eine Flaschenpost"});
		 putLocalizedText(deLocale, "tbt-adj1", new String[]{"wertorientierter", "unerschütterlicher", "erhabener", "tiefer", "nationaler", "historischer", "integrierter", "kreativer", "permanenter", "echter", "konstruktiver", "emanzipatorischer", "ambivalenter", "qualifizierter", "infantiler", "wohl erforschter", "hohenlohischer", "erstaunlicher", "tabellarischer"});
		 putLocalizedText(deLocale, "tbt-adj2", new String[]{"permanenter", "systematisierter", "funktionaler", "integrierter", "kreativer", "bedenklicher", "machtvoller", "dieser unserer", "freudiger", "abendländischer", "morgenländischer", "nordamerikanischer", "georgischer", "gregorianischer", "altruistischer", "evolutionsbiologischer", "spekulativer", "reformatorischer", "performanter", "wohlersonnener"});
		 putLocalizedText(deLocale, "tbt-adj3", new String[]{"Unbedingt", "Auf jeden Fall", "Unmissverständlich", "Absolut", "Heftig", "Zwingend", "Krass", "Muss man", "Soll man", "Aber hallo:", "Mein lieber Scholli: "});
		 putLocalizedText(deLocale, "tbt-compound1", new String[]{"Fluktuations-", "Beziehungs-", "Innovations-", "Koalitions-", "Motivations-", "Aktions-", "Identifikations-", "Kommunikations-", "Interpretations-", "Erinnerungs-", "Seins-", "Wesens-", "Zukunfts-", "Java-", "Hunde-", "Patriarchats-", "Handels-", "Matriarchats-", "Informatik-", "Weihnachts-", "Daseins-"});
		 putLocalizedText(deLocale, "tbt-compound2", new String[]{"Gemeinschaft", "Gläubigkeit", "Verstrickung", "Gewißheit", "Verpflichtung", "Bewahrung", "Aussage", "Phase", "Potenz", "Flexibilität", "Akzeleration", "Defenestration", "Problematik", "Tendenz", "Konzeption", "Präferenz", "Infantilität", "Kynologie", "Testabdeckung", "Herzensergießung", "Batchprogrammierung"});
		 putLocalizedText(deLocale, "tbt-imperative", new String[]{"lesen", "anschauen", "herunterladen", "begrüßen", "gebrauchen", "kaufen", "verschlingen", "reinschnuppern", "austesten", "abchecken", "schmökern"});
		 
		 // values for the medium and long TitleBlurbTemplate
		 putLocalizedText(deLocale, "longTitleBlurbTemplate", "Die {$female-protagonist} ist {$mtbt-adj1}, {$mtbt-adj2} und {$mtbt-adj3}. Ihrem {$partner}, für den sie {$emotion1}, aber nicht {$emotion2} empfindet, bleibt sie über seinen Tod hinaus treu, obwohl sie in {$mtbt-adj4} {$emotion3} für einen {$lover} entbrannt ist. Sie widersteht dessen hartnäckigem {$courting}, denn umgeben von den {$surrounding} {$location} strebt sie nach {$striving-for} – und fasst einen unerhörten Entschluss … ");
		 putLocalizedText(deLocale, "mediumTitleBlurbTemplate", "Die {$female-protagonist} ist {$mtbt-adj1} und {$mtbt-adj3}. Ihrem {$partner} bleibt sie über seinen Tod hinaus treu, obwohl sie in {$emotion3} für einen {$lover} entbrannt ist. Sie widersteht – und fasst einen unerhörten Entschluss … ");
		 putLocalizedText(deLocale, "female-protagonist", new String[]{"Prinzessin von Braunschweig", "Prinzessin von Hintertupfingen", "Prinzessin auf der Erbse", "erfolgsverwöhnte Tennisspielerin Klara", "Unterschicht-Angehörige Denise", "Boulevardblatt-Journalistin Karla", "päpstliche Kammerzofe Diana", "graue Maus", "Lauretta, die Gräfin von Sponheim", "Verlagslektorin Frau Himmelweit", "Zimmerfrau Josefine", "Softwareentwicklerin Lina Torvalds"});
		 putLocalizedText(deLocale, "mtbt-adj1", new String[]{"jung", "blutjung", "immer noch jung", "unerfahren", "abgebrüht", "schüchtern", "anschmiegsam", "abweisend", "vorsichtig"});
		 putLocalizedText(deLocale, "mtbt-adj2", new String[]{"schön", "hübsch", "intelligent", "aufgeweckt", "aufbrausend", "sozial", "umsichtig", "skeptisch", "sehnsüchtig"});
		 putLocalizedText(deLocale, "mtbt-adj3", new String[]{"wohlhabend", "reich", "klug", "selbstbewusst", "komplexbehaftet", "starrsinnig", "starrköpfig", "begütert", "verarmt", "schlau"});
		 putLocalizedText(deLocale, "mtbt-adj4", new String[]{"leidenschaftlicher", "bedingungsloser", "kopfloser", "außergewöhnlicher", "wahnsinniger", "rücksichtsloser", "blinder", "dramatischer"});
		 putLocalizedText(deLocale, "partner", new String[]{"Ehemann", "Verlobten", "Schoßhund", "Lieblingskater", "Gemahl", "Bettgenossen", "Teddybär", "Lebensabschnittsgefährten", "Sugardaddy"});
		 putLocalizedText(deLocale, "emotion1", new String[]{"Sympathie", "Zuneigung", "Mitgefühl", "Mitleid", "Zärtlichkeit", "Hingezogenheit", "Gleichgesinntheit", "Trunkenheit"});
		 putLocalizedText(deLocale, "emotion2", new String[]{"Liebe", "tiefe Zuneigung", "innige Verbundenheit", "eheliche Gefühle", "ewige Treue"});
		 putLocalizedText(deLocale, "emotion3", new String[]{"Liebe", "Zuneigung", "Verbundenheit", "Haßliebe", "Geschmeidigkeit", "Verliebtheit", "Begierde", "Kopflosigkeit"});
		 putLocalizedText(deLocale, "lover", new String[]{"anderen Mann", "nordischen Gott", "wahren Adonis", "verschrumpelten Alten", "schönen Grünschnabel", "Herzensbrecher", "schüchternen Fleischereifachverkäufer", "Hollywoodstar", "Bollywoodstar", "Hipster mit Bart", "draufgängerischen Buchhalter", "leidenschaftlichen Standardtänzer", "einfühlsamen Fagottisten"});
		 putLocalizedText(deLocale, "courting", new String[]{"Werben", "Buhlen", "Drängen", "Zureden", "Einschmeicheln", "Flirten"});
		 putLocalizedText(deLocale, "surrounding", new String[]{"Intrigen und Eitelkeiten", "Kabalen und Lieben", "Dekadenzen", "Sphärenklängen", "dreckigen Tricks", "Ungeheuerlichkeiten", "Spannungen", "Erziehungsbestrebungen", "Tumulten", "Verfallserscheinungen"});
		 putLocalizedText(deLocale, "location", new String[]{"des französischen Hofes", "des römischen Palastes", "der deutschen Gesellschaft ihrer Zeit", "der Bourgeoisie", "des Lumpenproletariats", "der Mehrheitsgesellschaft", "der Leitkultur", "des rechten Rands", "der toleranten Demokratie", "der Oberschicht"});
		 putLocalizedText(deLocale, "striving-for", new String[]{"einer anderen Art von Glück", "der ultimativen Erkenntnis", "Erfüllung weniger greifbarer Bedürfnisse", "subtilster Rache", "Erdbeereis bis zum Abwinken", "dem Weltfrieden", "ungehemmter Völlerei", "der Rettung zerkratzter Klos vor rauhen Bürsten", "dem Betriebsratsvorsitz"});
		 
		 // values for SeriesTemplate
		 putLocalizedText(deLocale, "seriesTemplate", "{$series-subject1}-{$series-subject2}-{$series-word}");
		 putLocalizedText(deLocale, "series-subject1", new String[]{"Hafenkanten", "Jodel", "Rheinschiffahrts", "Blumengießer", "Glocken", "Nachtwächter", "Turmwärter", "Meuchelpuffer", "Wagnis", "Schimmer", "Maus", "Wüstlings", "Mordbuben", "Analogkäse", "Gülleorgel", "Posaunen", "Diplom", "Hoch"});
		 putLocalizedText(deLocale, "series-subject2", new String[]{"Ohnmacht", "Kummer", "Kulturschock", "Speeddating", "Silberdistel", "Maulwurfsbarbie", "Spaziergang", "Gemüse", "Lagebeurteilung", "Legendenbildung", "Bergsattel", "Demut", "Mannigfaltigkeit", "Schwungsucht", "Orientalismus", "Geschenk", "Nasen"});
		 putLocalizedText(deLocale, "series-word", new String[]{"Reihe", "Bildungsromane", "Serie", "Taschenbücher", "Handbücher", "Hefte", "Stinkblumen", "Werke", "Beulwerte", "Publikationen", "Minis", "Maxis", "Sammlung", "Kollektion"});
		 
		 // value for CorporateNameTemplate
		 putLocalizedText(deLocale, "contributorTemplate", "{$ct-adj} {$ct-compound1}-{$ct-compound2}");
		 putLocalizedText(deLocale, "ct-adj", new String[]{"Internationale", "Bodenständige", "Handgearbeitete", "Wohltätige", "Fotografische", "Kalte", "Vorsichtige", "Sozialwissenschaftliche", "Sophisticated", "Allgemeine", "Stuttgarter", "Tübinger", "Reutlinger", "Ludwigsburger", "Echterdinger", "Individuelle"});
		 putLocalizedText(deLocale, "ct-compound1", new String[]{"Business", "Glut", "Motoren", "Sand", "Recherche", "Glocken", "Schokoladen", "Lebensmittel", "Werbungs", "Journalismus", "Individual", "Schulbuch"});
		 putLocalizedText(deLocale, "ct-compound2", new String[]{"Maschinen", "Werke", "Dienste", "Services", "Lagerhallen", "Werkstätten", "Dienstleistungen", "Druckerei", "Individualisten", "Verlagsauslieferung"});
		 
		 // values for the ONIXTitlePageTemplate
		 putLocalizedText(deLocale, "ONIXTitlePageTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Titelseite {$title}</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body style=\"text-align:center\"><h1>{$title}</h1><div><br/></div><h2>von {$author}</h2><div><br/></div><div>erschienen im</div><div><br/></div><div><br/></div><div>{$publishername}</div><div>{$publisherlocation}</div><div>{$year}</div><div><br/></div><div>ISBN {$isbn}</div></body></html>");
		 putLocalizedText(deLocale, "publisherlocation", new String[]{"Stuttgart", "Stuttgart-Vaihingen", "Stuttgart-Möhringen", "Stuttgart-Kaltental", "Stuttgart-Rohr"});
		 putLocalizedText(deLocale, "publishername", new String[]{"KNV IT-E-Books-Verlag", "Koch, Neff &amp; Volckmar IT-E-Books-Verlag", "KNO DiVA &amp; KNV IT-E-Books-Verlag", "KNO DiVA Verlag"});
		 
		 // values for the EPUBCoverPageTemplate
		 putLocalizedText(deLocale, "EPUBCoverPageTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Coverseite</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body style=\"text-align:center\"><div style=\"text-align:center;width:95%\"><img alt=\"Cover\" src=\"{$coverjpgpath}\" style=\"max-width:90%\" /></div></body></html>");
		 
		 // values for the EbookChapterTemplate
		 putLocalizedText(deLocale, "chapterTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Kapitel {$chapternumber}</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>Kapitel {$chapternumber}</h1><div><br/></div><div>{$chaptertext}</div></body></html>");
		 putLocalizedText(enLocale, "chapterTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Chapter {$chapternumber}</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>Chapter {$chapternumber}</h1><div><br/></div><div>{$chaptertext}</div></body></html>");
		 putLocalizedText(frLocale, "chapterTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Chapitre {$chapternumber}</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>Chapitre {$chapternumber}</h1><div><br/></div><div>{$chaptertext}</div></body></html>");
		 putLocalizedText(csLocale, "chapterTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Kapitola {$chapternumber}</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>Kapitola {$chapternumber}</h1><div><br/></div><div>{$chaptertext}</div></body></html>");
		 putLocalizedText(zhLocale, "chapterTemplate", "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>{$chapternumber} 卷</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>{$chapternumber} 卷</h1><div><br/></div><div>{$chaptertext}</div></body></html>");
		 
		 onixCodeListMap = new ConcurrentHashMap<Integer, Map<String, String>>();
		 
		 // assuming this HashMap is only used in read-only mode, so no need to consider concurrency
		 HashMap<String, String> list5 = new HashMap<String, String>();
		 list5.put("01", "Proprietary");
		 list5.put("02", "ISBN-10");
		 list5.put("03", "GTIN-13");
		 list5.put("04", "UPC");
		 list5.put("05", "ISMN-10");
		 list5.put("06", "DOI");
		 list5.put("13", "LCCN");
		 list5.put("14", "GTIN-14");
		 list5.put("15", "ISBN-13");
		 list5.put("17", "Legal deposit number");
		 list5.put("22", "URN");
		 list5.put("23", "OCLC number");
		 list5.put("24", "Co-publisher's ISBN-13");
		 list5.put("25", "ISMN-13");
		 list5.put("26", "ISBN-A");
		 list5.put("27", "JP e-code");
		 list5.put("28", "OLCC number");
		 list5.put("29", "JP Magazine ID");
		 
		 onixCodeListMap.put(new Integer(5), list5);
	}
	
	/**
	 * 
	 * @return The singleton instance of the ConfigurationRegistry
	 */
	public static synchronized ConfigurationRegistry getRegistry()
	{
		if(registry == null)
		{
			registry = new ConfigurationRegistry();
		}
		return registry;
	}
	
	/**
	 * Lookup method for getting a boolean configuration value based on a String key
	 * @param key The configuration registry key
	 * @return The boolean value that is represented by the key
	 */
	public boolean getBooleanValue(final String key)
	{
		String value = regMap.get(key);
		if(value == null) return false;
		if(value.equals("yes") | value.equals("y") | value.equals("j") | value.equals("ja")) return true;
		return false;
	}
	
	/**
	 * Lookup method for getting a double configuration value based on a String key
	 * @param key The configuration registry key
	 * @return The double value that is represented by the key. Return Double.MIN_VALUE if a parsing error occurs.
	 */
	public double getDoubleValue(final String key)
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
	
	/**
	 * Lookup method for getting an integer configuration value based on a String key
	 * @param key The configuration registry key.
	 * @return The integer value that is represented by the key; returns Integer.MIN_VALUE if a parsing error occurs.
	 */
	public int getIntValue(final String key)
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
	
	/**
	 * Lookup method for a localized text.
	 * @param loc The locale for the requested text.
	 * @param key The String key for the look-up.
	 * @return A String array containing the localized text strings.
	 */
	public String[] getLocalizedText(final Locale loc, final String key)
	{
		Map<String, String[]> innerMap = localeTextMap.get(loc);
		return innerMap.get(key);
	}
	
	/**
	 * Lookup method for getting a configuration String based on a String key
	 * @param key The configuration registry key.
	 * @return The integer value that is represented by the key; returns Integer.MIN_VALUE if a parsing error occurs.
	 */
	public String getString(final String key)
	{
		return regMap.get(key);
	}
	
	/**
	 * @return: A map for Onix code list values, where the integer key represents the Onix code list number
	 */
	public Map<Integer, Map<String, String>> getOnixCodeMap()
	{
		return onixCodeListMap;
	}

	/**
	 * 
	 * @param key The key to be used for the value to put
	 * @param value The value to be stored in the registry
	 */
	public void put(final String key, final String value)
	{
		regMap.put(key, value);
	}
	
	/*
	 * Convenience function: If a single String is to be stored in the locale map, wrap it into an array of length 1
	 */
	public void putLocalizedText(final Locale loc, final String key, final String value)
	{
		String[] sarr = new String[1];
		sarr[0] = value;
		putLocalizedText(loc, key, sarr);
	}
	
	/**
	 * Stores a localized registry key-value pair 
	 * @param loc The locale to be associated with the key-value pair
	 * @param key The key to use for the value
	 * @param sarr The string array to be stored with the key
	 */
	public void putLocalizedText(Locale loc, String key, String[] sarr)
	{
		Map<String, String[]> innerMap = localeTextMap.get(loc);
		innerMap.put(key, sarr);
	}
}