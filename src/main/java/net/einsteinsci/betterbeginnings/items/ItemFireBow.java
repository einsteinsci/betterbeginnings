package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFireBow extends Item
{
	public ItemFireBow()
	{
		super();
		setUnlocalizedName("fireBow");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));

		setMaxStackSize(1);
		setMaxDamage(8);

		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face,
							 float x2, float y2, float z2)
	{
		stack.damageItem(1, player);
		return true;
	}
}


//
