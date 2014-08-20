package net.einsteinsci.betterbeginnings.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Level;

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

	public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
	{
		switch (ctx.side)
		{
			case CLIENT:
				ModMain.Log(Level.ERROR, "Message for CLIENT received for dedicated server");
				return null;
			case SERVER:
				return ctx.getServerHandler().playerEntity;
			default:
				ModMain.Log(Level.ERROR, "Invalid side in RepairTableRepairPacket.Handler: " + ctx.side);
				return null;
		}
	}
}
