package net.einsteinsci.betterbeginnings.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by einsteinsci on 8/22/2014.
 */
public class PacketNetherBrickOvenFuelLevel implements IMessage
{
	public static final int INTSIZE = 4;

	int xPos, yPos, zPos;

	FluidStack fluid;

	public static class PacketHandler implements IMessageHandler<PacketNetherBrickOvenFuelLevel, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNetherBrickOvenFuelLevel message, MessageContext ctx)
		{
			EntityPlayer player = ModMain.proxy.getPlayerFromMessageContext(ctx);

			TileEntityNetherBrickOven oven = (TileEntityNetherBrickOven)player.worldObj.getTileEntity(
					message.xPos, message.yPos, message.zPos);
			oven.setFuelLevel(message.fluid);

			return null;
		}
	}

	public PacketNetherBrickOvenFuelLevel()
	{
		xPos = 0;
		yPos = 0;
		zPos = 0;

		fluid = null;
	}

	public PacketNetherBrickOvenFuelLevel(int x, int y, int z, FluidStack fuel)
	{
		xPos = x;
		yPos = y;
		zPos = z;

		fluid = fuel;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		xPos = ByteBufUtils.readVarInt(buf, INTSIZE);
		yPos = ByteBufUtils.readVarInt(buf, INTSIZE);
		zPos = ByteBufUtils.readVarInt(buf, INTSIZE);

		int fluidId = ByteBufUtils.readVarInt(buf, INTSIZE);
		int level = ByteBufUtils.readVarInt(buf, INTSIZE);

		if (level != 0)
		{
			fluid = new FluidStack(fluidId, level);
		}
		else
		{
			fluid = null;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, xPos, INTSIZE);
		ByteBufUtils.writeVarInt(buf, yPos, INTSIZE);
		ByteBufUtils.writeVarInt(buf, zPos, INTSIZE);

		if (fluid != null)
		{
			ByteBufUtils.writeVarInt(buf, fluid.getFluid().getID(), INTSIZE);
			ByteBufUtils.writeVarInt(buf, fluid.amount, INTSIZE);
		}
		else
		{
			ByteBufUtils.writeVarInt(buf, 0, INTSIZE);
			ByteBufUtils.writeVarInt(buf, 0, INTSIZE);
		}
	}
}
