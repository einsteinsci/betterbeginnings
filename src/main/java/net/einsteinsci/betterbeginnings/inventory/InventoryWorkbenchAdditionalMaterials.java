package net.einsteinsci.betterbeginnings.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class InventoryWorkbenchAdditionalMaterials implements IInventory
{
	private ItemStack[] stackList;

	private Container eventHandler;

	public InventoryWorkbenchAdditionalMaterials(Container container, int size)
	{
		stackList = new ItemStack[size];
		eventHandler = container;
	}

	@Override
	public int getSizeInventory()
	{
		return stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (getSizeInventory() <= slot)
		{
			return null;
		}
		else
		{
			return stackList[slot];
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (stackList[slot] != null)
		{
			ItemStack itemstack;

			if (stackList[slot].stackSize <= amount)
			{
				itemstack = stackList[slot];
				stackList[slot] = null;
				eventHandler.onCraftMatrixChanged(this);
				return itemstack;
			}
			else
			{
				itemstack = stackList[slot].splitStack(amount);

				if (stackList[slot].stackSize == 0)
				{
					stackList[slot] = null;
				}

				eventHandler.onCraftMatrixChanged(this);
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (stackList[slot] != null)
		{
			ItemStack itemstack = stackList[slot];
			stackList[slot] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		stackList[slot] = stack;
		eventHandler.onCraftMatrixChanged(this);
	}

	@Override
	public String getName()
	{
		return "container.workbenchmaterials";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(getName());
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < stackList.length; i++)
		{
			stackList[i] = null;
		}
	}
}
