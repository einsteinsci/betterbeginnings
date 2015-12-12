package net.einsteinsci.betterbeginnings.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

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
