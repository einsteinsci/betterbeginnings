package net.einsteinsci.betterbeginnings.register;

import cpw.mods.fml.common.registry.GameRegistry;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class RegisterHelper
{
	public static void registerBlock(Block block)
	{
		//substring(5) to remove the "tile." in "tile.blockNameHere".
		GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
	}

	public static void registerItem(Item item)
	{
		//substring(5) to remove the "item." in "item.itemNameHere".
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}

	public static void registerSmelterOreRecipe(String input, ItemStack output, float experience, int gravel, int bonus,
	                                            float chance)
	{
		for (ItemStack stack : OreDictionary.getOres(input))
		{
			SmelterRecipeHandler.addRecipe(stack, output, experience, gravel, bonus, chance);
		}
	}

	public static void registerSmelterOreRecipe(String input, String output, float experience, int gravel, int bonus,
	                                            float chance)
	{
		for (ItemStack stack : OreDictionary.getOres(input))
		{
			List<ItemStack> valid = OreDictionary.getOres(output);
			if (!valid.isEmpty())
			{
				SmelterRecipeHandler.addRecipe(stack, OreDictionary.getOres(output).get(0),
				                               experience, gravel, bonus, chance);
			}
		}
	}
}
