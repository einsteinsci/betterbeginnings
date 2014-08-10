package net.einsteinsci.noobcraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RepairTableRepairPacket implements IMessage
{
	public ItemStack repairedStack;
	
	public RepairTableRepairPacket(ItemStack repaired)
	{
		repairedStack = repaired;
	}
	
	public static class Handler implements IMessageHandler<RepairTableRepairPacket, IMessage>
	{
		@Override
		public IMessage onMessage(RepairTableRepairPacket message, MessageContext ctx)
		{
			message.repairedStack.setItemDamage(0);
			return null;
		}
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
