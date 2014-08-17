package net.einsteinsci.betterbeginnings.items;


import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRoastingStick extends Item
{

	public ItemRoastingStick()
	{
		setUnlocalizedName("roastingStick");
		this.shouldRotateAroundWhenRendering();
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(ModMain.tabBetterBeginnings);
		this.setMaxStackSize(1);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (player.inventory.hasItemStack(new ItemStack(RegisterItems.marshmallow)))
		{
			player.inventory.consumeInventoryItem(RegisterItems.marshmallow);
			stack = new ItemStack(RegisterItems.roastingStickrawMallow);
		}
		player.inventoryContainer.detectAndSendChanges();
		return stack;
	}
}
