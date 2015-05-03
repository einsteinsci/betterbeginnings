package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemTestItem extends Item implements IBBName
{
	public ItemTestItem()
	{
		super();
		setUnlocalizedName("testItem");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
								  float hitX, float hitY, float hitZ)
	{
		Block tested = world.getBlockState(pos).getBlock();
		String blockName = tested.getUnlocalizedName().substring(5);
		ChatComponentTranslation trans = new ChatComponentTranslation(blockName);
		trans.appendText(" : " + world.getBlockState(pos).hashCode());
		player.addChatMessage(trans);

		return true;
	}

	@Override
	public String getName()
	{
		return "testItem";
	}
}
