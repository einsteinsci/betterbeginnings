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
		List<Item> removedRecipes = new ArrayList<>();

		// Be sure to get the correct quantity and damage

		// Replaced/Obsolete Items
		removedRecipes.add(Items.wooden_sword);

		if (BBConfig.moduleFurnaces)
		{
			removedRecipes.add(Item.getItemFromBlock(Blocks.furnace));
		}

		removedRecipes.add(Item.getItemFromBlock(Blocks.crafting_table));

		if (BBConfig.removeWoodToolRecipes)
		{
			removedRecipes.add(Items.wooden_pickaxe);
			removedRecipes.add(Items.wooden_axe);
			removedRecipes.add(Items.wooden_shovel);
			removedRecipes.add(Items.wooden_hoe);
		}

		// Advanced Crafting
		if (BBConfig.moduleAdvancedCrafting)
		{
			if (BBConfig.advancedCraftingForLotsOfThings)
			{
				removedRecipes.add(Items.oak_door);
				removedRecipes.add(Items.spruce_door);
				removedRecipes.add(Items.birch_door);
				removedRecipes.add(Items.jungle_door);
				removedRecipes.add(Items.acacia_door);
				removedRecipes.add(Items.dark_oak_door);
				removedRecipes.add(Items.iron_door);
				removedRecipes.add(Item.getItemFromBlock(Blocks.oak_fence_gate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.spruce_fence_gate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.birch_fence_gate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.jungle_fence_gate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.acacia_fence_gate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.dark_oak_fence_gate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.trapdoor));
				removedRecipes.add(Item.getItemFromBlock(Blocks.chest));
				removedRecipes.add(Item.getItemFromBlock(Blocks.trapped_chest));
				removedRecipes.add(Item.getItemFromBlock(Blocks.piston));
				removedRecipes.add(Item.getItemFromBlock(Blocks.tripwire_hook));
				removedRecipes.add(Item.getItemFromBlock(Blocks.dispenser));
				removedRecipes.add(Item.getItemFromBlock(Blocks.noteblock));
				removedRecipes.add(Item.getItemFromBlock(Blocks.golden_rail));
				removedRecipes.add(Item.getItemFromBlock(Blocks.detector_rail));
				removedRecipes.add(Item.getItemFromBlock(Blocks.tnt));
				removedRecipes.add(Item.getItemFromBlock(Blocks.bookshelf));
				removedRecipes.add(Item.getItemFromBlock(Blocks.ladder));
				removedRecipes.add(Item.getItemFromBlock(Blocks.rail));
				removedRecipes.add(Item.getItemFromBlock(Blocks.enchanting_table));
				removedRecipes.add(Item.getItemFromBlock(Blocks.beacon));
				removedRecipes.add(Item.getItemFromBlock(Blocks.anvil));
				removedRecipes.add(Item.getItemFromBlock(Blocks.hopper));
				removedRecipes.add(Item.getItemFromBlock(Blocks.activator_rail));
				removedRecipes.add(Item.getItemFromBlock(Blocks.dropper));
				removedRecipes.add(Item.getItemFromBlock(Blocks.jukebox));
				removedRecipes.add(Item.getItemFromBlock(Blocks.redstone_lamp));
				removedRecipes.add(Item.getItemFromBlock(Blocks.ender_chest));
				removedRecipes.add(Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.light_weighted_pressure_plate));
				removedRecipes.add(Item.getItemFromBlock(Blocks.daylight_detector));
				removedRecipes.add(Item.getItemFromBlock(Blocks.iron_trapdoor));
				removedRecipes.add(Items.minecart);
				removedRecipes.add(Items.compass);
				removedRecipes.add(Items.clock);
				removedRecipes.add(Items.bed);
				removedRecipes.add(Items.brewing_stand);
				removedRecipes.add(Items.cauldron);
				removedRecipes.add(Items.item_frame);
				removedRecipes.add(Items.comparator);
				removedRecipes.add(Items.armor_stand);
			}

			removedRecipes.add(Items.bow);
			removedRecipes.add(Items.fishing_rod);
			removedRecipes.add(Items.shears);

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

			removedRecipes.add(Items.golden_pickaxe);
			removedRecipes.add(Items.golden_sword);
			removedRecipes.add(Items.golden_axe);
			removedRecipes.add(Items.golden_shovel);
			removedRecipes.add(Items.golden_hoe);

			removedRecipes.add(Items.golden_helmet);
			removedRecipes.add(Items.golden_chestplate);
			removedRecipes.add(Items.golden_leggings);
			removedRecipes.add(Items.golden_boots);
		}

		// Food that should be cooked in ovens
		if (BBConfig.removeCraftedFoodRecipes)
		{
			removedRecipes.add(Items.bread);
			removedRecipes.add(Items.cake);
			removedRecipes.add(Items.cookie);
			removedRecipes.add(Items.fermented_spider_eye);
			removedRecipes.add(Items.golden_apple);
			removedRecipes.add(Items.magma_cream);
			removedRecipes.add(Items.pumpkin_pie);
			removedRecipes.add(Items.rabbit_stew);
		}

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> iterator = recipes.iterator();

		while (iterator.hasNext())
		{
			IRecipe recipe = iterator.next();
			ItemStack stack = recipe.getRecipeOutput();

			if (stack != null)
			{
				Item item = stack.getItem();
				if (item == Item.getItemFromBlock(Blocks.crafting_table) &&
					recipe.getRecipeSize() != 4)
				{
					continue;
				}

				if (item != null && removedRecipes.contains(item))
				{
					iterator.remove();
				}
			}
		}
	}

	public static void removeFurnaceRecipes()
	{
		List<ItemStack> vanillaSmeltingOutputs = new ArrayList<>();

		vanillaSmeltingOutputs.add(new ItemStack(Items.baked_potato));
		vanillaSmeltingOutputs.add(new ItemStack(Items.brick));
		vanillaSmeltingOutputs.add(new ItemStack(Items.coal)); //charcoal, coal silk-touched
		vanillaSmeltingOutputs.add(new ItemStack(Items.cooked_beef));
		vanillaSmeltingOutputs.add(new ItemStack(Items.cooked_chicken));
		vanillaSmeltingOutputs.add(new ItemStack(Items.cooked_fish));
		vanillaSmeltingOutputs.add(new ItemStack(Items.cooked_porkchop));
		vanillaSmeltingOutputs.add(new ItemStack(Items.cooked_rabbit));
		vanillaSmeltingOutputs.add(new ItemStack(Items.cooked_mutton));
		vanillaSmeltingOutputs.add(new ItemStack(Items.diamond)); //silk-touched
		vanillaSmeltingOutputs.add(new ItemStack(Items.dye)); //Cactus green, lapis silk-touched
		vanillaSmeltingOutputs.add(new ItemStack(Items.emerald)); //silk-touched
		vanillaSmeltingOutputs.add(new ItemStack(Items.gold_ingot));
		vanillaSmeltingOutputs.add(new ItemStack(Items.iron_ingot));
		vanillaSmeltingOutputs.add(new ItemStack(Items.netherbrick));
		vanillaSmeltingOutputs.add(new ItemStack(Items.quartz)); //silk-touched
		vanillaSmeltingOutputs.add(new ItemStack(Items.redstone)); //silk-touched
		vanillaSmeltingOutputs.add(new ItemStack(Blocks.glass));
		vanillaSmeltingOutputs.add(new ItemStack(Blocks.hardened_clay));
		vanillaSmeltingOutputs.add(new ItemStack(Blocks.sponge)); //1.8
		vanillaSmeltingOutputs.add(new ItemStack(Blocks.stone));
		vanillaSmeltingOutputs.add(new ItemStack(Blocks.stonebrick)); //1.8

		if (BBConfig.removeSmeltingRecipes)
		{
			// Remove ALL THE THINGS!
			Map recipes = FurnaceRecipes.instance().getSmeltingList();
			Iterator iterator = recipes.entrySet().iterator();

			while (iterator.hasNext())
			{
				Map.Entry entry = (Map.Entry)iterator.next();
				ItemStack result = (ItemStack)entry.getValue();
				boolean isVanilla = false;
				for (ItemStack vanilla : vanillaSmeltingOutputs)
				{
					if (vanilla.getItem() == result.getItem())
					{
						isVanilla = true;
						break;
					}
				}

				if (!BBConfig.removeOnlyVanillaSmeltingRecipes || isVanilla)
				{
					iterator.remove();
				}
			}
		}
	}
}


// Buffer
