package testdatagen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.SwingWorker;

import testdatagen.DropboxUploaderThread;
import testdatagen.model.Title;
import testdatagen.model.files.GraphicFile;
import testdatagen.model.files.ONIXFile;
import testdatagen.onixbuilder.OnixPartsBuilder;

/**
 * GeneratorThread is a class representing a worker thread for writing the required files to the disk.
 * As I/O is usually relatively slow, and to keep the graphical user interface responsive, this class
 * is derived from SwingWorker, which also allows a progress bar of the GeneratorThread's work-load 
 * to be displayed in the GUI.
 */
public class GeneratorThread extends SwingWorker<Void, Void>
{
	private List<Title> titleList;
	private java.io.File destDir;
	
	/**
	 * Constructor.
	 * @param titleList The list of product Title objects, whose files are to be generated and stored
	 * @param destDir The destination directory as a java.io.File object 
	 */
	public GeneratorThread(final List<Title> titleList, final File destDir)
	{
		this.titleList = titleList;
		this.destDir = destDir;
	}

	/**
	 * The method does the actual work of the objects of this class (see class description above).
	 * If necessary, it also uploads the cover files to a Dropbox online storage account.
	 * It also maintains a progress value which can be used by the main window's progress bar. 
	 */
	public Void doInBackground()
	{
		setProgress(0);
		ListIterator<Title> titleIterator = titleList.listIterator();
		int progress = 0;
		final int tasks = titleList.size();
		while(titleIterator.hasNext())
		{
			Title title = titleIterator.next();
			// generate covers
			ArrayList<GraphicFile> coversList = title.getCoverFiles();
			ArrayList<Thread> uploadThreads = new ArrayList<Thread>();
			for(GraphicFile coverFile : coversList)
			{
				java.io.File storedFile = coverFile.generate(destDir);
				if(title.hasMediaFileLink())
				{
					DropboxUploaderThread uploader = new DropboxUploaderThread(storedFile, title);
					uploadThreads.add(uploader);
					uploader.start();
				}
			}
			
			// wait for dropbox upload threads to finish - we need the uploads to be finished before
			// Onix files are generated, because the shareable URL is saved in the Onix file.
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

			// generate ONIX 2.1 file with long tag names
			ONIXFile onixFile = new ONIXFile(title, OnixPartsBuilder.REFERENCETAG, "2.1");
			onixFile.generate(destDir);
			
			// generate ONIX 2.1 file with short tag names
			onixFile = new ONIXFile(title, OnixPartsBuilder.SHORTTAG, "2.1");
			onixFile.generate(destDir);
			
			// generate ONIX 3.0 file with long tag names
			onixFile = new ONIXFile(title, OnixPartsBuilder.REFERENCETAG, "3.0");
			onixFile.generate(destDir);
			
			// generate ONIX 3.0 file with short tag names
			onixFile = new ONIXFile(title, OnixPartsBuilder.SHORTTAG, "3.0");
			onixFile.generate(destDir);
			
			ArrayList<testdatagen.model.files.File> fileList = title.getNonCoverFiles();
			for(testdatagen.model.files.File file : fileList)
			{
				file.generate(destDir);
			}
			progress++;
			int progressPercent = (int)(100.0 / (float)tasks * progress);
			setProgress(progressPercent);
		}
		return null;
	}
}