package net.einsteinsci.betterbeginnings.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class RepairTableRepairPacket implements IMessage
{
	public ItemStack repairedStack;

	public static class Handler implements IMessageHandler<RepairTableRepairPacket, IMessage>
	{
		@Override
		public IMessage onMessage(RepairTableRepairPacket message, MessageContext ctx)
		{
			message.repairedStack.setItemDamage(0);
			return null;
		}
	}

	public RepairTableRepairPacket(ItemStack repaired)
	{
		repairedStack = repaired;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		repairedStack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeItemStack(buf, repairedStack);
	}

}
