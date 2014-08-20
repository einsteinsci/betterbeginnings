package net.einsteinsci.betterbeginnings.items;


import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRoastingStickMallow extends Item
{
	boolean isCooked;

	public ItemRoastingStickMallow(boolean cooked)
	{
		if (cooked)
		{
			setUnlocalizedName("roastingStickcookedMallow");
		}
		else
		{
			setUnlocalizedName("roastingStickrawMallow");
		}
		isCooked = cooked;
		this.shouldRotateAroundWhenRendering();
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(ModMain.tabBetterBeginnings);
		this.setMaxStackSize(1);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{

		if (isCooked)
		{
			player.inventory.addItemStackToInventory(new ItemStack(RegisterItems.marshmallowCooked));
			player.inventory.addItemStackToInventory(new ItemStack(RegisterItems.roastingStick));
			player.inventory.consumeInventoryItem(stack.getItem());
		}
		else
		{
			player.inventory.addItemStackToInventory(new ItemStack(RegisterItems.marshmallow));
			player.inventory.addItemStackToInventory(new ItemStack(RegisterItems.roastingStick));
			player.inventory.consumeInventoryItem(stack.getItem());
		}
		player.inventoryContainer.detectAndSendChanges();
		return stack;
	}
}
