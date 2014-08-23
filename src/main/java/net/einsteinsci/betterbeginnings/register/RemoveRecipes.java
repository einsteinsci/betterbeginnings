package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;

import java.util.*;

public class RemoveRecipes
{
	public static void remove()
	{
		List<Item> removedRecipes = new ArrayList<Item>();

		// Be sure to get the correct quantity and damage

		// Replaced/Obsolete Items
		removedRecipes.add(Items.wooden_sword);
		removedRecipes.add(Items.wooden_pickaxe);
		removedRecipes.add(Items.wooden_axe);
		removedRecipes.add(Item.getItemFromBlock(Blocks.furnace));
		removedRecipes.add(Item.getItemFromBlock(Blocks.crafting_table));

		// Advanced Crafting
		if (BBConfig.advancedCraftingForLotsOfThings)
		{
			removedRecipes.add(Items.wooden_door);
			removedRecipes.add(Items.iron_door);
			removedRecipes.add(Item.getItemFromBlock(Blocks.fence_gate));
			removedRecipes.add(Item.getItemFromBlock(Blocks.trapdoor));
			removedRecipes.add(Item.getItemFromBlock(Blocks.chest));
			removedRecipes.add(Item.getItemFromBlock(Blocks.trapped_chest));
			removedRecipes.add(Item.getItemFromBlock(Blocks.piston));
			removedRecipes.add(Item.getItemFromBlock(Blocks.tripwire_hook));
		}

		removedRecipes.add(Items.bow);
		removedRecipes.add(Items.wooden_shovel);
		removedRecipes.add(Items.wooden_hoe);

		removedRecipes.add(Items.leather_helmet);
		removedRecipes.add(Items.leather_chestplate);
		removedRecipes.add(Items.leather_leggings);
		removedRecipes.add(Items.leather_boots);

		removedRecipes.add(Items.stone_pickaxe);
		removedRecipes.add(Items.stone_sword);
		removedRecipes.add(Items.stone_axe);
		removedRecipes.add(Items.stone_shovel);
		removedRecipes.add(Items.stone_hoe);

		removedRecipes.add(Items.iron_pickaxe);
		removedRecipes.add(Items.iron_sword);
		removedRecipes.add(Items.iron_axe);
		removedRecipes.add(Items.iron_shovel);
		removedRecipes.add(Items.iron_hoe);

		removedRecipes.add(Items.iron_helmet);
		removedRecipes.add(Items.iron_chestplate);
		removedRecipes.add(Items.iron_leggings);
		removedRecipes.add(Items.iron_boots);

		removedRecipes.add(Items.diamond_sword);
		removedRecipes.add(Items.diamond_pickaxe);
		removedRecipes.add(Items.diamond_axe);
		removedRecipes.add(Items.diamond_shovel);
		removedRecipes.add(Items.diamond_hoe);

		removedRecipes.add(Items.diamond_helmet);
		removedRecipes.add(Items.diamond_chestplate);
		removedRecipes.add(Items.diamond_leggings);
		removedRecipes.add(Items.diamond_boots);

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> iterator = recipes.iterator();

		while (iterator.hasNext())
		{
			ItemStack stack = iterator.next().getRecipeOutput();

			if (stack != null)
			{
				Item item = stack.getItem();
				if (item != null && removedRecipes.contains(item))
				{
					iterator.remove();
				}
			}
		}
	}

	public static void removeFurnaceRecipes()
	{
		// Remove ALL THE THINGS!
		ItemStack ironOre = new ItemStack(Blocks.iron_ore);
		Map recipes = FurnaceRecipes.smelting().getSmeltingList();
		Iterator iterator = recipes.entrySet().iterator();

		while (iterator.hasNext())
		{
			iterator.next();
			iterator.remove();
		}
	}
}


// Buffer
