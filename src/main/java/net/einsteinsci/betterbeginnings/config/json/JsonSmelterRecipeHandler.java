package net.einsteinsci.betterbeginnings.config.json;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonSmelterRecipeHandler
{
	private List<JsonSmelterRecipe> recipes;

	public JsonSmelterRecipeHandler()
	{
		recipes = new ArrayList<>();

		// TESTING ONLY
		recipes.add(new JsonSmelterRecipe(new ItemStack(Blocks.bedrock), new ItemStack(Items.golden_hoe), 0.5f, 1, 0));
	}

	public List<JsonSmelterRecipe> getRecipes()
	{
		return recipes;
	}
}
