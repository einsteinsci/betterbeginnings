package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
        if (face == 0)
        {
            --y;
        }

        if (face == 1)
        {
            ++y;
        }

        if (face == 2)
        {
            --z;
        }

        if (face == 3)
        {
            ++z;
        }

        if (face == 4)
        {
            --x;
        }

        if (face == 5)
        {
            ++x;
        }

        if (!player.canPlayerEdit(x, y, z, face, stack))
        {
            return false;
        }
        else
        {
            if (world.isAirBlock(x, y, z))
            {
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                world.setBlock(x, y, z, Blocks.fire);
            }

            stack.damageItem(1, player);
            return true;
        }
    }
}


//
