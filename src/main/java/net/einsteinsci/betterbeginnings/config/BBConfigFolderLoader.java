package net.einsteinsci.betterbeginnings.config;

import net.einsteinsci.betterbeginnings.config.json.BBJsonLoader;
import net.einsteinsci.betterbeginnings.config.json.IJsonConfig;
import net.einsteinsci.betterbeginnings.config.json.KilnConfig;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;

public class BBConfigFolderLoader
{
	public static final String FOLDERNAME = "betterbeginnings";
	public static final String CONFIG_FILENAME = "betterbeginnings.cfg";

	private static File configFolder;

	public static Configuration getConfigFile(FMLPreInitializationEvent e)
	{
		configFolder = new File(e.getModConfigurationDirectory(), FOLDERNAME);

		File file = new File(e.getModConfigurationDirectory(), FOLDERNAME + "/" + CONFIG_FILENAME);
		Configuration res = new Configuration(file);

		return res;
	}

	public static void loadRecipes(FMLInitializationEvent e)
	{
		BBJsonLoader.initialize();

		loadJsonConfig(e, KilnConfig.INSTANCE);
	}

	public static void loadJsonConfig(FMLInitializationEvent e, IJsonConfig config)
	{
		File subfolder = new File(configFolder, config.getSubFolder());
		subfolder.mkdirs();

		String main = config.getMainJson(subfolder);
		String auto = config.getAutoJson(subfolder);
		String custom = config.getCustomJson(subfolder);

		config.loadJsonConfig(e, main, auto, custom);

		config.savePostLoad(subfolder);
	}
}
