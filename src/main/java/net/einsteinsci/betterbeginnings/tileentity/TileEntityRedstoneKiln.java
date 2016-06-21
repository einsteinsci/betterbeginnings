package net.einsteinsci.betterbeginnings.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockRedstoneKiln;
import net.einsteinsci.betterbeginnings.inventory.BatterySpecializedFurnace;
import net.einsteinsci.betterbeginnings.inventory.ContainerRedstoneKiln;

import net.einsteinsci.betterbeginnings.network.PacketPoweredBBFurnaceEnergy;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

public class TileEntityRedstoneKiln extends TileEntityKilnBase implements IEnergyReceiver, ITileEntityPoweredBBFurnace
{
	public static final int MAX_RF = 100000; // 100,000
	public static final int SMELT_COST = 1500;
	public static final int CHARGE_RATE = 15;

	private BatterySpecializedFurnace battery;

	public TileEntityRedstoneKiln()
	{
		super();
		processTime = 200;
		burnTime = 1;

		battery = new BatterySpecializedFurnace(MAX_RF);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[SLOT_FUEL]);
		battery.setEnergyStored(tagCompound.getInteger("Energy"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("Energy", battery.getEnergyStored());
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		return 1;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{

			//if (burnTime > 0)
			//{
			//	--burnTime;
			//}
			//
			//if (burnTime == 0 && canSmelt())
			//{
			//	currentItemBurnLength = burnTime = getItemBurnTime(specialFurnaceStacks[SLOT_FUEL]);
			//
			//	if (burnTime > 0)
			//	{
			//		flag1 = true;
			//		if (specialFurnaceStacks[SLOT_FUEL] != null)
			//		{
			//			--specialFurnaceStacks[SLOT_FUEL].stackSize;
			//
			//			if (specialFurnaceStacks[SLOT_FUEL].stackSize == 0)
			//			{
			//				specialFurnaceStacks[SLOT_FUEL] = specialFurnaceStacks[SLOT_FUEL].getItem()
			//					.getContainerItem(specialFurnaceStacks[SLOT_FUEL]);
			//			}
			//		}
			//	}
			//}

			boolean couldSmelt = canSmelt();
			boolean dirty = false;

			if (isBurning() && canSmelt())
			{
				++cookTime;
				if (cookTime == processTime)
				{
					cookTime = 0;
					smeltItem();
					dirty = true;
				}
			}
			else
			{
				cookTime = 0;
			}

			// Charge battery from "fuel" slot
			ItemStack fuel = specialFurnaceStacks[SLOT_FUEL];
			if (fuel != null && fuel.getItem() instanceof IEnergyContainerItem)
			{
				IEnergyContainerItem capacitor = (IEnergyContainerItem)fuel.getItem();

				int extracted = capacitor.extractEnergy(fuel, CHARGE_RATE, true);

				if (battery.receiveEnergy(extracted, true) == extracted)
				{
					capacitor.extractEnergy(fuel, CHARGE_RATE, false);
					battery.receiveEnergy(extracted, false);

					updateNetwork();
					dirty = true;
				}
			}

			if (fuel != null && fuel.getItem() == Items.fire_charge) // DEBUG
			{
				battery.receiveEnergy(CHARGE_RATE, false);

				updateNetwork();
				dirty = true;
			}

			updateBlockState();

			if (couldSmelt != canSmelt())
			{
				dirty = true;
			}

			if (dirty)
			{
				markDirty();
			}
		}
	}

	@Override
	public boolean isBurning()
	{
		boolean hasEnergy = battery.extractEnergy(SMELT_COST, true) == SMELT_COST;
		return specialFurnaceStacks[SLOT_INPUT] != null && hasEnergy;
	}

	@Override
	public void smeltItem()
	{
		if (canSmelt())
		{
			int extr = battery.extractEnergy(SMELT_COST, false);
			LogUtil.log(Level.ERROR, "=== EXTRACTED: " + extr + " ===");
		}

		super.smeltItem();

		updateNetwork();
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.redstoneKiln";
	}

	@Override
	public void updateBlockState()
	{
		worldObj.markBlockForUpdate(pos);
		BlockRedstoneKiln.updateBlockState(isBurning(), worldObj, pos);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerRedstoneKiln(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":redstoneKiln";
	}

	@Override
	public BatterySpecializedFurnace getBattery()
	{
		return battery;
	}

	@Override
	public int getEnergyScaled(int px)
	{
		float outOfOne = (float)battery.getEnergyStored() / (float)battery.getMaxEnergyStored();
		return (int)(outOfOne * px);
	}

	@Override
	public void setEnergy(int rf)
	{
		battery.setEnergyStored(rf);
	}

	@Override
	public void updateNetwork()
	{
		NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
			worldObj.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 16.0d);
		ModMain.network.sendToAllAround(new PacketPoweredBBFurnaceEnergy(
			pos, battery.getEnergyStored()), point);
	}

	// region IEnergyReceiver
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return battery.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing from)
	{
		return battery.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return battery.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{
		return true;
	}
	// endregion IEnergyReceiver
}
