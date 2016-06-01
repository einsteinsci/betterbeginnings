package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class OreRecipeElement
{
	public int stackSize;
	List<ItemStack> validItems;
	String oreDictionaryEntry;

	public OreRecipeElement(ItemStack stack)
	{
		validItems = new ArrayList<>();
		validItems.add(stack);
		oreDictionaryEntry = "";
		stackSize = stack.stackSize;
	}

	public OreRecipeElement(Item item)
	{
		this(new ItemStack(item));
	}
	public OreRecipeElement(Item item, int count)
	{
		this(new ItemStack(item, count));
	}
	public OreRecipeElement(Item item, int count, int meta)
	{
		this(new ItemStack(item, count, meta));
	}

	public OreRecipeElement(Block block)
	{
		this(new ItemStack(block));
	}
	public OreRecipeElement(Block block, int count)
	{
		this(new ItemStack(block, count));
	}
	public OreRecipeElement(Block block, int count, int meta)
	{
		this(new ItemStack(block, count, meta));
	}

	public OreRecipeElement(String dictionaryEntry, int size)
	{
		validItems = new ArrayList<>();
		validItems.addAll(OreDictionary.getOres(dictionaryEntry));
		oreDictionaryEntry = dictionaryEntry;
		stackSize = size;
	}

	public OreRecipeElement(String dictionaryEntry)
	{
		this(dictionaryEntry, 1);
	}

	public OreRecipeElement(List<ItemStack> valid, String entry, int size)
	{
		validItems = valid;
		oreDictionaryEntry = entry;
		stackSize = size;
	}

	public boolean matches(ItemStack stackGiven)
	{
		if(stackGiven  == null)
		{
			return false;
		}
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
		if(stackGiven == null)
		{
			return false;
		}
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
