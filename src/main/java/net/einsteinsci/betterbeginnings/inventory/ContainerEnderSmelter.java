package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityEnderSmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEnderSmelter extends Container
{
	private TileEntityEnderSmelter smelter;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerEnderSmelter(InventoryPlayer playerInv, TileEntityEnderSmelter tileSmelter)
	{
		smelter = tileSmelter;
		addSlotToContainer(new Slot(tileSmelter, TileEntityEnderSmelter.INPUT, 46, 17));
		addSlotToContainer(new Slot(tileSmelter, TileEntityEnderSmelter.FUEL, 56, 53));
		addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tileSmelter,
		                                         TileEntityEnderSmelter.OUTPUT, 116, 35));
		addSlotToContainer(new Slot(tileSmelter, TileEntityEnderSmelter.GRAVEL, 66, 17));

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
	public void onCraftGuiOpened(ICrafting craft)
	{
		super.onCraftGuiOpened(craft);

		craft.sendProgressBarUpdate(this, 0, smelter.smelterCookTime);
		craft.sendProgressBarUpdate(this, 1, smelter.smelterBurnTime);
		craft.sendProgressBarUpdate(this, 2, smelter.currentItemBurnLength);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (Object listItem : crafters)
		{
			ICrafting crafter = (ICrafting)listItem;

			if (lastCookTime != smelter.smelterCookTime)
			{
				crafter.sendProgressBarUpdate(this, 0, smelter.smelterCookTime);
			}
			if (lastBurnTime != smelter.smelterBurnTime)
			{
				crafter.sendProgressBarUpdate(this, 1, smelter.smelterBurnTime);
			}
			if (lastItemBurnTime != smelter.currentItemBurnLength)
			{
				crafter.sendProgressBarUpdate(this, 2, smelter.currentItemBurnLength);
			}
		}

		lastBurnTime = smelter.smelterBurnTime;
		lastCookTime = smelter.smelterCookTime;
		lastItemBurnTime = smelter.currentItemBurnLength;
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

			if (fromId == TileEntityEnderSmelter.OUTPUT)
			{
				if (!mergeItemStack(movedStack, 4, 40, true))
				{
					return null;
				}
				slot.onSlotChange(movedStack, movedStackDupe);
			}
			else if (fromId != TileEntityEnderSmelter.FUEL && fromId != TileEntityEnderSmelter.INPUT &&
					fromId != TileEntityEnderSmelter.GRAVEL)
			{
				if (SmelterRecipeHandler.smelting().getSmeltingResult(movedStack) != null)
				{
					if (!mergeItemStack(movedStack,
					                    TileEntityEnderSmelter.INPUT,
					                    TileEntityEnderSmelter.INPUT + 1,
					                    false))
					{
						return null;
					}
				}
				else if (movedStack.getItem() == Item.getItemFromBlock(Blocks.gravel))
				{
					if (!mergeItemStack(movedStack,
					                    TileEntityEnderSmelter.GRAVEL,
					                    TileEntityEnderSmelter.GRAVEL + 1,
					                    false))
					{
						return null;
					}
				}
				else if (TileEntityEnderSmelter.isItemFuel(movedStack))
				{
					if (!mergeItemStack(movedStack,
					                    TileEntityEnderSmelter.FUEL,
					                    TileEntityEnderSmelter.FUEL + 1,
					                    false))
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

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			smelter.smelterCookTime = par2;
		}
		if (par1 == 1)
		{
			smelter.smelterBurnTime = par2;
		}
		if (par1 == 2)
		{
			smelter.currentItemBurnLength = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return smelter.isUseableByPlayer(player);
	}

	public boolean merge(ItemStack stack, int startSlot, int endSlot, boolean searchFromBottom)
	{
		return mergeItemStack(stack, startSlot, endSlot, searchFromBottom);
	}
}
