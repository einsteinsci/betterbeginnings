package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by einsteinsci on 11/18/2014.
 */
public class OreRecipeElement
{
	public int stackSize;
	List<ItemStack> validItems;
	String oreDictionaryEntry;

	public OreRecipeElement(ItemStack stack)
	{
		validItems = new ArrayList<ItemStack>();
		validItems.add(stack);
		oreDictionaryEntry = "";
		stackSize = stack.stackSize;
	}

	public OreRecipeElement(String dictionaryEntry, int size)
	{
		validItems = new ArrayList<ItemStack>();
		validItems.addAll(OreDictionary.getOres(dictionaryEntry));
		oreDictionaryEntry = dictionaryEntry;
		stackSize = size;
	}

	public OreRecipeElement(List<ItemStack> valid, String entry, int size)
	{
		validItems = valid;
		oreDictionaryEntry = entry;
		stackSize = size;
	}

	public boolean matches(ItemStack stackGiven)
	{
		for (ItemStack valid : validItems)
		{
			if (valid.getItem() == stackGiven.getItem() && (valid.getItemDamage() == stackGiven.getItemDamage() ||
					valid.getItemDamage() == OreDictionary.WILDCARD_VALUE))
			{
				return true;
			}
		}

		return false;
	}

	public boolean matchesCheckSize(ItemStack stackGiven)
	{
		for (ItemStack valid : validItems)
		{
			if (valid.getItem() == stackGiven.getItem() && (valid.getItemDamage() == stackGiven.getItemDamage() ||
					valid.getItemDamage() == OreDictionary.WILDCARD_VALUE) && stackSize <= stackGiven.stackSize)
			{
				return true;
			}
		}

		return false;
	}

	public List<ItemStack> getValidItems()
	{
		return validItems;
	}

	public String getOreDictionaryEntry()
	{
		return oreDictionaryEntry;
	}

	public OreRecipeElement copy()
	{
		return new OreRecipeElement(validItems, oreDictionaryEntry, stackSize);
	}

	public ItemStack getFirst()
	{
		if (validItems != null && !validItems.isEmpty())
		{
			ItemStack zero = validItems.get(0);
			return new ItemStack(zero.getItem(), stackSize, zero.getItemDamage());
		}
		else
		{
			return null;
		}
	}
}
