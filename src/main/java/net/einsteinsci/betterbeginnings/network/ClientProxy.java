package net.einsteinsci.betterbeginnings.network;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.renderer.RenderCampfire;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Level;

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

	@Override
	public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
	{
		switch (ctx.side)
		{
			case CLIENT:
			{
				return Minecraft.getMinecraft().thePlayer;
			}
			case SERVER:
			{
				return ctx.getServerHandler().playerEntity;
			}
			default:
				ModMain.Log(Level.ERROR, "Invalid side in TestMsgHandler: " + ctx.side);
		}
		return null;
	}
}
