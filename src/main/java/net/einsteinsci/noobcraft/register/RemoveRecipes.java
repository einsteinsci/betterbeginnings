package net.einsteinsci.noobcraft.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RemoveRecipes
{
	public static void remove()
	{
		List<Item> removedRecipes = new ArrayList<Item>();
		
		// Be sure to get the correct quantity and damage
		removedRecipes.add(Items.wooden_sword);
		removedRecipes.add(Item.getItemFromBlock(Blocks.furnace));
		removedRecipes.add(Items.diamond_pickaxe);
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> iterator = recipes.iterator();
		
		while (iterator.hasNext())
		{
			ItemStack stack = iterator.next().getRecipeOutput();
			if (stack == null)
			{
				continue;
			}
			else
			{
				Item item = stack.getItem();
				if (item != null && removedRecipes.contains(item))
				{
					iterator.remove();
				}
			}
		}
	}
}
