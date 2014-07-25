package net.einsteinsci.noobcraft.inventory;

import net.einsteinsci.noobcraft.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.noobcraft.tileentity.TileEntitySmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerSmelter extends Container
{
	private TileEntitySmelter smelter;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;
	
	public ContainerSmelter(InventoryPlayer playerInv, TileEntitySmelter tileEntitySmelter)
	{
		smelter = tileEntitySmelter;
		addSlotToContainer(new Slot(tileEntitySmelter, TileEntitySmelter.INPUT, 46, 17));
		addSlotToContainer(new Slot(tileEntitySmelter, TileEntitySmelter.GRAVEL, 66, 17));
		addSlotToContainer(new Slot(tileEntitySmelter, TileEntitySmelter.FUEL, 56, 53));
		addSlotToContainer(new SlotFurnace(playerInv.player, tileEntitySmelter, TileEntitySmelter.OUTPUT, 116, 35));
		
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
		
		craft.sendProgressBarUpdate(this, 0, smelter.smelterCookTime);
		craft.sendProgressBarUpdate(this, 1, smelter.smelterBurnTime);
		craft.sendProgressBarUpdate(this, 2, smelter.currentItemBurnLength);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting craft = (ICrafting)crafters.get(i);
			
			if (lastCookTime != smelter.smelterCookTime)
			{
				craft.sendProgressBarUpdate(this, 0, smelter.smelterCookTime);
			}
			if (lastBurnTime != smelter.smelterBurnTime)
			{
				craft.sendProgressBarUpdate(this, 1, smelter.smelterBurnTime);
			}
			if (lastItemBurnTime != smelter.currentItemBurnLength)
			{
				craft.sendProgressBarUpdate(this, 2, smelter.currentItemBurnLength);
			}
		}
		
		lastBurnTime = smelter.smelterBurnTime;
		lastCookTime = smelter.smelterCookTime;
		lastItemBurnTime = smelter.currentItemBurnLength;
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
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotid)
	{
		ItemStack movedStackDupe = null;
		Slot slot = (Slot)inventorySlots.get(slotid);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack movedStack = slot.getStack();
			movedStackDupe = movedStack.copy();
			
			if (slotid == 2)
			{
				if (!mergeItemStack(movedStack, 3, 39, true))
				{
					return null;
				}
				slot.onSlotChange(movedStack, movedStackDupe);
			}
			else if (slotid != 1 && slotid != 0)
			{
				if (SmelterRecipeHandler.smelting().getSmeltingResult(movedStack) != null)
				{
					if (!mergeItemStack(movedStack, 0, 1, false))
					{
						return null;
					}
				}
				else if (movedStack.getItem() == Item.getItemFromBlock(Blocks.gravel))
				{
					if (!mergeItemStack(movedStack, TileEntitySmelter.GRAVEL, 2, false))
					{
						return null;
					}
				}
				else if (TileEntitySmelter.isItemFuel(movedStack))
				{
					if (!mergeItemStack(movedStack, TileEntitySmelter.FUEL, 2, false))
					{
						return null;
					}
				}
				else if (slotid >= 4 && slotid < 31)
				{
					if (!mergeItemStack(movedStack, 30, 39, false))
					{
						return null;
					}
				}
				else if (slotid >= 31 && slotid < 40 && !mergeItemStack(movedStack, 4, 31, false))
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
				slot.putStack((ItemStack)null);
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
}
