package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonBrickOvenRecipeHandler
{
	private List<JsonBrickOvenShapedRecipe> shaped = new ArrayList<>();
	private List<JsonBrickOvenShapelessRecipe> shapeless = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();

	//private List<String> __COMMENTS = new ArrayList<>();

	public JsonBrickOvenRecipeHandler()
	{
		this(false);
	}
	public JsonBrickOvenRecipeHandler(boolean includeTesting)
	{
		//__COMMENTS = new ArrayList<>();
		//__COMMENTS.add("Brick Oven recipes do not support ore dictionary in ingredients.");
		//__COMMENTS.add("This will change eventually. This only applies to Brick Oven recipes.");

		if (includeTesting)
		{
			// TESTING ONLY
			shaped.add(new JsonBrickOvenShapedRecipe(new ItemStack(Items.beef), "x ", "ox", 'x',
				Blocks.bedrock, 'o', Items.chainmail_chestplate));

			shapeless.add(new JsonBrickOvenShapelessRecipe(new ItemStack(Items.porkchop), Blocks.bedrock, Items.chainmail_boots));
		}
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

	public List<String> getModDependencies()
	{
		return modDependencies;
	}
}
