package net.einsteinsci.betterbeginnings.network;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

public class ServerProxy
{
	public void preInit(FMLPreInitializationEvent e)
	{

	}

	public void init(FMLInitializationEvent e)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(ModMain.modInstance, new BBGuiHandler());
	}

	public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
	{
		switch (ctx.side)
		{
			case CLIENT:
				LogUtil.log(Level.ERROR, "Message for CLIENT received for dedicated server");
				return null;
			case SERVER:
				return ctx.getServerHandler().playerEntity;
			default:
				LogUtil.log(Level.ERROR, "Invalid side in PacketHandler: " + ctx.side);
				return null;
		}
	}
}
