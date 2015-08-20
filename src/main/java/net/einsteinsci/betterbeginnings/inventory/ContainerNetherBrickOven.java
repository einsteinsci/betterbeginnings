package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerNetherBrickOven extends Container
{
	private TileEntityNetherBrickOven tileBrickOven;
	private int lastCookTime;

	public ContainerNetherBrickOven(InventoryPlayer playerInv, TileEntityNetherBrickOven tileEntityBrickOven)
	{
		tileBrickOven = tileEntityBrickOven;
		addSlotToContainer(new Slot(tileEntityBrickOven, TileEntityNetherBrickOven.FUELINPUT, 17, 63));
		addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tileEntityBrickOven,
		                                         TileEntityNetherBrickOven.OUTPUT, 138, 35));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; j++)
			{
				addSlotToContainer(new Slot(tileEntityBrickOven, j + i * 3 + TileEntityNetherBrickOven.INPUTSTART,
				                            44 + j * 18, 17 + i * 18));
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
		}

		lastCookTime = tileBrickOven.ovenCookTime;
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

			if (slotId == TileEntityNetherBrickOven.OUTPUT)
			{
				if (!mergeItemStack(itemstack1, 11, 47, true)) // move to inventory (all, avoid hotbar)
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (slotId >= 11 && slotId < 38) // non-hotbar inventory
			{
				if (FluidContainerRegistry.isBucket(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.FUELINPUT,
					                    TileEntityNetherBrickOven.FUELINPUT + 1, false))
					{
						return null;
					}
				}
				else if (BrickOvenRecipeHandler.instance().isInRecipe(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.INPUTSTART,
					                    TileEntityNetherBrickOven.INPUTSTART + 9,
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
				if (FluidContainerRegistry.isBucket(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.FUELINPUT,
					                    TileEntityNetherBrickOven.FUELINPUT + 1, false))
					{
						return null;
					}
				}
				else if (BrickOvenRecipeHandler.instance().isInRecipe(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.INPUTSTART,
					                    TileEntityNetherBrickOven.INPUTSTART + 9,
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
				slot.putStack(null);
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
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileBrickOven.isUseableByPlayer(player);
	}
}
