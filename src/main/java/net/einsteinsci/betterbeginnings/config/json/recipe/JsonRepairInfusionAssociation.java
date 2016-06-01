package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItemStack;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.einsteinsci.betterbeginnings.util.InfusionRepairUtil;
import net.minecraft.item.ItemStack;

public class JsonRepairInfusionAssociation
{
	private int enchantmentID;
	private JsonLoadedItemStack associatedItem;

	public JsonRepairInfusionAssociation(int enchID, OreRecipeElement stack)
	{
		enchantmentID = enchID;

		if (!stack.getOreDictionaryEntry().isEmpty())
		{
			associatedItem = JsonLoadedItemStack.makeOreDictionary(stack.getOreDictionaryEntry());
		}
		else
		{
			associatedItem = new JsonLoadedItemStack(stack.getFirst());
		}
	}

	public void register()
	{
		if (associatedItem.isOreDictionary())
		{
			InfusionRepairUtil.registerEnchantment(enchantmentID, new OreRecipeElement(associatedItem.getItemName()));
		}
		else
		{
			InfusionRepairUtil.registerEnchantment(enchantmentID, new OreRecipeElement(associatedItem.getFirstItemStackOrNull()));
		}
	}

	public int getEnchantmentID()
	{
		return enchantmentID;
	}

	public JsonLoadedItemStack getAssociatedItem()
	{
		return associatedItem;
	}
}
