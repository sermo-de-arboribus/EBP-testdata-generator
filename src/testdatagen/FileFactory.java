package testdatagen;

import testdatagen.model.Title;
import testdatagen.model.files.File;

public interface FileFactory
{
	 public File generateFile(String format, Title title);
}