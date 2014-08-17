package net.einsteinsci.betterbeginnings.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipes;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerKiln extends Container
{
	private TileEntityKiln tileKiln;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerKiln(InventoryPlayer playerInv, TileEntityKiln tileEntityKiln)
	{
		tileKiln = tileEntityKiln;
		addSlotToContainer(new Slot(tileEntityKiln, 0, 56, 17));
		addSlotToContainer(new Slot(tileEntityKiln, 1, 56, 53));
		addSlotToContainer(new SlotFurnace(playerInv.player, tileEntityKiln, 2, 116, 35));

		int i;
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting craft)
	{
		super.addCraftingToCrafters(craft);

		craft.sendProgressBarUpdate(this, 0, tileKiln.kilnCookTime);
		craft.sendProgressBarUpdate(this, 1, tileKiln.kilnBurnTime);
		craft.sendProgressBarUpdate(this, 2, tileKiln.currentBurnTime);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting craft = (ICrafting)crafters.get(i);

			if (lastCookTime != tileKiln.kilnCookTime)
			{
				craft.sendProgressBarUpdate(this, 0, tileKiln.kilnCookTime);
			}
			if (lastBurnTime != tileKiln.kilnBurnTime)
			{
				craft.sendProgressBarUpdate(this, 1, tileKiln.kilnBurnTime);
			}
			if (lastItemBurnTime != tileKiln.currentBurnTime)
			{
				craft.sendProgressBarUpdate(this, 2, tileKiln.currentBurnTime);
			}
		}

		lastBurnTime = tileKiln.kilnBurnTime;
		lastCookTime = tileKiln.kilnCookTime;
		lastItemBurnTime = tileKiln.currentBurnTime;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 2)
			{
				if (!mergeItemStack(itemstack1, 3, 39, true))
				{
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 != 1 && par2 != 0)
			{
				if (KilnRecipes.smelting().getSmeltingResult(itemstack1) != null)
				{
					if (!mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if (TileEntityKiln.isItemFuel(itemstack1))
				{
					if (!mergeItemStack(itemstack1, 1, 2, false))
					{
						return null;
					}
				}
				else if (par2 >= 3 && par2 < 30)
				{
					if (!mergeItemStack(itemstack1, 30, 39, false))
					{
						return null;
					}
				}
				else if (par2 >= 30 && par2 < 39 && !mergeItemStack(itemstack1, 3, 30, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 3, 39, false))
			{
				return null;
			}
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			tileKiln.kilnCookTime = par2;
		}
		if (par1 == 1)
		{
			tileKiln.kilnBurnTime = par2;
		}
		if (par1 == 2)
		{
			tileKiln.currentBurnTime = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileKiln.isUseableByPlayer(player);
	}
	
	/*
	 * @Override public ItemStack transferStackInSlot(EntityPlayer player, int
	 * intoSlot) { ItemStack stack = null; Slot slot =
	 * (Slot)this.inventorySlots.get(intoSlot);
	 * 
	 * if (slot != null && slot.getHasStack()) { ItemStack itemStack1 =
	 * slot.getStack(); stack = itemStack1.copy();
	 * 
	 * if (intoSlot == 2) { if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
	 * return null; } slot.onSlotChange(itemStack1, stack); } else if (intoSlot
	 * != 1 && intoSlot != 0) { if
	 * (FurnaceRecipes.smelting().getSmeltingResult(itemStack1) != null) { if
	 * (!this.mergeItemStack(itemStack1, 0, 1, false)) { return null; } } else
	 * if (TileEntityKiln.isItemFuel(itemStack1)) { if
	 * (!this.mergeItemStack(itemStack1, 1, 2, false)) { return null; } } else
	 * if (intoSlot >= 3 && intoSlot < 30) { if
	 * (!this.mergeItemStack(itemStack1, 30, 39, false)) { return null; } } else
	 * if (intoSlot >= 30 && intoSlot < 39 && !this.mergeItemStack(itemStack1,
	 * 3, 30, false)) { return null; } else if (!this.mergeItemStack(itemStack1,
	 * 3, 39, false)) { return null; }
	 * 
	 * if (itemStack1.stackSize == 0) { slot.putStack(null); } else {
	 * slot.onSlotChanged(); }
	 * 
	 * if (itemStack1.stackSize == stack.stackSize) { return null; }
	 * 
	 * slot.onPickupFromSlot(player, itemStack1); }
	 * 
	 * return stack; }
	 * 
	 * return null; }
	 */

}
