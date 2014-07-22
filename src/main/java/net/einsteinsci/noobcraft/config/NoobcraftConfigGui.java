package net.einsteinsci.noobcraft.config;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class NoobcraftConfigGui extends GuiConfig
{
	public NoobcraftConfigGui(GuiScreen parent)
	{
		super(parent, new ConfigElement(NoobcraftMod.configFile.getCategory(Configuration.CATEGORY_GENERAL))
		.getChildElements(), NoobcraftMod.MODID, false, false,
		GuiConfig.getAbridgedConfigPath(NoobcraftMod.configFile.toString()));
	}
}
