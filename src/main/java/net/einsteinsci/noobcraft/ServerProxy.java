package net.einsteinsci.noobcraft;

import net.einsteinsci.noobcraft.gui.NoobCraftGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ServerProxy 
{
	public void registerRenderThings()
	{
		
	}
	
	public void registerNetworkStuff()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(NoobcraftMod.modInstance, new NoobCraftGuiHandler());
	}
}
