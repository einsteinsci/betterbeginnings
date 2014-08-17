package net.einsteinsci.betterbeginnings;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.einsteinsci.betterbeginnings.renderer.RenderCampfire;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
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

	public void registerTileEntitySpecialRenderer()
	{

	}
}
