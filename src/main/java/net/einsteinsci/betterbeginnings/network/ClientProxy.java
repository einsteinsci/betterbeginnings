package net.einsteinsci.betterbeginnings.network;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.client.InfusionRender;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityInfusionRepair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

public class ClientProxy extends ServerProxy
{
	@Override
	public void registerRenderThings()
	{
		// nothing here
		registerTileEntitySpecialRenderer();
	}

	@Override
	public void registerTileEntitySpecialRenderer()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfusionRepair.class,
			new InfusionRender(Minecraft.getMinecraft().getRenderManager()));
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
				ModMain.log(Level.ERROR, "Invalid side in TestMsgHandler: " + ctx.side);
		}
		return null;
	}
}
