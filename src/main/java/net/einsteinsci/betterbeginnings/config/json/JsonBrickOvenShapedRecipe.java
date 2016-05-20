package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBrickOvenShapedRecipe
{
	private JsonLoadedItemStack output;
	private List<String> recipe = new ArrayList<>();
	private Map<Character, JsonLoadedItem> ingredients = new HashMap<>();

	public JsonBrickOvenShapedRecipe(ItemStack output, Object... params)
	{
		this.output = new JsonLoadedItemStack(output);

		char active = '\0';
		boolean recipeFinished = false;
		for (Object obj : params)
		{
			if (!recipeFinished)
			{
				if (obj instanceof String)
				{
					String str = (String)obj;
					recipe.add(str);
				}
				else if (obj instanceof Character)
				{
					active = (Character)obj;
					recipeFinished = true;
				}
				else
				{
					throw new IllegalArgumentException("Invalid type for first phase of recipe: " +
						obj.getClass().getName());
				}
			}
			else
			{
				if (obj instanceof Character)
				{
					char c = (Character)obj;

					if (c != ' ')
					{
						active = c;
					}
				}
				else if (obj instanceof ItemStack)
				{
					ItemStack stack = (ItemStack)obj;
					JsonLoadedItem ing = new JsonLoadedItem(stack);
					ingredients.put(active, ing);
				}
				else if (obj instanceof Item)
				{
					Item item = (Item)obj;
					JsonLoadedItem ing = new JsonLoadedItem(new ItemStack(item));
					ingredients.put(active, ing);
				}
				else if (obj instanceof Block)
				{
					Block block = (Block)obj;
					JsonLoadedItem ing = new JsonLoadedItem(new ItemStack(block));
					ingredients.put(active, ing);
				}
			}
		}
	}

	public JsonBrickOvenShapedRecipe(JsonLoadedItemStack output, List<String> recipe, Map<Character, JsonLoadedItem> ingredients)
	{
		this.output = output;
		this.recipe = recipe;
		this.ingredients = ingredients;
	}

	public void register()
	{
		List<Object> res = new ArrayList<>();
		for (String s : recipe)
		{
			res.add(s);
		}

		boolean oreDict = false;
		for (Map.Entry<Character, JsonLoadedItem> entry : ingredients.entrySet())
		{
			res.add(entry.getKey());

			JsonLoadedItem jli = entry.getValue();

			ItemStack stack = jli.getFirstItemStackOrNull();
			if (stack != null)
			{
				res.add(stack);
			}
			else
			{
				ItemStack invalid = new ItemStack(Blocks.barrier);
				invalid.setStackDisplayName("ERROR IN LOADING JSON RECIPE. MISSING INGREDIENT.");
				res.add(invalid);
			}
		}
		Object[] params = res.toArray();
		BrickOvenRecipeHandler.addShapedRecipe(output.getFirstItemStackOrNull(), params);
	}

	public JsonLoadedItemStack getOutput()
	{
		return output;
	}

	public List<String> getRecipe()
	{
		return recipe;
	}

	public Map<Character, JsonLoadedItem> getIngredients()
	{
		return ingredients;
	}
}
