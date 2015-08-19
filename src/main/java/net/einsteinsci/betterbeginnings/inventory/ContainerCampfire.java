package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.items.ItemPan;
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
	private static final int SLOT_FUEL = TileEntityCampfire.SLOT_FUEL;
	private static final int SLOT_PAN = TileEntityCampfire.SLOT_PAN;
	public int lastItemBurnTime;
	public int lastCookTime;
	private TileEntityCampfire tileCampfire;
	private int lastBurnTime;
	private int lastDecayTime;

	public ContainerCampfire(InventoryPlayer inventory, TileEntityCampfire campfire)
	{

		tileCampfire = campfire;
		addSlotToContainer(new Slot(tileCampfire, SLOT_INPUT, 58, 12));
		addSlotToContainer(new SlotFurnaceOutput(inventory.player, campfire, SLOT_OUTPUT, 118, 34));
		addSlotToContainer(new SlotFurnaceFuel(tileCampfire, SLOT_FUEL, 58, 57));
		addSlotToContainer(new Slot(tileCampfire, SLOT_PAN, 32, 35));

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
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstackCopy = null;
		Slot slot = (Slot)inventorySlots.get(slotId);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack = slot.getStack();
			itemstackCopy = itemstack.copy();

			if (slotId == SLOT_OUTPUT)
			{
				if (!mergeItemStack(itemstack, SLOT_PAN + 1, 39, true))
				{
					return null;
				}
				slot.onSlotChange(itemstack, itemstackCopy);
			}
			else if (slotId != SLOT_INPUT && slotId != SLOT_FUEL && slotId != SLOT_PAN)
			{
				if (itemstack.getItem() instanceof ItemPan)
				{
					if (!mergeItemStack(itemstack, SLOT_PAN, SLOT_PAN + 1, false))
					{
						return null;
					}
				}
				else if (TileEntityCampfire.isItemFuel(itemstack))
				{
					if (!mergeItemStack(itemstack, SLOT_FUEL, SLOT_FUEL + 1, false))
					{
						return null;
					}
				}
				else if (CampfireRecipes.smelting().getSmeltingResult(itemstack) != null)
				{
					if (!mergeItemStack(itemstack, SLOT_INPUT, SLOT_INPUT + 1, false))
					{
						return null;
					}
				}
				else if (slotId > SLOT_OUTPUT && slotId < 30)
				{
					if (!mergeItemStack(itemstack, 30, 39, false))
					{
						return null;
					}
				}
				else if (slotId >= 30 && slotId < 39 &&
					!mergeItemStack(itemstack, SLOT_PAN + 1, 30, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack, SLOT_PAN + 1, 39, false))
			{
				return null;
			}

			if (itemstack.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack.stackSize == itemstackCopy.stackSize)
			{
				return null;
			}
			slot.onPickupFromSlot(player, itemstack);
		}
		return itemstackCopy;
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
