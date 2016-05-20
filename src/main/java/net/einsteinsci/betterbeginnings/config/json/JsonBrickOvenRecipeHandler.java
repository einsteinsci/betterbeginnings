package net.einsteinsci.betterbeginnings.config.json;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonBrickOvenRecipeHandler
{
	private List<JsonBrickOvenShapedRecipe> shaped;
	private List<JsonBrickOvenShapelessRecipe> shapeless;

	private List<String> includes;

	private List<String> __COMMENTS;

	public JsonBrickOvenRecipeHandler()
	{
		shaped = new ArrayList<>();
		shapeless = new ArrayList<>();
		includes = new ArrayList<>();

		__COMMENTS = new ArrayList<>();
		__COMMENTS.add("Brick Oven recipes do not support ore dictionary in ingredients.");
		__COMMENTS.add("This will change eventually. This only applies to Brick Oven recipes.");

		// TESTING ONLY
		shaped.add(new JsonBrickOvenShapedRecipe(new ItemStack(Items.beef), "x ", "ox", 'x',
			Blocks.bedrock, 'o', Items.chainmail_chestplate));

		shapeless.add(new JsonBrickOvenShapelessRecipe(new ItemStack(Items.porkchop), Blocks.bedrock, Items.chainmail_boots));
	}

	public List<JsonBrickOvenShapedRecipe> getShaped()
	{
		return shaped;
	}

	public List<JsonBrickOvenShapelessRecipe> getShapeless()
	{
		return shapeless;
	}

	public List<String> getIncludes()
	{
		return includes;
	}
}
