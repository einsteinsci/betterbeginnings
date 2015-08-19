package net.einsteinsci.betterbeginnings.network;

import io.netty.buffer.ByteBuf;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCampfireState implements IMessage
{
	BlockPos pos;

	byte state;

	public static class PacketHandler implements IMessageHandler<PacketCampfireState, IMessage>
	{
		@Override
		public IMessage onMessage(PacketCampfireState msg, MessageContext ctx)
		{
			EntityPlayer player = ModMain.proxy.getPlayerFromMessageContext(ctx);

			TileEntityCampfire campfire = (TileEntityCampfire)player.worldObj.getTileEntity(msg.pos);

			if (campfire != null)
			{
				campfire.campfireState = msg.state;
			}

			return null;
		}
	}

	public PacketCampfireState()
	{
		pos = new BlockPos(0, 0, 0);

		state = 0;
	}

	public PacketCampfireState(BlockPos _pos, byte _state)
	{
		pos = _pos;

		state = _state;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

		state = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		buf.writeByte(state);
	}
}
