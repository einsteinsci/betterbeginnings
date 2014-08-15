package net.einsteinsci.noobcraft.inventory;

import net.einsteinsci.noobcraft.register.recipe.CampfirePotRecipes;
import net.einsteinsci.noobcraft.register.recipe.CampfireRecipes;
import net.einsteinsci.noobcraft.register.recipe.KilnRecipes;
import net.einsteinsci.noobcraft.tileentity.TileEntityCampfire;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCampfire extends Container
{
	private TileEntityCampfire tileCampfire;
	
	private int lastBurnTime;
	public int lastItemBurnTime;
	public int lastCookTime;

	public ContainerCampfire(InventoryPlayer inventory, TileEntityCampfire campfire) {
		
		tileCampfire = campfire;
		addSlotToContainer(new Slot(tileCampfire, 0, 58, 12));
		addSlotToContainer(new Slot(tileCampfire, 1, 32, 35));
		addSlotToContainer(new Slot(tileCampfire, 2, 58, 57));
		addSlotToContainer(new SlotFurnace(inventory.player, campfire, 3, 118, 34));
		
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

	public void addCraftingToCrafters(ICrafting craft){
		super.addCraftingToCrafters(craft);
		
		craft.sendProgressBarUpdate(this, 0, tileCampfire.cookTime);
		craft.sendProgressBarUpdate(this, 1, tileCampfire.burnTime);
		craft.sendProgressBarUpdate(this, 2, tileCampfire.currentItemBurnTime);
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue){
		if(slot == 0) this.tileCampfire.cookTime = newValue;
		if(slot == 1) this.tileCampfire.burnTime = newValue;
		if(slot == 2) this.tileCampfire.currentItemBurnTime = newValue;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(par2);
		
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
				slot.putStack((ItemStack) null);
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
	
	
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		
		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting craft = (ICrafting) crafters.get(i);
			
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
		}
		
		lastBurnTime = tileCampfire.burnTime;
		lastCookTime = tileCampfire.cookTime;
		lastItemBurnTime = tileCampfire.currentItemBurnTime;
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileCampfire.isUseableByPlayer(player);
	}
	
}
