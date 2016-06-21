package net.einsteinsci.betterbeginnings.network;

import io.netty.buffer.ByteBuf;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.ITileEntityPoweredBBFurnace;
import net.einsteinsci.betterbeginnings.tileentity.TileEntitySpecializedFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPoweredBBFurnaceEnergy implements IMessage
{
	public static class PacketHandler implements IMessageHandler<PacketPoweredBBFurnaceEnergy, IMessage>
	{
		@Override
		public IMessage onMessage(PacketPoweredBBFurnaceEnergy message, MessageContext ctx)
		{
			EntityPlayer player = ModMain.proxy.getPlayerFromMessageContext(ctx);

			TileEntitySpecializedFurnace tileEntity = (TileEntitySpecializedFurnace)player.worldObj.getTileEntity(message.pos);
			if (tileEntity instanceof ITileEntityPoweredBBFurnace)
			{
				ITileEntityPoweredBBFurnace itepbbf = (ITileEntityPoweredBBFurnace)tileEntity;
				itepbbf.setEnergy(message.energy);
			}

			return null;
		}
	}

	private BlockPos pos;

	private int energy;

	public PacketPoweredBBFurnaceEnergy()
	{
		this(BlockPos.ORIGIN, 0);
	}
	public PacketPoweredBBFurnaceEnergy(BlockPos _pos, int _energy)
	{
		pos = _pos;
		energy = _energy;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		energy = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		buf.writeInt(energy);
	}
}
