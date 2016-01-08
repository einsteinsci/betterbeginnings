package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockBrickOven;
import net.einsteinsci.betterbeginnings.inventory.ContainerBrickOven;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;

public class TileEntityBrickOven extends TileEntityBrickOvenBase implements IInteractionObject
{
	public TileEntityBrickOven()
	{
		super();
		processTime = 120;
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
		return hasCustomName() ? customName : "container.brickoven";
	}

	@Override
	public ItemStack findMatchingRecipe()
	{
		return BrickOvenRecipeHandler.instance().findMatchingRecipe(this);
	}

	@Override
	public void updateBlockState()
	{
		BlockBrickOven.updateBlockState(burnTime > 0, worldObj, pos);
	}

	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return getStackInSlot(INPUTSTART + row + column * 3);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerBrickOven(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":brickOven";
	}
}