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
			ArrayList<Thread> uploadThreads = new ArrayList<Thread>();
			for(GraphicFile coverFile : coversList)
			{
				java.io.File storedFile = coverFile.generate(title, destDir);
				// TODO: check, if cover is handled by MediaFileLink
				if(title.hasMediaFileLink())
				{
					// TODO: upload cover file(s) to Dropbox and delete them from the local disc; keep file for later usage by ONIX builder
					DropboxUploaderThread uploader = new DropboxUploaderThread(storedFile, title);
					uploadThreads.add(uploader);
					uploader.start();
				}
			}
			
			// wait for dropbox upload threads to finish
			try
			{
				for(Thread uploadThread : uploadThreads)
				{
					uploadThread.join();
				}
			}
			catch(InterruptedException e)
			{
				System.out.println("Warning: interrupted while waiting for results of dropbox upload threads");
			}
			
			System.out.println("MediaFileLink is: " + title.getMediaFileUrl());
			ONIXFile onixFile = new ONIXFile(title.getIsbn13());
			onixFile.generate(title, destDir);
			
			ArrayList<testdatagen.model.files.File> fileList = title.getNonCoverFiles();
			for(testdatagen.model.files.File file : fileList)
			{
				file.generate(title, new java.io.File(FilenameUtils.concat(destDir.getPath(), file.getName())));
			}
		}
	}
}