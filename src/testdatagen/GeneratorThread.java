package testdatagen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import testdatagen.model.Title;
import testdatagen.model.files.GraphicFile;

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
			// TODO: generate cover
			ArrayList<GraphicFile> coversList = title.getCoverFiles();
			for(GraphicFile coverFile : coversList)
			{
				coverFile.generate(title, destDir);
			}
			// TODO: check, if cover is handled by MediaFileLink
			// TODO: generate ONIX file
			// TODO: iterate over all file Objects and generate the files
		}
	}
}