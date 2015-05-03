package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.register.recipe.CampfireRecipes;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCampfire extends Container
{
	private static final int SLOT_INPUT = TileEntityCampfire.SLOT_INPUT;
	private static final int SLOT_OUTPUT = TileEntityCampfire.SLOT_OUTPUT;
	private static final int SLOT_PAN = TileEntityCampfire.SLOT_PAN;
	private static final int SLOT_FUEL = TileEntityCampfire.SLOT_FUEL;
	public int lastItemBurnTime;
	public int lastCookTime;
	private TileEntityCampfire tileCampfire;
	private int lastBurnTime;
	private int lastDecayTime;

	public ContainerCampfire(InventoryPlayer inventory, TileEntityCampfire campfire)
	{

		tileCampfire = campfire;
		addSlotToContainer(new Slot(tileCampfire, SLOT_INPUT, 58, 12));
		addSlotToContainer(new Slot(tileCampfire, SLOT_PAN, 32, 35));
		addSlotToContainer(new SlotFurnaceFuel(tileCampfire, SLOT_FUEL, 58, 57));
		addSlotToContainer(new SlotFurnaceOutput(inventory.player, campfire, SLOT_OUTPUT, 118, 34));

		int i;
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	public void addCraftingToCrafters(ICrafting craft)
	{
		super.addCraftingToCrafters(craft);

		craft.sendProgressBarUpdate(this, 0, tileCampfire.cookTime);
		craft.sendProgressBarUpdate(this, 1, tileCampfire.burnTime);
		craft.sendProgressBarUpdate(this, 2, tileCampfire.currentItemBurnTime);
		craft.sendProgressBarUpdate(this, 3, tileCampfire.decayTime);
	}

	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (Object crafter : crafters)
		{
			ICrafting craft = (ICrafting)crafter;

			if (lastCookTime != tileCampfire.cookTime)
			{
				craft.sendProgressBarUpdate(this, 0, tileCampfire.cookTime);
			}
			if (lastBurnTime != tileCampfire.burnTime)
			{
				craft.sendProgressBarUpdate(this, 1, tileCampfire.burnTime);
			}
			if (lastItemBurnTime != tileCampfire.currentItemBurnTime)
			{
				craft.sendProgressBarUpdate(this, 2, tileCampfire.currentItemBurnTime);
			}
			if (lastDecayTime != tileCampfire.decayTime)
			{
				craft.sendProgressBarUpdate(this, 3, tileCampfire.decayTime);
			}
		}

		lastBurnTime = tileCampfire.burnTime;
		lastCookTime = tileCampfire.cookTime;
		lastItemBurnTime = tileCampfire.currentItemBurnTime;
		lastDecayTime = tileCampfire.decayTime;
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
				if (CampfireRecipes.smelting().getSmeltingResult(itemstack1) != null)
				{
					if (!mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if (TileEntityCampfire.isItemFuel(itemstack1))
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
	public void updateProgressBar(int barId, int newValue)
	{
		if (barId == 0)
		{
			tileCampfire.cookTime = newValue;
		}
		if (barId == 1)
		{
			tileCampfire.burnTime = newValue;
		}
		if (barId == 2)
		{
			tileCampfire.currentItemBurnTime = newValue;
		}
		if (barId == 3)
		{
			tileCampfire.decayTime = newValue;
		}
	}

	public boolean canInteractWith(EntityPlayer player)
	{
		return tileCampfire.isUseableByPlayer(player);
	}

}
