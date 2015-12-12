package net.einsteinsci.betterbeginnings.util;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class LogUtil
{
	public static void logDebug(String text)
	{
		if (BBConfig.debugLogging)
		{
			log(Level.DEBUG, text);
		}
	}

	public static void logDebug(Level level, String text)
	{
		if (BBConfig.debugLogging)
		{
			log(level, text);
		}
	}

	public static void log(Level level, String text)
	{
		FMLLog.log(ModMain.NAME, level, text);
	}

	public static void log(String text)
	{
		log(Level.INFO, text);
	}
}
