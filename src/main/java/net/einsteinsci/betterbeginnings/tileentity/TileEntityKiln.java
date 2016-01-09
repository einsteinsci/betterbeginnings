package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockKiln;
import net.einsteinsci.betterbeginnings.inventory.ContainerKiln;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IInteractionObject;

public class TileEntityKiln extends TileEntityKilnBase
{
	public TileEntityKiln()
	{
		super();
		processTime = 250;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTimeStatic(specialFurnaceStacks[1]);
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.kiln";
	}

	@Override
	public void updateBlockState()
	{
		BlockKiln.updateBlockState(burnTime > 0, worldObj, pos);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerKiln(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":kiln";
	}
}
