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

public class ContainerNetherBrickOven extends ContainerSpecializedFurnace
{
	public static final int FUEL_LEVEL_ID = 3;
	
	protected int lastFuelLevel;
	
	protected TileEntityNetherBrickOven tileEntityNetherBrickOven;
	
	public ContainerNetherBrickOven(InventoryPlayer playerInv, TileEntityNetherBrickOven tileEntityBrickOven)
	{
		tileSpecialFurnace = tileEntityBrickOven;
		tileEntityNetherBrickOven = tileEntityBrickOven;
		addSlotToContainer(new Slot(tileEntityBrickOven, TileEntityNetherBrickOven.FUEL, 17, 63));
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
	public void onCraftGuiOpened(ICrafting craft)
	{
		super.onCraftGuiOpened(craft);
		
		craft.sendProgressBarUpdate(this, FUEL_LEVEL_ID, tileSpecialFurnace.currentItemBurnLength);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (Object listItem : crafters)
		{
			ICrafting crafter = (ICrafting)listItem;
			
			if (lastFuelLevel != tileEntityNetherBrickOven.getFuelLevel())
			{
				crafter.sendProgressBarUpdate(this, FUEL_LEVEL_ID, tileEntityNetherBrickOven.getFuelLevel());
			}
		}
		
		lastFuelLevel = tileEntityNetherBrickOven.getFuelLevel();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int progressBar, int level)
	{
		super.updateProgressBar(progressBar, level);
		
		if (progressBar == FUEL_LEVEL_ID)
		{
			tileEntityNetherBrickOven.setFuelLevel(level);
		}
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
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.FUEL,
					                    TileEntityNetherBrickOven.FUEL + 1, false))
					{
						return null;
					}
				}
				else if (BrickOvenRecipeHandler.instance().isInRecipe(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.INPUTSTART,
					                    TileEntityNetherBrickOven.INPUTSTART + 9, false)) // move to craft matrix
					{
						return null;
					}
				}
				else if (!mergeItemStack(itemstack1, 38, 47, false)) // move to hotbar
				{
					return null;
				}
			}
			else if (slotId > 37 && slotId <= 46) // hotbar
			{
				if (FluidContainerRegistry.isBucket(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.FUEL,
					                    TileEntityNetherBrickOven.FUEL + 1, false))
					{
						return null;
					}
				}
				else if (BrickOvenRecipeHandler.instance().isInRecipe(itemstack1))
				{
					if (!mergeItemStack(itemstack1, TileEntityNetherBrickOven.INPUTSTART,
					                    TileEntityNetherBrickOven.INPUTSTART + 9, false)) // move to craft matrix
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
}
