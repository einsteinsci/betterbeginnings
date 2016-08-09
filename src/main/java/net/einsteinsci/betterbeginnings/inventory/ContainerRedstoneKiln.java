package net.einsteinsci.betterbeginnings.inventory;

import cofh.api.energy.IEnergyContainerItem;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKilnBase;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityRedstoneKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerRedstoneKiln extends ContainerSpecializedFurnace
{
	protected int lastPowerLevel;
	
	protected TileEntityRedstoneKiln tileRedstoneKiln;
	
	public static final int POWER_LEVEL_ID = 3;
	
	public ContainerRedstoneKiln(InventoryPlayer playerInv, TileEntityRedstoneKiln redKiln)
	{
		tileSpecialFurnace = redKiln;
		tileRedstoneKiln = redKiln;
		addSlotToContainer(new Slot(redKiln, 0, 56, 27));
		addSlotToContainer(new Slot(redKiln, 1, 17, 63));
		addSlotToContainer(new SlotFurnaceOutput(playerInv.player, redKiln, 2, 116, 35));

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
		
		craft.sendProgressBarUpdate(this, POWER_LEVEL_ID, tileSpecialFurnace.currentItemBurnLength);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (Object listItem : crafters)
		{
			ICrafting crafter = (ICrafting)listItem;
			
			if (lastPowerLevel != tileRedstoneKiln.getBattery().getEnergyStored())
			{
				crafter.sendProgressBarUpdate(this, POWER_LEVEL_ID, tileRedstoneKiln.getBattery().getEnergyStored());
			}
		}
		
		lastPowerLevel = tileRedstoneKiln.getBattery().getEnergyStored();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int progressBar, int level)
	{
		super.updateProgressBar(progressBar, level);
		
		if (progressBar == POWER_LEVEL_ID)
		{
			tileRedstoneKiln.getBattery().setEnergyStored(level);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(par2);

		TileEntityKilnBase kiln = (TileEntityKilnBase)tileSpecialFurnace;

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
				if (KilnRecipeHandler.instance().getSmeltingResult(itemstack1) != null)
				{
					if (!mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if (itemstack1.getItem() instanceof IEnergyContainerItem)
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
}
