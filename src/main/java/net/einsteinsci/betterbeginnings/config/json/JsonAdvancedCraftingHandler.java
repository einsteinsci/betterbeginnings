package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonAdvancedCraftingHandler
{
	private List<JsonAdvancedRecipe> recipes = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();

	public JsonAdvancedCraftingHandler()
	{
		// TESTING ONLY
		recipes.add(new JsonAdvancedRecipe(new ItemStack(Items.string, 38),
			new Object[]{new ItemStack(RegisterItems.cloth, 7), "dustRedstone", 13}, "ox ", "xxx", " xo",
			'x', Blocks.bedrock, 'o', "nuggetIron"));
	}

	public List<JsonAdvancedRecipe> getRecipes()
	{
		return recipes;
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
