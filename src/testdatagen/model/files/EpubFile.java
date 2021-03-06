package testdatagen.model.files;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import nu.xom.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.templates.*;
import testdatagen.utilities.Utilities;

/**
 * This concrete class represents and generates E-Book files in the EPUB format (see http://idpf.org/epub).
 * EPUB files can be full product files, samples/demos of the product, or booklets (for audio books).
 */
public class EpubFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	private boolean isBooklet;
	// prepare a byte array with the ASCII codes for the String "application/epub+zip" without EOL/EOF marker
	private static final byte[] mimetypeASCII = new byte[]{0x61, 0x70, 0x70, 0x6C, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x2F, 0x65, 0x70, 0x75, 0x62, 0x2B, 0x7A, 0x69, 0x70};
	
	/**
	 * Convenience Constructor, sets the isBooklet flag (see below) to false
	 * @param title The product Title object that this EPUB file belongs to.
	 * @param demoFlag This boolean flag indicates, if the product is meant as a demo or sample file (which has an influence on file naming).
	 */
	public EpubFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
		isBooklet = false;
	}
	
	/**
	 * Constructor
	 * @param title The product Title object that this EPUB file belongs to.
	 * @param demoFlag This boolean flag indicates, if the product is meant as a demo or sample file (which has an influence on file naming).
	 * @param isBooklet This boolean flag indicates, if the EPUB is used as a booklet for an AUDIO product.
	 */
	public EpubFile(final Title title, final boolean demoFlag, final boolean isBooklet)
	{
		this(title, demoFlag);
		this.isBooklet = isBooklet;
	}

	@Override
	public java.io.File generate(final java.io.File destFolder)
	{
		java.io.File tempDir = Utilities.getTempDir();	
		// generate cover file
		JpegFile cover = new JpegFile(title, GraphicFile.Type.COVER);
		java.io.File imageFileDir = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/images/"));
		java.io.File imageFilePath = cover.generate(imageFileDir);
		
		String tocCoverPath = imageFilePath.toURI().toString();
		int index = tocCoverPath.indexOf("images/");
		tocCoverPath = tocCoverPath.substring(index);
		
		// generate cover HTML
		EPUBCoverPageTemplate coverTemplate = new EPUBCoverPageTemplate(new Locale("de"), "../" + tocCoverPath);
		String HTMLCoverPage = coverTemplate.fillWithText();
		java.io.File HTMLCoverPagePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/text/Cover.xhtml"));
		saveHtmlFile(HTMLCoverPagePath, HTMLCoverPage);
		
		// generate title page file
		EPUBTitlePageTemplate template = new EPUBTitlePageTemplate(new Locale("de"), title);
		String HTMLTitlePage = template.fillWithText();
		java.io.File HTMLTitlePagePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/text/Title.xhtml"));
		saveHtmlFile(HTMLTitlePagePath, HTMLTitlePage);
		
		// generate HTML chapter files
		List<java.io.File> chapterFiles = new ArrayList<java.io.File>();
		for(int i = 1; i < 13; i++)
		{
			Locale locale = getRandomLocale();
			EBookChapterTemplate chapterTemplate = new EBookChapterTemplate(locale, i);
			String HTMLChapterPage = chapterTemplate.fillWithText();
			java.io.File HTMLChapterFile = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/text/Chapter" + i + ".xhtml"));
			chapterFiles.add(HTMLChapterFile);
			saveHtmlFile(HTMLChapterFile, HTMLChapterPage);
		}
		
		// generate NCX file
		DocType ncxDtd = new DocType("ncx", "-//NISO//DTD ncx 2005-1//EN", "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd");
		Element ncxRoot = new Element("ncx", "http://www.daisy.org/z3986/2005/ncx/");
		Document ncxFile = new Document(ncxRoot);
		ncxFile.setDocType(ncxDtd);
		
		ncxRoot.addAttribute(new Attribute("version", "2005-1"));
		
		Element ncxHead = new Element("head", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta1 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta2 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta3 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		Element meta4 = new Element("meta", "http://www.daisy.org/z3986/2005/ncx/");
		
		meta1.addAttribute(new Attribute("name", "dtb:uid"));
		meta1.addAttribute(new Attribute("content", "urn:isbn:" + title.getIsbn13()));
		meta2.addAttribute(new Attribute("name", "dtb:depth"));
		meta2.addAttribute(new Attribute("content", "0"));
		meta3.addAttribute(new Attribute("name", "dtb:totalPageCount"));
		meta3.addAttribute(new Attribute("content", "0"));
		meta4.addAttribute(new Attribute("name", "dtb:maxPageNumber"));
		meta4.addAttribute(new Attribute("content", "0"));
		
		ncxHead.appendChild(meta1);
		ncxHead.appendChild(meta2);
		ncxHead.appendChild(meta3);
		ncxHead.appendChild(meta4);
		
		ncxRoot.appendChild(ncxHead);
		
		Element ncxTitle = new Element("docTitle", "http://www.daisy.org/z3986/2005/ncx/");
		Element titleText = new Element("text", "http://www.daisy.org/z3986/2005/ncx/");
		titleText.appendChild(new Text(title.getName()));
		ncxTitle.appendChild(titleText);
		ncxRoot.appendChild(ncxTitle);
		
		Element navMap = new Element("navMap", "http://www.daisy.org/z3986/2005/ncx/");
		Element navPoint = new Element("navPoint", "http://www.daisy.org/z3986/2005/ncx/");
		navPoint.addAttribute(new Attribute("id", "navPoint-1"));
		navPoint.addAttribute(new Attribute("playOrder", "1"));
		navMap.appendChild(navPoint);
		Element navLabel = new Element("navLabel", "http://www.daisy.org/z3986/2005/ncx/");
		Element labelText = new Element("text", "http://www.daisy.org/z3986/2005/ncx/");
		labelText.appendChild(new Text("Startseite"));
		navLabel.appendChild(labelText);
		navPoint.appendChild(navLabel);
		Element content = new Element("content", "http://www.daisy.org/z3986/2005/ncx/");
		content.addAttribute(new Attribute("src", "text/Title.xhtml"));
		navPoint.appendChild(content);
		
		// add book chapters
		Iterator<java.io.File> iterator = chapterFiles.iterator();
		int counter = 2;
		while(iterator.hasNext())
		{
			java.io.File nextFile = iterator.next();
			Element chapterNavPoint = new Element("navPoint", "http://www.daisy.org/z3986/2005/ncx/");
			chapterNavPoint.addAttribute(new Attribute("id", "navPoint-" + counter));
			chapterNavPoint.addAttribute(new Attribute("playOrder", "" + counter));
			
			Element chapterLabel = new Element("navLabel", "http://www.daisy.org/z3986/2005/ncx/");
			Element chapterText = new Element("text", "http://www.daisy.org/z3986/2005/ncx/");
			chapterText.appendChild(new Text("Kapitel " + (counter - 1)));
			chapterLabel.appendChild(chapterText);
			chapterNavPoint.appendChild(chapterLabel);
			Element chapterContent = new Element("content", "http://www.daisy.org/z3986/2005/ncx/");
			chapterContent.addAttribute(new Attribute("src", "text/" + FilenameUtils.getName(nextFile.getPath())));
			chapterNavPoint.appendChild(chapterContent);
			navMap.appendChild(chapterNavPoint);
			
			counter++;
		}
		
		ncxRoot.appendChild(navMap);
		
		java.io.File ncxFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/toc.ncx"));
		try
		{
			FileOutputStream fos = new FileOutputStream(ncxFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(ncxFile);
			ser.flush();
			fos.close();
		}
		catch (IOException ex)
		{
			Utilities.showErrorPane("Could not save NCX file when generating an Epub for " + title.getIsbn13(), ex);
		}
		
		// generate OPF file
		Element opfRoot = new Element("package", "http://www.idpf.org/2007/opf");
		opfRoot.addAttribute(new Attribute("unique-identifier", "BookId"));
		opfRoot.addAttribute(new Attribute("version", "2.0"));
		
		Element metadata = new Element("metadata", "http://www.idpf.org/2007/opf");
		metadata.addNamespaceDeclaration("dc", "http://purl.org/dc/elements/1.1/");
		metadata.addNamespaceDeclaration("opf", "http://www.idpf.org/2007/opf");
		
		Element identifier = new Element("dc:identifier", "http://purl.org/dc/elements/1.1/");
		identifier.addAttribute(new Attribute("id", "BookId"));
		identifier.addAttribute(new Attribute("opf:scheme", "http://www.idpf.org/2007/opf", "UUID"));
		identifier.appendChild(new Text("urn:isbn:" + title.getIsbn13()));
		metadata.appendChild(identifier);
		
		Element dcTitle = new Element("dc:title", "http://purl.org/dc/elements/1.1/");
		dcTitle.appendChild(new Text(title.getName()));
		metadata.appendChild(dcTitle);
		
		Element dcCreator = new Element("dc:creator", "http://purl.org/dc/elements/1.1/");
		dcCreator.addAttribute(new Attribute("opf:file-as", "http://www.idpf.org/2007/opf", title.getAuthorLastName() + ", " + title.getAuthorFirstName()));
		dcCreator.addAttribute(new Attribute("opf:role", "http://www.idpf.org/2007/opf", "aut"));
		dcCreator.appendChild(new Text(title.getAuthor()));
		metadata.appendChild(dcCreator);
		
		Element dcLanguage = new Element("dc:language", "http://purl.org/dc/elements/1.1/");
		dcLanguage.appendChild(new Text("de")); // TODO: use current locale?
		metadata.appendChild(dcLanguage);
		
		Element dcDate = new Element("dc:date", "http://purl.org/dc/elements/1.1/");
		dcDate.addAttribute(new Attribute("opf:event", "http://www.idpf.org/2007/opf", "modification"));
		dcDate.appendChild(new Text(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
		metadata.appendChild(dcDate);
		
		Element isbn = new Element("dc:identifier", "http://purl.org/dc/elements/1.1/");
		isbn.addAttribute(new Attribute("opf:scheme", "http://www.idpf.org/2007/opf", "ISBN"));
		isbn.appendChild(new Text("" + title.getIsbn13()));
		metadata.appendChild(isbn);
		
		Element dcPublisher = new Element("dc:publisher", "http://purl.org/dc/elements/1.1/");
		dcPublisher.appendChild(new Text("KNV IT E-Books-verlag"));
		metadata.appendChild(dcPublisher);
		
		Element metaCover = new Element("meta", "http://www.idpf.org/2007/opf");
		metaCover.addAttribute(new Attribute("name", "cover"));
		metaCover.addAttribute(new Attribute("content", "coverjpg"));
		metadata.appendChild(metaCover);
		
		opfRoot.appendChild(metadata);
		
		Element manifest = new Element("manifest", "http://www.idpf.org/2007/opf");
		Element item1 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item2 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item3 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item4 = new Element("item", "http://www.idpf.org/2007/opf");
		
		item1.addAttribute(new Attribute("href", "toc.ncx"));
		item1.addAttribute(new Attribute("id", "ncx"));
		item1.addAttribute(new Attribute("media-type", "application/x-dtbncx+xml"));
		manifest.appendChild(item1);
		
		item2.addAttribute(new Attribute("href", "text/Title.xhtml"));
		item2.addAttribute(new Attribute("id", "Title"));
		item2.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
		manifest.appendChild(item2);
		
		// list cover HTML
		item4.addAttribute(new Attribute("href", "text/Cover.xhtml"));
		item4.addAttribute(new Attribute("id", "Cover"));
		item4.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
		manifest.appendChild(item4);
		
		// list cover JPG
		item3.addAttribute(new Attribute("href", tocCoverPath));
		item3.addAttribute(new Attribute("id", "coverjpg"));
		item3.addAttribute(new Attribute("media-type", "image/jpeg"));
		// This is a suggested solution for generating Mobi, but only valid in EPUB 3
		// item3.addAttribute(new Attribute("properties", "cover-image"));
		manifest.appendChild(item3);
		
		// add chapters
		Iterator<java.io.File> opfChapterItems = chapterFiles.iterator();
		while(opfChapterItems.hasNext())
		{
			java.io.File nextFile = opfChapterItems.next();
			Element nextItem = new Element("item", "http://www.idpf.org/2007/opf");
			nextItem.addAttribute(new Attribute("href", "text/" + FilenameUtils.getName(nextFile.getPath())));
			nextItem.addAttribute(new Attribute("id", FilenameUtils.getBaseName(nextFile.getPath())));
			nextItem.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
			manifest.appendChild(nextItem);
		}
		
		opfRoot.appendChild(manifest);
		
		Element spine = new Element("spine", "http://www.idpf.org/2007/opf");
		spine.addAttribute(new Attribute("toc", "ncx"));
		
		Element coverref = new Element("itemref", "http://www.idpf.org/2007/opf");
		coverref.addAttribute(new Attribute("idref", "Cover"));
		spine.appendChild(coverref);
		
		Element itemref = new Element("itemref", "http://www.idpf.org/2007/opf");
		itemref.addAttribute(new Attribute("idref", "Title"));
		spine.appendChild(itemref);
		
		// add chapters
		Iterator<java.io.File> opfChapterSpineRefs = chapterFiles.iterator();
		while(opfChapterSpineRefs.hasNext())
		{
			java.io.File nextFile = opfChapterSpineRefs.next();
			Element nextItem = new Element("itemref", "http://www.idpf.org/2007/opf");
			nextItem.addAttribute(new Attribute("idref", FilenameUtils.getBaseName(nextFile.getPath())));
			spine.appendChild(nextItem);
		}
		
		opfRoot.appendChild(spine);
		
		Element guide = new Element("guide", "http://www.idpf.org/2007/opf");
		Element guideRef = new Element("reference", "http://www.idpf.org/2007/opf");
		guideRef.addAttribute(new Attribute("type", "cover"));
		guideRef.addAttribute(new Attribute("title", "Cover"));
		guideRef.addAttribute(new Attribute("href", "text/Cover.xhtml"));
		guide.appendChild(guideRef);
		
		opfRoot.appendChild(guide);
		
		Document opfDoc = new Document(opfRoot);
		
		java.io.File opfFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/content.opf"));
		try
		{
			FileOutputStream fos = new FileOutputStream(opfFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(opfDoc);
			ser.flush();
			fos.close();
		}
		catch (IOException ex)
		{
			Utilities.showErrorPane("Could not save OPF file when generating an Epub for " + title.getIsbn13(), ex);
		}
		
		// create container.xml file
		Element container = new Element("container", "urn:oasis:names:tc:opendocument:xmlns:container");
		container.addAttribute(new Attribute("version", "1.0"));
		
		Element rootfiles = new Element("rootfiles", "urn:oasis:names:tc:opendocument:xmlns:container");
		Element rootfile = new Element("rootfile", "urn:oasis:names:tc:opendocument:xmlns:container");
		rootfile.addAttribute(new Attribute("full-path", "OEBPS/content.opf"));
		rootfile.addAttribute(new Attribute("media-type", "application/oebps-package+xml"));
		rootfiles.appendChild(rootfile);
		container.appendChild(rootfiles);
		
		Document containerDoc = new Document(container);
		java.io.File containerFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "META-INF/container.xml"));
		containerFilePath.getParentFile().mkdirs();
		try
		{
			FileOutputStream fos = new FileOutputStream(containerFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(containerDoc);
			ser.flush();
			fos.close();
		}
		catch (IOException ex)
		{
			Utilities.showErrorPane("Could not save container file when generating an Epub for " + title.getIsbn13(), ex);
		}
		
		// zip epub in a way that is conformant with the EPUB spec
		String destPath = FilenameUtils.concat(destFolder.getPath(), "" + buildFileName());
		
		zipEpub(destPath, tempDir.getPath());
		
		// delete temp folder
		try
		{
			FileUtils.deleteDirectory(tempDir);
		}
		catch (IOException ex)
		{
			Utilities.showWarnPane("Could not delete temporary directory " + tempDir.getPath() + " for " + title.getIsbn13());
		}
		
		return new java.io.File(destPath);
	}
	
	/**
	 * @return Returns "true" is this EPUB represents the booklet of an audio product
	 */
	public boolean isBooklet()
	{
		return isBooklet;
	}
	
	@Override
	public String toString()
	{
		String fileString = "Epub";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		if(this.isBooklet())
		{
			fileString += "_booklet";
		}
		return fileString + "[" + title.getIsbn13() + "]";
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "") + (isBooklet() ? "_Booklet" : "") +  ".epub";
	}

	// Helper method to put all temporary files into a zip container as required by the EPUB specification
	private void zipEpub(String zipFileName, String dir)
	{
		try
		{
		    java.io.File dirObj = new java.io.File(dir);
		    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		    
		    // write uncompressed mimetype file
			ZipEntry zipEntry = new ZipEntry("mimetype");
			zipEntry.setMethod(ZipOutputStream.STORED);
			zipEntry.setSize(20);
			zipEntry.setCompressedSize(20);
			zipEntry.setCrc(0x2CAB616F);
			out.putNextEntry(zipEntry);
			out.write(mimetypeASCII, 0, mimetypeASCII.length);
			out.closeEntry();
			
			// add all the remaining files
			addDir("", dirObj, out);
		    out.close();
		}
		catch(IOException ex)
		{
			Utilities.showErrorPane("Error zipping Epub file " + zipFileName, ex);
		}
	}

	// Helper method to add all files in a given directory to a zip container. The method works recursively
	// through all child directories.
	private void addDir(String basePath, java.io.File dirObj, ZipOutputStream out) throws IOException
	{
		java.io.File[] files = dirObj.listFiles();
	    byte[] tmpBuf = new byte[1024];

	    for (int i = 0; i < files.length; i++)
	    {
	      if (files[i].isDirectory())
	      {
	    	String path = basePath + files[i].getName() + "/";
	    	out.putNextEntry(new ZipEntry(path));
	        addDir(path, files[i], out);
	        out.closeEntry();
	        continue;
	      }
	      FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
	      out.putNextEntry(new ZipEntry(basePath + files[i].getName()));
	      int len;
	      while ((len = in.read(tmpBuf)) > 0)
	      {
	        out.write(tmpBuf, 0, len);
	      }
	      out.closeEntry();
	      in.close();
	    }
	}

	private Locale getRandomLocale()
	{
		Random random = new Random();
		int rnd = random.nextInt(18);
		switch(rnd)
		{
			case 1: case 2: return new Locale("en");
			case 3: case 4: return new Locale("fr");
			case 5: return new Locale("cs");
			case 6: return new Locale("zh");
			default: return new Locale("de");
		}
	}
	
	// Helper method to write out HTML pages (passed in as a String) to a file
	private void saveHtmlFile(java.io.File HTMLPagePath, String HTMLPage)
	{
		HTMLPagePath.getParentFile().mkdirs();
		try
		{
			PrintWriter out = new PrintWriter(HTMLPagePath, "UTF-8");
			out.print(HTMLPage);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}