package testdatagen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.FilenameUtils;

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
					DropboxUploaderThread uploader = new DropboxUploaderThread(storedFile, title);
					uploader.start();
					// TODO: how to handle notification when upload is finished?
				}
			}
			// TODO: figure out a good notification strategy among threads with wait() and notify()
			// for the time being: just sleep a while, to give the Dropbox uploader the opportunity
			// to upload the cover file
			try
			{
				sleep(5000);
			}
			catch(InterruptedException e)
			{
				//
			}
			
			System.out.println("MediaFileLink is: " + title.getMediaFileUrl());
			ONIXFile onixFile = new ONIXFile(title.getIsbn13());
			// TODO: do we need the return value of generate()? 
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
