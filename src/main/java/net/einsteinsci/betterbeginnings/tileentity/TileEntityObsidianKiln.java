package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockObsidianKiln;
import net.einsteinsci.betterbeginnings.inventory.ContainerObsidianKiln;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.world.IInteractionObject;

public class TileEntityObsidianKiln extends TileEntityKilnBase
{
	public TileEntityObsidianKiln()
	{
		super();
		processTime = 250;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[SLOT_FUEL]);
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}
		else
		{
			Item item = itemStack.getItem();

			// Blaze Rods and Lava are valid fuel sources for an obsidian kiln.
			if (item == Items.blaze_rod)
			{
				return 1600;
			}
			if (item == Items.lava_bucket)
			{
				return 80000;
			}

			// All fuels from the Kiln apply here too.
			return super.getItemBurnTime(itemStack);
		}
	}
	
	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.obsidianKiln";
	}

	@Override
	public void updateBlockState()
	{
		BlockObsidianKiln.updateBlockState(burnTime > 0, worldObj, pos);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerObsidianKiln(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":obsidianKiln";
	}
}

