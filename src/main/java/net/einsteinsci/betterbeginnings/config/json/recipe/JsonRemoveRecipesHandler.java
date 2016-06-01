package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonRemoveRecipesHandler
{
	List<JsonRemovedCraftingRecipe> craftingRemoved = new ArrayList<>();
	List<JsonRemovedSmeltingRecipe> smeltingRemoved = new ArrayList<>();

	List<String> includes = new ArrayList<>();
	List<String> modDependencies = new ArrayList<>();

	String __COMMENT = "Add items whose recipes you want to remove below. This does not affect recipes removed by " +
		"BetterBeginnings through regular config. Some examples are provided above (both of which have no recipe already).";

	public JsonRemoveRecipesHandler()
	{
		// Some examples
		craftingRemoved.add(new JsonRemovedCraftingRecipe(new ItemStack(Blocks.barrier)));
		smeltingRemoved.add(new JsonRemovedSmeltingRecipe(new ItemStack(RegisterItems.testItem)));
	}

	public List<JsonRemovedCraftingRecipe> getCraftingRemoved()
	{
		return craftingRemoved;
	}

	public List<JsonRemovedSmeltingRecipe> getSmeltingRemoved()
	{
		return smeltingRemoved;
	}

	public List<String> getIncludes()
	{
		return includes;
	}

	public List<String> getModDependencies()
	{
		return modDependencies;
	}
}
