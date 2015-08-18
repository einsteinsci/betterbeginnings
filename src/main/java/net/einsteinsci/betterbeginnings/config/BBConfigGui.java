package net.einsteinsci.betterbeginnings.config;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class BBConfigGui extends GuiConfig
{
	public BBConfigGui(GuiScreen parent)
	{
		super(parent, getAllCategories(), ModMain.MODID, false, false,
		GuiConfig.getAbridgedConfigPath(ModMain.configFile.toString()));
	}

	public static List<IConfigElement> getAllCategories()
	{
		List<IConfigElement> all = new ConfigElement(ModMain.configFile.getCategory(BBConfig.GENERAL))
				.getChildElements();
		all.addAll(new ConfigElement(ModMain.configFile.getCategory(BBConfig.CRAFTING)).getChildElements());
		all.addAll(new ConfigElement(ModMain.configFile.getCategory(BBConfig.SMELTING)).getChildElements());
		all.addAll(new ConfigElement(ModMain.configFile.getCategory(BBConfig.MOBDROPS)).getChildElements());
		all.addAll(new ConfigElement(ModMain.configFile.getCategory(BBConfig.TWEAKS)).getChildElements());
		all.addAll(new ConfigElement(ModMain.configFile.getCategory(BBConfig.WORLDGEN)).getChildElements());

		return all;
	}
}
