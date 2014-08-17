package net.einsteinsci.betterbeginnings.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class BBConfigGui extends GuiConfig
{
	public BBConfigGui(GuiScreen parent)
	{
		super(parent, new ConfigElement(ModMain.configFile.getCategory(Configuration.CATEGORY_GENERAL))
		.getChildElements(), ModMain.MODID, false, false,
		GuiConfig.getAbridgedConfigPath(ModMain.configFile.toString()));
	}
}
