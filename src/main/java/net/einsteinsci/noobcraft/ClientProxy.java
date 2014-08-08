package net.einsteinsci.noobcraft;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.einsteinsci.noobcraft.renderer.RenderCampfire;
import net.einsteinsci.noobcraft.tileentity.TileEntityCampfire;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class ClientProxy extends ServerProxy
{
	@Override
	public void registerRenderThings()
	{
		//Campfire
		TileEntitySpecialRenderer render = new RenderCampfire();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfire.class, render);
	}
	public void registerTileEntitySpecialRenderer(){
		
	}
}
