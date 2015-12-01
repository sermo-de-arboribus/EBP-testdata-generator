package testdatagen;

import testdatagen.model.Title;
import testdatagen.model.files.File;

public abstract class FileFactory
{
	 public abstract File generateFile(String format, Title title);
}