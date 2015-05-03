package net.einsteinsci.betterbeginnings.network;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

public class ClientProxy extends ServerProxy
{
	@Override
	public void registerRenderThings()
	{
		//Campfire
		//TileEntitySpecialRenderer render = new RenderCampfire();
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfire.class, render);
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
