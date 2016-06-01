package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItem;
import net.einsteinsci.betterbeginnings.register.RemoveRecipes;
import net.minecraft.item.ItemStack;

public class JsonRemovedCraftingRecipe
{
	public JsonLoadedItem removedItem;

	public JsonRemovedCraftingRecipe(ItemStack stack)
	{
		removedItem = new JsonLoadedItem(stack);
	}

	public void register()
	{
		for (ItemStack rem : removedItem.getItemStacks())
		{
			RemoveRecipes.getCustomRemovedCraftingRecipes().add(rem);
		}
	}

	public JsonLoadedItem getRemovedItem()
	{
		return removedItem;
	}
}
