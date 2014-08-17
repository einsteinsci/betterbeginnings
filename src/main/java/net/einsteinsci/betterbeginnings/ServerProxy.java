package net.einsteinsci.betterbeginnings;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;

public class ServerProxy
{
	public void registerRenderThings()
	{

	}

	public void registerTileEntitySpecialRenderer()
	{

	}

	public void registerNetworkStuff()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(ModMain.modInstance, new BBGuiHandler());
	}
}
