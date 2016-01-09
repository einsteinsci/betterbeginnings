package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.blocks.BlockSmelter;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntitySmelter extends TileEntitySmelterBase
{
	public TileEntitySmelter()
	{
		super();
		processTime = 160;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[FUEL]);
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.smelter";
	}

	@Override
	public void updateBlockState()
	{
		BlockSmelter.updateBlockState(burnTime > 0, worldObj, pos);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return null;
	}

	@Override
	public String getGuiID()
	{
		return null;
	}
}
