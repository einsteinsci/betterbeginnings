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

	public JsonBrickOvenRecipeHandler()
	{
		shaped = new ArrayList<>();
		shapeless = new ArrayList<>();

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
}
