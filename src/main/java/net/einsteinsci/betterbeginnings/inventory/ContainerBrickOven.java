package net.einsteinsci.betterbeginnings.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerBrickOven extends Container
{
	private TileEntityBrickOven tileBrickOven;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerBrickOven(InventoryPlayer playerInv, TileEntityBrickOven tileEntityBrickOven)
	{
		tileBrickOven = tileEntityBrickOven;
		addSlotToContainer(new Slot(tileEntityBrickOven, TileEntityBrickOven.FUEL, 92, 58));
		addSlotToContainer(new SlotFurnace(playerInv.player, tileEntityBrickOven, TileEntityBrickOven.OUTPUT, 124, 21));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; j++)
			{
				addSlotToContainer(new Slot(tileEntityBrickOven, j + i * 3 + TileEntityBrickOven.INPUTSTART,
											30 + j * 18, 17 + i * 18));
			}
		}

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

		craft.sendProgressBarUpdate(this, 0, tileBrickOven.ovenCookTime);
		craft.sendProgressBarUpdate(this, 1, tileBrickOven.ovenBurnTime);
		craft.sendProgressBarUpdate(this, 2, tileBrickOven.currentItemBurnLength);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting craft = (ICrafting)crafters.get(i);

			if (lastCookTime != tileBrickOven.ovenCookTime)
			{
				craft.sendProgressBarUpdate(this, 0, tileBrickOven.ovenCookTime);
			}
			if (lastBurnTime != tileBrickOven.ovenBurnTime)
			{
				craft.sendProgressBarUpdate(this, 1, tileBrickOven.ovenBurnTime);
			}
			if (lastItemBurnTime != tileBrickOven.currentItemBurnLength)
			{
				craft.sendProgressBarUpdate(this, 2, tileBrickOven.currentItemBurnLength);
			}
		}

		lastBurnTime = tileBrickOven.ovenBurnTime;
		lastCookTime = tileBrickOven.ovenCookTime;
		lastItemBurnTime = tileBrickOven.currentItemBurnLength;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(slotId);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotId == TileEntityBrickOven.OUTPUT)
			{
				if (!mergeItemStack(itemstack1, 11, 47, true)) // move to inventory (all, avoid hotbar)
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (slotId >= 11 && slotId < 38) // non-hotbar inventory
			{
				if (TileEntityBrickOven.isItemFuel(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityBrickOven.FUEL, TileEntityBrickOven.FUEL + 1, false))
					{
						return null;
					}
				}
				else if (BrickOvenRecipeHandler.instance().isInRecipe(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityBrickOven.INPUTSTART, TileEntityBrickOven.INPUTSTART + 9,
										false)) // move to craft matrix
					{
						return null;
					}
				}
				else if (!mergeItemStack(itemstack1, 38, 47, false)) // move to hotbar
				{
					return null;
				}
			}
			else if (slotId >= 37 && slotId < 46) // hotbar
			{
				if (TileEntityBrickOven.isItemFuel(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityBrickOven.FUEL, TileEntityBrickOven.FUEL + 1, false))
					{
						return null;
					}
				}
				else if (BrickOvenRecipeHandler.instance().isInRecipe(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityBrickOven.INPUTSTART, TileEntityBrickOven.INPUTSTART + 9,
										false)) // move to craft matrix
					{
						return null;
					}
				}
				else if (!mergeItemStack(itemstack1, 11, 38, false)) // move to inventory (non-hotbar)
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 11, 47, false)) // move to inventory (all)
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
			tileBrickOven.ovenCookTime = par2;
		}
		if (par1 == 1)
		{
			tileBrickOven.ovenBurnTime = par2;
		}
		if (par1 == 2)
		{
			tileBrickOven.currentItemBurnLength = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileBrickOven.isUseableByPlayer(player);
	}
}
