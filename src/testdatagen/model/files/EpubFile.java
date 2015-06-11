package testdatagen.model.files;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import nu.xom.Attribute;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import nu.xom.Text;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
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
	
	public java.io.File generate(Title title, java.io.File destDir)
	{	
		java.io.File tempDir = new java.io.File(destDir.getPath() + "temp");
		// generate cover file
		JpegFile cover = new JpegFile(title.getIsbn13(), GraphicFile.Type.COVER);
		cover.generate(title, new java.io.File(FilenameUtils.concat(tempDir.getPath(), "/OEBPS/images/")));
		
		// generate title page file
		EPUBTitlePageTemplate template = new EPUBTitlePageTemplate(new Locale("de"), title);
		String HTMLTitlePage = template.fillWithText();
		try
		{
			PrintWriter out = new PrintWriter(FilenameUtils.concat(tempDir.getPath(), "OEBPS/text/Section0001.xhtml"));
			out.print(HTMLTitlePage);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		// generate NCX file
		DocType ncxDtd = new DocType("ncx", "-//NISO//DTD ncx 2005-1//EN", "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd");
		Element ncxRoot = new Element("ncx", "http://www.daisy.org/z3986/2005/ncx/");
		Document ncxFile = new Document(ncxRoot);
		ncxFile.setDocType(ncxDtd);
		
		ncxRoot.addAttribute(new Attribute("version", "2005-1"));
		
		Element ncxHead = new Element("head");
		Element meta1 = new Element("meta");
		Element meta2 = new Element("meta");
		Element meta3 = new Element("meta");
		Element meta4 = new Element("meta");
		
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
		
		Element ncxTitle = new Element("docTitle");
		Element titleText = new Element("text");
		titleText.appendChild(new Text(title.getName()));
		ncxTitle.appendChild(titleText);
		ncxRoot.appendChild(ncxTitle);
		
		Element navMap = new Element("navMap");
		Element navPoint = new Element("navPoint");
		navPoint.addAttribute(new Attribute("id", "navPoint-1"));
		navPoint.addAttribute(new Attribute("playOrder", "1"));
		navMap.appendChild(navPoint);
		Element navLabel = new Element("navLabel");
		Element labelText = new Element("text");
		labelText.appendChild(new Text("Startseite"));
		navLabel.appendChild(labelText);
		navPoint.appendChild(navLabel);
		Element content = new Element("content");
		content.addAttribute(new Attribute("src", "text/Section0001.xhtml"));
		navPoint.appendChild(content);
		ncxRoot.appendChild(navMap);
		
		java.io.File ncxFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "/OEBPS/" + title.getIsbn13() + "toc.ncx"));
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
		
		Element identifier = new Element("identifier", "http://purl.org/dc/elements/1.1/");
		identifier.addAttribute(new Attribute("id", "BookId"));
		identifier.addAttribute(new Attribute("scheme", "http://www.idpf.org/2007/opf", "UUID"));
		identifier.appendChild(new Text("urn:isbn:" + title.getIsbn13()));
		metadata.appendChild(identifier);
		
		Element dcTitle = new Element("title", "http://purl.org/dc/elements/1.1/");
		dcTitle.appendChild(new Text(title.getName()));
		metadata.appendChild(dcTitle);
		
		Element dcCreator = new Element("creator", "http://purl.org/dc/elements/1.1/");
		dcCreator.addAttribute(new Attribute("file-as", "http://www.idpf.org/2007/opf", title.getAuthorLastName() + ", " + title.getAuthorFirstName()));
		dcCreator.addAttribute(new Attribute("role", "http://www.idpf.org/2007/opf", "aut"));
		dcCreator.appendChild(new Text(title.getAuthor()));
		metadata.appendChild(dcCreator);
		
		Element dcLanguage = new Element("language", "http://purl.org/dc/elements/1.1/");
		dcLanguage.appendChild(new Text("de")); // TODO: use current locale?
		metadata.appendChild(dcLanguage);
		
		Element dcDate = new Element("date", "http://purl.org/dc/elements/1.1/");
		dcDate.addAttribute(new Attribute("event", "http://www.idpf.org/2007/opf", "modification"));
		dcDate.appendChild(new Text(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
		metadata.appendChild(dcDate);
		
		Element isbn = new Element("identifier", "http://purl.org/dc/elements/1.1/");
		isbn.addAttribute(new Attribute("scheme", "http://www.idpf.org/2007/opf", "ISBN"));
		isbn.appendChild(new Text("" + title.getIsbn13()));
		metadata.appendChild(isbn);
		
		Element dcPublisher = new Element("publisher", "http://purl.org/dc/elements/1.1/");
		dcPublisher.appendChild(new Text("KNV IT E-Books-verlag"));
		metadata.appendChild(dcPublisher);
		
		opfRoot.appendChild(metadata);
		
		Element manifest = new Element("manifest");
		Element item1 = new Element("item");
		Element item2 = new Element("item");
		Element item3 = new Element("item");
		
		item1.addAttribute(new Attribute("href", "toc.ncx"));
		item1.addAttribute(new Attribute("id", "ncx"));
		item1.addAttribute(new Attribute("media-type", "application/x-dtbncx+xml"));
		manifest.appendChild(item1);
		
		item2.addAttribute(new Attribute("href", "text/Section0001.xhtml"));
		item2.addAttribute(new Attribute("id", "Section0001"));
		item2.addAttribute(new Attribute("media-type", "application/xhtml+xml"));
		manifest.appendChild(item2);
		
		item3.addAttribute(new Attribute("href", "images/" + title.getIsbn13() + ".jpg"));
		item3.addAttribute(new Attribute("id", "cover"));
		item3.addAttribute(new Attribute("media-type", "image/jpeg"));
		item3.addAttribute(new Attribute("properties", "cover-image"));
		manifest.appendChild(item3);
		
		opfRoot.appendChild(manifest);
		
		Element spine = new Element("spine");
		spine.addAttribute(new Attribute("toc", "ncx"));
		
		Element itemref = new Element("itemref");
		itemref.addAttribute(new Attribute("idref", "Section0001.xhtml"));
		spine.appendChild(itemref);
		
		opfRoot.appendChild(spine);
		
		Element guide = new Element("guide");
		Element guideRef = new Element("reference");
		guideRef.addAttribute(new Attribute("type", "cover"));
		guideRef.addAttribute(new Attribute("title", "Cover"));
		guideRef.addAttribute(new Attribute("href", "images/" + title.getIsbn13() + ".jpg"));
		guide.appendChild(guideRef);
		
		opfRoot.appendChild(guide);
		
		Document opfDoc = new Document(opfRoot);
		
		java.io.File opfFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "/OEBPS/" + title.getIsbn13() + "content.opf"));
		try
		{
			FileOutputStream fos = new FileOutputStream(opfFilePath);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(ncxFile);
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
		
		Element rootfiles = new Element("rootfiles");
		Element rootfile = new Element("rootfile");
		rootfile.addAttribute(new Attribute("full-path", "OEBPS/content.opf"));
		rootfile.addAttribute(new Attribute("media-type", "application/oebps-package+xml"));
		rootfiles.appendChild(rootfile);
		container.appendChild(rootfile);
		
		Document containerDoc = new Document(container);
		
		java.io.File containerFilePath = new java.io.File(FilenameUtils.concat(tempDir.getPath(), "/META-INF/container.xml"));
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
		String dest = FilenameUtils.concat(destDir.getPath(), "" + title.getIsbn13() + ".epub");
		zipEpub(dest, destDir.getPath());
		
		return new java.io.File(dest);
	}
	
	public boolean isBooklet()
	{
		return isBooklet;
	}
	
	public String toString()
	{
		return "Epub["+ISBN+"]" + (isBooklet ? "_Booklet" : "");
	}
	
	private void zipEpub(String zipFileName, String dir)
	{
		try
		{
		    java.io.File dirObj = new java.io.File(dir);
		    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		    
		    // write uncompressed mimetype file
			out.setLevel(ZipOutputStream.STORED);
			ZipEntry zipEntry = new ZipEntry("mimetype");
			out.putNextEntry(zipEntry);
			out.write(mimetypeASCII, 0, mimetypeASCII.length);
			out.closeEntry();
			
			out.setLevel(ZipOutputStream.DEFLATED);
			addDir(dirObj, out);
		    out.close();
		}
		catch(IOException ex)
		{
			Utilities.showErrorPane("Error zipping Epub file " + zipFileName, ex);
		}
	}

	private void addDir(java.io.File dirObj, ZipOutputStream out) throws IOException
	{
		java.io.File[] files = dirObj.listFiles();
	    byte[] tmpBuf = new byte[1024];

	    for (int i = 0; i < files.length; i++)
	    {
	      if (files[i].isDirectory())
	      {
	        addDir(files[i], out);
	        continue;
	      }
	      FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
	      out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
	      int len;
	      while ((len = in.read(tmpBuf)) > 0)
	      {
	        out.write(tmpBuf, 0, len);
	      }
	      out.closeEntry();
	      in.close();
	    }
	}
}