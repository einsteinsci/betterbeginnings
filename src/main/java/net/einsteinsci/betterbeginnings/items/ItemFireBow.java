package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFireBow extends Item implements IBBName
{
	public ItemFireBow()
	{
		super();
		setUnlocalizedName("fireBow");

		setMaxStackSize(1);
		setMaxDamage(8);

		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                             float hitX, float hitY, float hitZ)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (side == EnumFacing.DOWN)
        {
            --y;
        }

        if (side == EnumFacing.UP)
        {
            ++y;
        }

        if (side == EnumFacing.NORTH)
        {
            --z;
        }

        if (side == EnumFacing.SOUTH)
        {
            ++z;
        }

        if (side == EnumFacing.WEST)
        {
            --x;
        }

        if (side == EnumFacing.EAST)
        {
            ++x;
        }

        if (!player.canPlayerEdit(pos, side, stack))
        {
            return false;
        }
        else
        {
            if (world.isAirBlock(pos))
            {
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F,
                                      itemRand.nextFloat() * 0.4F + 0.8F);
                world.setBlockState(pos, Blocks.fire.getDefaultState());
            }

            stack.damageItem(1, player);
            return true;
        }
    }

    @Override
    public String getName()
    {
        return "fireBow";
    }
}
