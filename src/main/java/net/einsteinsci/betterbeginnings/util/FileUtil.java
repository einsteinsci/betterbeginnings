package net.einsteinsci.betterbeginnings.util;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileUtil
{
	public static String readAllText(File file)
	{
		if (!file.exists())
		{
			return null;
		}

		try
		{
			return new String(Files.readAllBytes(file.toPath()));
		}
		catch (IOException ex)
		{
			LogUtil.log(Level.ERROR, "IOException occurred opening " + file.getPath());
			LogUtil.log("");
			LogUtil.log(Level.ERROR, ex.toString());
			return null;
		}
	}

	public static boolean overwriteAllText(File file, String text)
	{
		if (file.exists())
		{
			if (!file.delete())
			{
				LogUtil.log(Level.ERROR, "Could not delete file to be overwritten: " + file.getPath());
				return false;
			}
		}

		try
		{
			Files.write(file.toPath(), text.getBytes(), StandardOpenOption.CREATE);
			return true;
		}
		catch (IOException e)
		{
			LogUtil.log(Level.ERROR, "IOException occurred saving " + file.getPath());
			LogUtil.log("");
			LogUtil.log(Level.ERROR, e.toString());
			return false;
		}
	}
}
