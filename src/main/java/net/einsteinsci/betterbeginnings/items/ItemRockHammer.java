package net.einsteinsci.betterbeginnings.items;

import com.google.common.collect.Sets;
import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Set;


public class ItemRockHammer extends ItemHammer
{


	public static final float DAMAGE = 2.0F;

	public ItemRockHammer(ToolMaterial material)
	{
		super(material);
		setUnlocalizedName("rockHammer");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	public static Set GetBreakable()
	{
		Set s = Sets.newHashSet();

		s.add(Blocks.stone);
		s.add(Blocks.cobblestone);
		s.add(Blocks.gravel);
		s.add(Blocks.sandstone);
		s.add(new ItemStack(Blocks.sandstone, 2));
		s.add(Blocks.dirt);


		return s;
	}

	// Determines if a block broken with the tool will drop its drops.
	@Override
	public boolean func_150897_b(Block block)
	{
		if (block.getMaterial() == Material.rock && toolMaterial.getHarvestLevel() >= 0)
		{
			return true;
		}

		return false;
	}
}
