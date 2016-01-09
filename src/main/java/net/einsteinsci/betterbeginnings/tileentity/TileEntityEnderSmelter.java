package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockEnderSmelter;
import net.einsteinsci.betterbeginnings.inventory.ContainerEnderSmelter;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityEnderSmelter extends TileEntitySmelterBase
{
	public boolean oreDoubled = false;

	public TileEntityEnderSmelter()
	{
		super(4);
		processTime = 140;
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}

		Item item = itemStack.getItem();

		// Ender eyes and pearls are great fuel sources for the ender smelter.
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
	public float getBaseBoosterLevel()
	{
		return 1.75f;
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
