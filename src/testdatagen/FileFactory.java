package testdatagen;

import testdatagen.model.files.File;

public abstract class FileFactory
{
	 public abstract File generateFile(String format, long ISBN);
}