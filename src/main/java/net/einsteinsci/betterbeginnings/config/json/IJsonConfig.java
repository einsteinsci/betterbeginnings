package net.einsteinsci.betterbeginnings.config.json;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;
import java.util.List;

public interface IJsonConfig
{
	String getSubFolder();

	String getMainJson(File subfolder); // later
	String getAutoJson(File subfolder); // later
	String getCustomJson(File subfolder);

	List<String> getIncludedJson(File subfolder);

	void loadJsonConfig(FMLInitializationEvent e, String mainJson, String autoJson, String customJson);
	void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons);

	void savePostLoad(File subfolder);
}
