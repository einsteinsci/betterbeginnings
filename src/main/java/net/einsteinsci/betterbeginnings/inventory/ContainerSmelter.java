package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntitySmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerSmelter extends ContainerSpecializedFurnace
{
	public ContainerSmelter(InventoryPlayer playerInv, TileEntitySmelter tileEntitySmelter)
	{
		tileSpecialFurnace = tileEntitySmelter;
		addSlotToContainer(new Slot(tileEntitySmelter, TileEntitySmelter.INPUT, 46, 17));
		addSlotToContainer(new Slot(tileEntitySmelter, TileEntitySmelter.FUEL, 56, 53));
		addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tileEntitySmelter,
		                                         TileEntitySmelter.OUTPUT, 116, 35));
		addSlotToContainer(new Slot(tileEntitySmelter, TileEntitySmelter.GRAVEL, 66, 17));

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
	public ItemStack transferStackInSlot(EntityPlayer player, int fromId)
	{
		ItemStack movedStackDupe = null;
		Slot slot = (Slot)inventorySlots.get(fromId);

		if (slot != null && slot.getHasStack())
		{
			ItemStack movedStack = slot.getStack();
			movedStackDupe = movedStack.copy();

			if (fromId == TileEntitySmelter.OUTPUT)
			{
				if (!mergeItemStack(movedStack, 4, 40, true))
				{
					return null;
				}
				slot.onSlotChange(movedStack, movedStackDupe);
			}
			else if (fromId != TileEntitySmelter.FUEL && fromId != TileEntitySmelter.INPUT &&
					fromId != TileEntitySmelter.GRAVEL)
			{
				if (SmelterRecipeHandler.instance().getSmeltingResult(movedStack) != null)
				{
					if (!mergeItemStack(movedStack, TileEntitySmelter.INPUT, TileEntitySmelter.INPUT + 1, false))
					{
						return null;
					}
				}
				else if (movedStack.getItem() == Item.getItemFromBlock(Blocks.gravel))
				{
					if (!mergeItemStack(movedStack, TileEntitySmelter.GRAVEL, TileEntitySmelter.GRAVEL + 1, false))
					{
						return null;
					}
				}
				else if (TileEntitySmelter.isItemFuel(movedStack))
				{
					if (!mergeItemStack(movedStack, TileEntitySmelter.FUEL, TileEntitySmelter.FUEL + 1, false))
					{
						return null;
					}
				}
				else if (fromId >= 4 && fromId < 31)
				{
					if (!mergeItemStack(movedStack, 31, 40, false))
					{
						return null;
					}
				}
				else if (fromId >= 31 && fromId < 40 && !mergeItemStack(movedStack, 4, 31, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(movedStack, 4, 40, false))
			{
				return null;
			}
			if (movedStack.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (movedStack.stackSize == movedStackDupe.stackSize)
			{
				return null;
			}
			slot.onPickupFromSlot(player, movedStack);
		}
		return movedStackDupe;
	}

	public boolean merge(ItemStack stack, int startSlot, int endSlot, boolean searchFromBottom)
	{
		return mergeItemStack(stack, startSlot, endSlot, searchFromBottom);
	}
}
