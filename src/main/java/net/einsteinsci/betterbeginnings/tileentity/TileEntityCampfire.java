package net.einsteinsci.betterbeginnings.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCampfire extends TileEntity implements IInventory
{

	public int cookTime;
	public int burnTime;
	public int currentItemBurnTime;

	public static boolean isItemFuel(ItemStack itemstack1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBurning()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int getCookProgressScaled(int i)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBurnTimeRemainingScaled(int i)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSizeInventory()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getInventoryName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
