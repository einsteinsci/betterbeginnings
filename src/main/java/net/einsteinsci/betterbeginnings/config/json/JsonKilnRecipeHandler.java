package net.einsteinsci.betterbeginnings.config.json;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonKilnRecipeHandler
{
	private List<JsonKilnRecipe> recipes;
	private List<String> includes;

	public JsonKilnRecipeHandler()
	{
		recipes = new ArrayList<>();
		includes = new ArrayList<>();

		// TESTING ONLY
		recipes.add(new JsonKilnRecipe(new ItemStack(Blocks.bedrock), new ItemStack(Items.blaze_rod), 0.5f));
	}

	public List<JsonKilnRecipe> getRecipes()
	{
		return recipes;
	}

	public List<String> getIncludes()
	{
		return includes;
	}
}
