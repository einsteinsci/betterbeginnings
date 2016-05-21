package net.einsteinsci.betterbeginnings.config.json;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonKilnRecipeHandler
{
	private List<JsonKilnRecipe> recipes = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();

	public JsonKilnRecipeHandler()
	{ }

	public JsonKilnRecipeHandler(boolean includeTesting)
	{
		if (includeTesting)
		{
			// TESTING ONLY
			recipes.add(new JsonKilnRecipe(new ItemStack(Blocks.bedrock), new ItemStack(Items.blaze_rod), 0.5f));
		}
	}

	public List<JsonKilnRecipe> getRecipes()
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
