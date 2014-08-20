package net.einsteinsci.betterbeginnings.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityRepairTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class RepairTableRepairPacket implements IMessage
{
	public int xPos, yPos, zPos;

	public static class Handler implements IMessageHandler<RepairTableRepairPacket, IMessage>
	{
		@Override
		public IMessage onMessage(RepairTableRepairPacket message, MessageContext ctx)
		{
			EntityPlayer player = ModMain.proxy.getPlayerFromMessageContext(ctx);
			TileEntity entity = player.getEntityWorld().getTileEntity(message.xPos, message.yPos, message.zPos);
			TileEntityRepairTable repairTable = (TileEntityRepairTable)entity;
			if (repairTable != null)
			{
				repairTable.repairCenter();
			}

			return null;
		}
	}

	public RepairTableRepairPacket(int x, int y, int z)
	{
		xPos = x;
		yPos = y;
		zPos = z;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		xPos = ByteBufUtils.readVarShort(buf);
		yPos = ByteBufUtils.readVarShort(buf);
		zPos = ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarShort(buf, xPos);
		ByteBufUtils.writeVarShort(buf, yPos);
		ByteBufUtils.writeVarShort(buf, zPos);
	}

}
