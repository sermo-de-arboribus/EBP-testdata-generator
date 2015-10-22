package testdatagen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.FilenameUtils;

import testdatagen.controller.OnixPartsBuilder;
import testdatagen.model.Title;
import testdatagen.model.files.GraphicFile;
import testdatagen.model.files.ONIXFile;

public class GeneratorThread extends Thread
{
	List<Title> titleList;
	java.io.File destDir;
	
	public GeneratorThread(final List<Title> titleList, final File destDir)
	{
		this.titleList = titleList;
		this.destDir = destDir;
	}

	public void run()
	{
		ListIterator<Title> titleIterator = titleList.listIterator();
		while(titleIterator.hasNext())
		{
			Title title = titleIterator.next();
			// generate covers
			ArrayList<GraphicFile> coversList = title.getCoverFiles();
			for(GraphicFile coverFile : coversList)
			{
				java.io.File storedFile = coverFile.generate(title, destDir);
				// TODO: check, if cover is handled by MediaFileLink
				if(title.hasMediaFileLink())
				{
					// TODO: upload cover file(s) to Dropbox and delete them from the local disc; keep file for later usage by ONIX builder
					DropboxUploaderThread uploader = new DropboxUploaderThread(storedFile);
					uploader.start();
					// TODO: how to handle notification when upload is finished?
				}
			}
			// generate ONIX 2.1 file with long tag names
			ONIXFile onixFile = new ONIXFile(title.getIsbn13(), OnixPartsBuilder.REFERENCETAG, "2.1");
			// TODO: do we need the return value of generate()? 
			onixFile.generate(title, destDir);
			
			// generate ONIX 2.1 file with short tag names
			onixFile = new ONIXFile(title.getIsbn13(), OnixPartsBuilder.SHORTTAG, "2.1");
			// TODO: do we need the return value of generate()? 
			onixFile.generate(title, destDir);
			
			// generate ONIX 3.0 file with long tag names
			onixFile = new ONIXFile(title.getIsbn13(), OnixPartsBuilder.REFERENCETAG, "3.0");
			onixFile.generate(title, destDir);
			
			// generate ONIX 3.0 file with short tag names
			onixFile = new ONIXFile(title.getIsbn13(), OnixPartsBuilder.SHORTTAG, "3.0");
			onixFile.generate(title, destDir);
			
			// TODO: iterate over all file Objects and generate the files
			ArrayList<testdatagen.model.files.File> fileList = title.getNonCoverFiles();
			for(testdatagen.model.files.File file : fileList)
			{
				file.generate(title, new java.io.File(FilenameUtils.concat(destDir.getPath(), file.getName())));
			}
		}
	}
}