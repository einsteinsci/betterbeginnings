package net.einsteinsci.betterbeginnings.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;

import java.util.Set;

public class ItemHammer extends ItemTool
{
    private static final Set field_150915_c = Sets.newHashSet(new Block[] {Blocks.stone, Blocks.cobblestone, Blocks.gravel, Blocks.sandstone});
    private static final String __OBFID = "CL_00000053";

    protected ItemHammer(Item.ToolMaterial p_i45347_1_)
    {
        super(2.0F, p_i45347_1_, field_150915_c);
    }

	public static Set GetBreakable()
	{
		Set s = Sets.newHashSet();

		s.add(Blocks.stone);
		s.add(Blocks.cobblestone);
		s.add(Blocks.gravel);
		s.add(Blocks.sandstone);
		s.add(Blocks.melon_block);

		return s;
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

	public boolean func_150897_b(Block block)
	{
		return block == Blocks.obsidian ? toolMaterial.getHarvestLevel() == 3
				: block != Blocks.diamond_block && block != Blocks.diamond_ore
						? block != Blocks.emerald_ore && block != Blocks.emerald_block ?
						block != Blocks.gold_block && block != Blocks.gold_ore
								? block != Blocks.iron_block && block != Blocks.iron_ore ?
								block != Blocks.lapis_block && block != Blocks.lapis_ore
										? block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore
										? block.getMaterial() == Material.rock ? true
										: block.getMaterial() == Material.iron ? true
												: block.getMaterial() == Material.anvil
										: toolMaterial.getHarvestLevel() >= 2
										: toolMaterial.getHarvestLevel() >= 1
								: toolMaterial.getHarvestLevel() >= 1
								: toolMaterial.getHarvestLevel() >= 2
						: toolMaterial.getHarvestLevel() >= 2
						: toolMaterial.getHarvestLevel() >= 2;
	}

	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
	{
		return p_150893_2_.getMaterial() != Material.iron && p_150893_2_.getMaterial() != Material.anvil && p_150893_2_
				.getMaterial() != Material.rock ? super.func_150893_a(p_150893_1_, p_150893_2_)
				: this.efficiencyOnProperMaterial;
	}
}
