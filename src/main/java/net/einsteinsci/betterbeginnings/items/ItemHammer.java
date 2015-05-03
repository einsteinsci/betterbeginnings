package net.einsteinsci.betterbeginnings.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;

import java.util.Set;

public class ItemHammer extends ItemTool
{
	private static final Set breakableBlocks = Sets
			.newHashSet(Blocks.stone, Blocks.cobblestone, Blocks.gravel, Blocks.sandstone);

	protected ItemHammer(Item.ToolMaterial material)
	{
	    super(2.0F, material, breakableBlocks);
	}

	public static ItemStack getCrushResult(Block broken)
	{
		if (broken == Blocks.stone)
		{
			return new ItemStack(Blocks.cobblestone);
		}
		else if (broken == Blocks.cobblestone)
		{
			return new ItemStack(Blocks.gravel);
		}
		else if (broken == Blocks.gravel)
		{
			return new ItemStack(Items.flint, 2);
		}
		else if (broken == Blocks.sandstone)
		{
			return new ItemStack(Blocks.sand);
		}
		else
		{
			return null;
		}
	}
}
