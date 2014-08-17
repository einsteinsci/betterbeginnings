package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.Map.Entry;

public class SmelterRecipeHandler
{
	private static final SmelterRecipeHandler SMELTINGBASE = new SmelterRecipeHandler();

	// private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();
	// private Map gravelMap = new HashMap();

	private List<SmelterRecipe> recipes = new ArrayList<SmelterRecipe>();

	private SmelterRecipeHandler()
	{
		// nothing here
	}

	public static void addRecipe(Item input, ItemStack output, float experience, int gravel)
	{
		smelting().addLists(input, output, experience, gravel);
	}

	public void addLists(Item input, ItemStack output, float experience, int gravel)
	{
		putLists(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), output, experience, gravel);
	}

	public static SmelterRecipeHandler smelting()
	{
		return SMELTINGBASE;
	}

	public void putLists(ItemStack input, ItemStack output, float experience, int gravel)
	{
		// smeltingList.put(input, output);
		experienceList.put(output, Float.valueOf(experience));
		// gravelMap.put(output, Integer.valueOf(gravel));

		recipes.add(new SmelterRecipe(output, input, experience, gravel));
	}

	public static void addRecipe(Block input, ItemStack output, float experience, int gravel)
	{
		smelting().addLists(Item.getItemFromBlock(input), output, experience, gravel);
	}

	public static void addRecipe(ItemStack input, ItemStack output, float experience, int gravel)
	{
		smelting().putLists(input, output, experience, gravel);
	}

	public ItemStack getSmeltingResult(ItemStack input)
	{
		for (SmelterRecipe recipe : recipes)
		{
			if (recipe.getInput().getItem() == input.getItem())
			{
				return recipe.getOutput();
			}
		}

		return null;

		// Iterator iterator = smeltingList.entrySet().iterator();
		// Entry entry;
		//
		// do
		// {
		// if (!iterator.hasNext())
		// {
		// return null;
		// }
		//
		// entry = (Entry)iterator.next();
		// } while (!canBeSmelted(input, (ItemStack)entry.getKey()));
		//
		// return (ItemStack)entry.getValue();
	}

	public int getGravelCount(ItemStack stack)
	{
		for (SmelterRecipe recipe : recipes)
		{
			if (recipe.getInput().getItem() == stack.getItem())
			{
				return recipe.getGravel();
			}
		}

		return -1;

		// Iterator iterator = gravelMap.entrySet().iterator();
		// Entry entry;
		//
		// do
		// {
		// if (!iterator.hasNext())
		// {
		// return 0;
		// }
		//
		// entry = (Entry)iterator.next();
		// } while (!canBeSmelted(stack, (ItemStack)entry.getKey()));
		//
		// return (int)entry.getValue();
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

		return ((Float)entry.getValue()).floatValue();
	}

	private boolean canBeSmelted(ItemStack stack, ItemStack stack2)
	{
		return stack2.getItem() == stack.getItem() &&
				(stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == stack
						.getItemDamage());
	}
}
