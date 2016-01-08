package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.tileentity.TileEntitySpecializedFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ContainerSpecializedFurnace extends Container
{
	protected TileEntitySpecializedFurnace tileSpecialFurnace;
	
	protected int lastCookTime;
	protected int lastBurnTime;
	protected int lastItemBurnTime;
	
	@Override
	public void onCraftGuiOpened(ICrafting craft)
	{
		super.onCraftGuiOpened(craft);

		craft.sendProgressBarUpdate(this, 0, tileSpecialFurnace.cookTime);
		craft.sendProgressBarUpdate(this, 1, tileSpecialFurnace.burnTime);
		craft.sendProgressBarUpdate(this, 2, tileSpecialFurnace.currentItemBurnLength);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (Object listItem : crafters)
		{
			ICrafting crafter = (ICrafting)listItem;

			if (lastCookTime != tileSpecialFurnace.cookTime)
			{
				crafter.sendProgressBarUpdate(this, 0, tileSpecialFurnace.cookTime);
			}
			if (lastBurnTime != tileSpecialFurnace.burnTime)
			{
				crafter.sendProgressBarUpdate(this, 1, tileSpecialFurnace.burnTime);
			}
			if (lastItemBurnTime != tileSpecialFurnace.currentItemBurnLength)
			{
				crafter.sendProgressBarUpdate(this, 2, tileSpecialFurnace.currentItemBurnLength);
			}
		}

		lastBurnTime = tileSpecialFurnace.burnTime;
		lastCookTime = tileSpecialFurnace.cookTime;
		lastItemBurnTime = tileSpecialFurnace.currentItemBurnLength;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			tileSpecialFurnace.cookTime = par2;
		}
		if (par1 == 1)
		{
			tileSpecialFurnace.burnTime = par2;
		}
		if (par1 == 2)
		{
			tileSpecialFurnace.currentItemBurnLength = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileSpecialFurnace.isUseableByPlayer(player);
	}
}
