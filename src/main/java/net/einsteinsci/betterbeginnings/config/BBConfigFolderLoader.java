package net.einsteinsci.betterbeginnings.config;

import net.einsteinsci.betterbeginnings.config.json.*;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BBConfigFolderLoader
{
	public static final String FOLDERNAME = "betterbeginnings";
	public static final String CONFIG_FILENAME = "betterbeginnings.cfg";

	private static File configFolder;

	public static Configuration getConfigFile(FMLPreInitializationEvent e)
	{
		configFolder = new File(e.getModConfigurationDirectory(), FOLDERNAME);

		File file = new File(e.getModConfigurationDirectory(), FOLDERNAME + "/" + CONFIG_FILENAME);
		return new Configuration(file);
	}

	public static void loadRecipes(FMLInitializationEvent e)
	{
		loadJsonConfig(e, KilnConfig.INSTANCE);
		loadJsonConfig(e, SmelterConfig.INSTANCE);
		loadJsonConfig(e, BrickOvenConfig.INSTANCE);
		loadJsonConfig(e, AdvancedCraftingConfig.INSTANCE);
		loadJsonConfig(e, CampfireConfig.INSTANCE);
		loadJsonConfig(e, RepairInfusionConfig.INSTANCE);
		loadJsonConfig(e, BoosterConfig.INSTANCE);
	}

	// Should be run before RemoveRecipes.remove() and .removeFurnaceRecipes()
	public static void loadRemovedRecipes(FMLInitializationEvent e)
	{
		BBJsonLoader.initialize();

		loadJsonConfig(e, RemovalConfig.INSTANCE);
	}

	public static void loadJsonConfig(FMLInitializationEvent e, IJsonConfig config)
	{
		File subfolder = new File(configFolder, config.getSubFolder());
		subfolder.mkdirs();

		String main = config.getMainJson(subfolder);
		String auto = config.getAutoJson(subfolder);
		String custom = config.getCustomJson(subfolder);

		config.loadJsonConfig(e, main, auto, custom);

		List<String> includes = config.getIncludedJson(subfolder);
		config.loadIncludedConfig(e, includes);

		config.savePostLoad(subfolder);
	}

	public static void saveAutoJson(IJsonConfig config)
	{
		File subfolder = new File(configFolder, config.getSubFolder());
		subfolder.mkdirs();

		config.saveAutoJson(subfolder);
	}
}
