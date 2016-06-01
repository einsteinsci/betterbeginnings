package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItem;
import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItemStack;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JsonBrickOvenShapelessRecipe
{
	private JsonLoadedItemStack output;
	private List<JsonLoadedItem> inputs;

	public JsonBrickOvenShapelessRecipe(ItemStack output, Object... params)
	{
		this.output = new JsonLoadedItemStack(output);

		inputs = new ArrayList<>();
		for (Object obj : params)
		{
			if (obj instanceof JsonLoadedItem)
			{
				inputs.add((JsonLoadedItem)obj);
			}
			else if (obj instanceof ItemStack)
			{
				ItemStack stack = (ItemStack)obj;
				inputs.add(new JsonLoadedItem(stack));
			}
			else if (obj instanceof Item)
			{
				Item item = (Item)obj;
				inputs.add(new JsonLoadedItem(new ItemStack(item)));
			}
			else if (obj instanceof Block)
			{
				Block block = (Block)obj;
				inputs.add(new JsonLoadedItem(new ItemStack(block)));
			}
			else if (obj instanceof String)
			{
				String ore = (String)obj;
				inputs.add(JsonLoadedItem.makeOreDictionary(ore));
			}
		}
	}

	public JsonBrickOvenShapelessRecipe(JsonLoadedItemStack output, List<JsonLoadedItem> inputs)
	{
		this.output = output;
		this.inputs = inputs;
	}

	public void register()
	{
		List<Object> res = new ArrayList<>();
		for (JsonLoadedItem jli : inputs)
		{
			if (jli.isOreDictionary())
			{
				res.add(jli.getItemName());
			}
			else
			{
				ItemStack stack = jli.getFirstItemStackOrNull();
				if (stack != null)
				{
					res.add(stack);
				}
			}
		}
		Object[] params = res.toArray();

		BrickOvenRecipeHandler.addShapelessRecipe(output.getFirstItemStackOrNull(), params);
	}
}
