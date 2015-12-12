package net.einsteinsci.betterbeginnings.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class BBConfigFolderLoader
{
	public static final String FOLDERNAME = "betterbeginnings";
	public static final String CONFIG_FILENAME = "betterbeginnings.cfg";

	public static Configuration getConfigFile(FMLPreInitializationEvent e)
	{
		File file = new File(e.getModConfigurationDirectory(), FOLDERNAME + "/" + CONFIG_FILENAME);
		Configuration res = new Configuration(file);

		return res;
	}
}
