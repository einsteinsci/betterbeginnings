package net.einsteinsci.betterbeginnings.config.json;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

public interface IJsonConfig
{
	String getSubFolder();

	String getMainJson(File subfolder); // later
	String getAutoJson(File subfolder); // later
	String getCustomJson(File subfolder);

	boolean isOnlyMain();

	void loadJsonConfig(FMLInitializationEvent e, String mainJson, String autoJson, String customJson);

	void savePostLoad(File subfolder);
}
