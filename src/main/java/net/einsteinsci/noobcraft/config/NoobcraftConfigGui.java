package net.einsteinsci.noobcraft.config;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class NoobcraftConfigGui extends GuiConfig
{
	public NoobcraftConfigGui(GuiScreen parent)
	{
		super(parent, new ConfigElement(ModMain.configFile.getCategory(Configuration.CATEGORY_GENERAL))
		.getChildElements(), ModMain.MODID, false, false,
		GuiConfig.getAbridgedConfigPath(ModMain.configFile.toString()));
	}
}
