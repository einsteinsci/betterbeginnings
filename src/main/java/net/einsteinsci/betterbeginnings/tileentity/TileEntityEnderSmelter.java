package net.einsteinsci.betterbeginnings.tileentity;

import java.util.Random;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockEnderSmelter;
import net.einsteinsci.betterbeginnings.inventory.ContainerEnderSmelter;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityEnderSmelter extends TileEntitySmelterBase
{
	public static final Random RANDOM = new Random();

	public boolean oreDoubled = false;

	public TileEntityEnderSmelter()
	{
		super(4);
		processTime = 140;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[FUEL]);
		oreDoubled = tagCompound.getByte("oreDoubled") != 0;
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setByte("oreDoubled", oreDoubled ? (byte)1 : (byte)0);
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}

		Item item = itemStack.getItem();

		// Ender eyes and pearls are great fuel sources for the ender furnace.
		if (item == Items.ender_pearl)
		{
			return 1200;
		}
		if (item == Items.ender_eye)
		{
			return 2400;
		}

		return super.getItemBurnTime(itemStack);
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.enderSmelter";
	}

	@Override
	public boolean canSmelt()
	{
		if (specialFurnaceStacks[INPUT] == null || specialFurnaceStacks[GRAVEL] == null)
		{
			return false;
		}
		else
		{
			ItemStack output = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);
			int gravelNeeded = SmelterRecipeHandler.instance().getGravelCount(specialFurnaceStacks[INPUT]);
			int bonus = SmelterRecipeHandler.instance().getBonus(specialFurnaceStacks[INPUT]);

			if (output == null)
			{
				return false;
			}

			if (gravelNeeded > specialFurnaceStacks[GRAVEL].stackSize)
			{
				return false;
			}

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				return true;
			}
			if (!specialFurnaceStacks[OUTPUT].isItemEqual(output))
			{
				return false;
			}

			int result = specialFurnaceStacks[OUTPUT].stackSize + output.stackSize + bonus;
			return result <= getInventoryStackLimit() && result <= specialFurnaceStacks[OUTPUT].getMaxStackSize();
		}
	}

	@Override
	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);

			int bonus = SmelterRecipeHandler.instance().getBonus(specialFurnaceStacks[INPUT]);
			float chance = SmelterRecipeHandler.instance().getBonusChance(specialFurnaceStacks[INPUT]);
			int resultSize = itemStack.stackSize;
			if (RANDOM.nextFloat() < chance)
			{
				resultSize += bonus;
				oreDoubled = true;
			}

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += resultSize;
			}

			int gravelUsed = SmelterRecipeHandler.instance().getGravelCount(specialFurnaceStacks[INPUT]);

			--specialFurnaceStacks[INPUT].stackSize;

			if (specialFurnaceStacks[INPUT].stackSize <= 0)
			{
				specialFurnaceStacks[INPUT] = null;
			}
			specialFurnaceStacks[GRAVEL].stackSize -= gravelUsed;

			if (specialFurnaceStacks[GRAVEL].stackSize <= 0)
			{
				specialFurnaceStacks[GRAVEL] = null;
			}
		}
	}

	@Override
	public void updateBlockState()
	{
		BlockEnderSmelter.updateBlockState(burnTime > 0, worldObj, pos);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerEnderSmelter(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":enderSmelter";
	}
}
