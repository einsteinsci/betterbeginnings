package net.einsteinsci.betterbeginnings.network;

import net.einsteinsci.betterbeginnings.client.InfusionRender;
import net.einsteinsci.betterbeginnings.client.RegisterMetaItemRenders;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityInfusionRepair;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

public class ClientProxy extends ServerProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		registerTileEntitySpecialRenderers();
	}

	@Override
	public void init(FMLInitializationEvent e)
	{
		RegisterMetaItemRenders.init();
	}

	public void registerTileEntitySpecialRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfusionRepair.class, new InfusionRender());
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
				LogUtil.log(Level.ERROR, "Invalid side in TestMsgHandler: " + ctx.side);
		}
		return null;
	}
}
