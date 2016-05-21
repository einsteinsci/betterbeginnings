package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.register.recipe.AdvancedCraftingHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonAdvancedRecipe
{
	private JsonLoadedItemStack output;
	private List<String> recipe = new ArrayList<>();
	private Map<Character, JsonLoadedItem> ingredients = new HashMap<>();
	private List<JsonLoadedItemStack> catalysts = new ArrayList<>();
	private boolean hideFromNEI;

	public JsonAdvancedRecipe(ItemStack output, Object[] catalysts, Object... params)
	{
		this(output, false, catalysts, params);
	}
	public JsonAdvancedRecipe(ItemStack output, boolean hide, Object[] catalysts, Object... params)
	{
		this.output = new JsonLoadedItemStack(output);
		hideFromNEI = hide;

		String ore = null;
		for (Object obj : catalysts)
		{
			if (obj instanceof String)
			{
				ore = (String)obj;
			}
			else if (obj instanceof Integer)
			{
				if (ore == null)
				{
					continue;
				}

				int count = (Integer)obj;
				JsonLoadedItemStack jlis = JsonLoadedItemStack.makeOreDictionary(ore, count);
				this.catalysts.add(jlis);
			}
			else if (obj instanceof ItemStack)
			{
				ore = null;

				ItemStack stack = (ItemStack)obj;
				JsonLoadedItemStack jlis = new JsonLoadedItemStack(stack);
				this.catalysts.add(jlis);
			}
		}

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
				else if (obj instanceof String)
				{
					if (active == '\0')
					{
						continue;
					}

					String str = (String)obj;
					JsonLoadedItem ing = JsonLoadedItem.makeOreDictionary(str);
					ingredients.put(active, ing);
				}
				else if (obj instanceof ItemStack)
				{
					if (active == '\0')
					{
						continue;
					}

					ItemStack stack = (ItemStack)obj;
					JsonLoadedItem ing = new JsonLoadedItem(stack);
					ingredients.put(active, ing);
				}
				else if (obj instanceof Item)
				{
					if (active == '\0')
					{
						continue;
					}

					Item item = (Item)obj;
					JsonLoadedItem ing = new JsonLoadedItem(new ItemStack(item));
					ingredients.put(active, ing);
				}
				else if (obj instanceof Block)
				{
					if (active == '\0')
					{
						continue;
					}

					Block block = (Block)obj;
					JsonLoadedItem ing = new JsonLoadedItem(new ItemStack(block));
					ingredients.put(active, ing);
				}
			}
		}
	}

	public JsonAdvancedRecipe(JsonLoadedItemStack output, List<String> recipe,
		Map<Character, JsonLoadedItem> ingredients, List<JsonLoadedItemStack> catalysts)
	{
		this.output = output;
		this.recipe = recipe;
		this.ingredients = ingredients;
		this.catalysts = catalysts;
	}

	public void register()
	{
		List<Object> res = new ArrayList<>();
		for (JsonLoadedItemStack jlis : catalysts)
		{
			if (jlis.isOreDictionary())
			{
				res.add(jlis.getItemName());
				res.add(jlis.getStackSize());
			}
			else
			{
				res.add(jlis.getFirstItemStackOrNull());
			}
		}
		Object[] addedMats = res.toArray();

		res = new ArrayList<>();
		for (String s : recipe)
		{
			res.add(s);
		}
		for (Map.Entry<Character, JsonLoadedItem> entry : ingredients.entrySet())
		{
			res.add(entry.getKey());
			if (entry.getValue().isOreDictionary())
			{
				res.add(entry.getValue().getItemName());
			}
			else
			{
				ItemStack stack = entry.getValue().getFirstItemStackOrNull();
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
		}
		Object[] params = res.toArray();

		AdvancedCraftingHandler.addAdvancedRecipe(output.getFirstItemStackOrNull(), hideFromNEI, addedMats, params);
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

	public List<JsonLoadedItemStack> getCatalysts()
	{
		return catalysts;
	}
}
