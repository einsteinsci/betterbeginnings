package net.einsteinsci.betterbeginnings.items;


import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRoastingStick extends Item implements IBBName
{

	public ItemRoastingStick()
	{
		setUnlocalizedName("roastingStick");
		//shouldRotateAroundWhenRendering();
		//setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
		setMaxStackSize(5);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (player.inventory.hasItemStack(new ItemStack(RegisterItems.marshmallow)))
		{
			player.inventory.consumeInventoryItem(RegisterItems.marshmallow);
			stack = new ItemStack(RegisterItems.roastingStickRawMallow);
		}
		player.inventoryContainer.detectAndSendChanges();
		return stack;
	}

	@Override
	public String getName()
	{
		return "roastingStick";
	}
}
