package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.Map.Entry;

public class CampfirePanRecipes
{
	private static final CampfirePanRecipes SMELTINGBASE = new CampfirePanRecipes();

	private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();

	private CampfirePanRecipes()
	{
		// nothing here
	}

	public static void addRecipe(Item input, ItemStack output, float experience)
	{
		smelting().addLists(input, output, experience);
	}

	public void addLists(Item input, ItemStack itemStack, float experience)
	{
		putLists(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), itemStack, experience);
	}

	public static CampfirePanRecipes smelting()
	{
		return SMELTINGBASE;
	}

	public void putLists(ItemStack itemStack, ItemStack itemStack2, float experience)
	{
		smeltingList.put(itemStack, itemStack2);
		experienceList.put(itemStack2, Float.valueOf(experience));
	}

	public static void addRecipe(Block input, ItemStack output, float experience)
	{
		smelting().addLists(Item.getItemFromBlock(input), output, experience);
	}

	public static void addRecipe(ItemStack input, ItemStack output, float experience)
	{
		smelting().putLists(input, output, experience);
	}

	public ItemStack getSmeltingResult(ItemStack stack)
	{
		Iterator iterator = smeltingList.entrySet().iterator();
		Entry entry;

		do
		{
			if (!iterator.hasNext())
			{
				return null;
			}

			entry = (Entry)iterator.next();
		} while (!canBeSmelted(stack, (ItemStack)entry.getKey()));

		return (ItemStack)entry.getValue();
	}

	private boolean canBeSmelted(ItemStack stack, ItemStack stack2)
	{
		return stack2.getItem() == stack.getItem()
				&& (stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == stack
				.getItemDamage());
	}

	public float giveExperience(ItemStack stack)
	{
		Iterator iterator = experienceList.entrySet().iterator();
		Entry entry;

		do
		{
			if (!iterator.hasNext())
			{
				return 0.0f;
			}

			entry = (Entry)iterator.next();
		} while (!canBeSmelted(stack, (ItemStack)entry.getKey()));

		if (stack.getItem().getSmeltingExperience(stack) != -1)
		{
			return stack.getItem().getSmeltingExperience(stack);
		}

		return (Float)entry.getValue();
	}

	public static Map getSmeltingList()
	{
		return smelting().smeltingList;
	}
}
