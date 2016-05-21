package net.einsteinsci.betterbeginnings.config.json;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonCampfireRecipeHandler
{
	private List<JsonCampfireRecipe> recipes = new ArrayList<>();
	private List<JsonCampfirePanRecipe> panRecipes = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();

	public JsonCampfireRecipeHandler()
	{ }

	public JsonCampfireRecipeHandler(boolean includeTesting)
	{
		if (includeTesting)
		{
			// TESTING ONLY
			recipes.add(new JsonCampfireRecipe(new ItemStack(Blocks.bedrock), new ItemStack(Items.magma_cream), 0.5f));
			panRecipes.add(new JsonCampfirePanRecipe(new ItemStack(Blocks.bedrock), new ItemStack(Items.ender_eye), 2.0f));
		}
	}

	public List<JsonCampfireRecipe> getRecipes()
	{
		return recipes;
	}

	public List<JsonCampfirePanRecipe> getPanRecipes()
	{
		return panRecipes;
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
