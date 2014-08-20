package net.einsteinsci.betterbeginnings.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class InventoryBrickOvenMatrix extends InventoryCrafting implements IInventory
{
	private static final String __OBFID = "CL_00001743";
	/**
	 * List of the stacks in the crafting matrix.
	 */
	private ItemStack[] stackList;
	/**
	 * the width of the crafting inventory
	 */
	private int inventoryWidth;
	/**
	 * Class containing the callbacks for the events on_GUIClosed and
	 * on_CraftMaxtrixChanged.
	 */
	private Container eventHandler;

	public InventoryBrickOvenMatrix(Container container, int width, int height)
	{
		super(container, width, height);
		int k = width * height;
		stackList = new ItemStack[k];
		eventHandler = container;
		inventoryWidth = width;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return stackList.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return slot >= getSizeInventory() ? null : stackList[slot];
	}

	/**
	 * Returns the itemstack in the slot specified (Top left is 0, 0). Args:
	 * row, column
	 */
	@Override
	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		if (row >= 0 && row < inventoryWidth)
		{
			int k = row + column * inventoryWidth;
			return getStackInSlot(k);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the customName of the inventory
	 */
	@Override
	public String getInventoryName()
	{
		return "container.brickovenmatrix";
	}

	/**
	 * Returns if the inventory is named
	 */
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
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

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
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

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		stackList[slot] = stack;
		eventHandler.onCraftMatrixChanged(this);
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved
	 * to disk later - the game won't think it hasn't changed and skip it.
	 */
	@Override
	public void markDirty()
	{
	}

	/**
	 * Do not make give this method the customName canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item)
	{
		return true;
	}
}
