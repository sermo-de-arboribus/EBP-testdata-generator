package testdatagen.model.files;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import nu.xom.Attribute;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import nu.xom.Text;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.templates.EBookChapterTemplate;
import testdatagen.templates.EPUBTitlePageTemplate;
import testdatagen.utilities.Utilities;

public class EpubFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	private boolean isBooklet;
	// prepare a byte array with the ASCII codes for "application/epub+zip" without EOL/EOF marker
	private static byte[] mimetypeASCII = new byte[]{0x61, 0x70, 0x70, 0x6C, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x2F, 0x65, 0x70, 0x75, 0x62, 0x2B, 0x7A, 0x69, 0x70};
	
	public EpubFile(final long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) +  ".epub", demoFlag);
		this.ISBN = ISBN;
		isBooklet = false;
	}
	
	public EpubFile(final long ISBN, boolean demoFlag, boolean isBooklet)
	{
		this(ISBN, demoFlag);
		isBooklet = true;
	}
	
	public java.io.File generate(Title title, java.io.File destPath)
	{
		java.io.File tempDir = null;
		SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS");
		if(destPath.isDirectory())
		{
			tempDir = new java.io.File(FilenameUtils.concat(destPath.getPath(),"temp" + df.format(new Date())));
		}
		else
		{
			tempDir = new java.io.File(FilenameUtils.concat(destPath.getParent(), "temp" + df.format(new Date())));
		}
		
		// generate cover file
		JpegFile cover = new JpegFile(title.getIsbn13(), GraphicFile.Type.COVER);
		java.io.File imageFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/images/" + title.getIsbn13() + ".jpg"));
		
		cover.generate(title, imageFilePath);
		
		// generate title page file
		EPUBTitlePageTemplate template = new EPUBTitlePageTemplate(new Locale("de"), title);
		String HTMLTitlePage = template.fillWithText();
		java.io.File HTMLTitlePagePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "OEBPS/text/Title.xhtml"));
		saveHtmlFile(HTMLTitlePagePath, HTMLTitlePage);
		
		// generate HTML chapter files
		List<java.io.File> chapterFiles = new ArrayList<java.io.File>();
		for(int i = 1; i < 13; i++)
		{
			EBookChapterTemplate chapterTemplate = new EBookChapterTemplate(new Locale("de"), i);
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
		
		// add chapters
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
		
		opfRoot.appendChild(metadata);
		
		Element manifest = new Element("manifest", "http://www.idpf.org/2007/opf");
		Element item1 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item2 = new Element("item", "http://www.idpf.org/2007/opf");
		Element item3 = new Element("item", "http://www.idpf.org/2007/opf");
		
		item1.addAttribute(new Attribute("href", "toc.ncx"));
		item1.addAttribute(new Attribute("id", "ncx"));
		item1.addAttribute(new Attribute("media-type", "application/x-dtbncx+xml"));
		manifest.appendChild(item1);
		
		item2.addAttribute(new Attribute("href", "text/Title.xhtml"));
		item2.addAttribute(new Attribute("id", "Title"));
		item2.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
		manifest.appendChild(item2);
		
		item3.addAttribute(new Attribute("href", "images/" + title.getIsbn13() + ".jpg"));
		item3.addAttribute(new Attribute("id", "cover"));
		item3.addAttribute(new Attribute("media-type", "image/jpeg"));
		item3.addAttribute(new Attribute("properties", "cover-image"));
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
		guideRef.addAttribute(new Attribute("href", "images/" + title.getIsbn13() + ".jpg"));
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
		String dest = null;
		if(destPath.isDirectory())
		{
			dest = FilenameUtils.concat(destPath.getPath(), "" + title.getIsbn13() + ".epub");
		}
		else
		{
			dest = destPath.getPath();
		}
		
		zipEpub(dest, tempDir.getPath());
		
		// delete temp folder
		try
		{
			FileUtils.deleteDirectory(tempDir);
		}
		catch (IOException ex)
		{
			Utilities.showWarnPane("Could not delete temporary directory " + tempDir.getPath() + " for " + title.getIsbn13());
		}
		
		return new java.io.File(dest);
	}
	
	public boolean isBooklet()
	{
		return isBooklet;
	}
	
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
		return fileString + "[" +ISBN+ "]";
	}
	
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