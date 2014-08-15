package net.einsteinsci.noobcraft.register.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CampfireRecipes
{
	private static final CampfireRecipes SMELTINGBASE = new CampfireRecipes();
	
	private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();
	
	private CampfireRecipes()
	{
		// nothing here
	}
	
	public static CampfireRecipes smelting()
	{
		return SMELTINGBASE;
	}
	
	public static void addRecipe(Item input, ItemStack output, float experience)
	{
		smelting().addLists(input, output, experience);
	}
	
	public static void addRecipe(Block input, ItemStack output, float experience)
	{
		smelting().addLists(Item.getItemFromBlock(input), output, experience);
	}
	
	public static void addRecipe(ItemStack input, ItemStack output, float experience)
	{
		smelting().putLists(input, output, experience);
	}
	
	public void addLists(Item input, ItemStack itemStack, float experience)
	{
		putLists(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), itemStack, experience);
	}
	
	public void putLists(ItemStack itemStack, ItemStack itemStack2, float experience)
	{
		smeltingList.put(itemStack, itemStack2);
		experienceList.put(itemStack2, Float.valueOf(experience));
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
			
			entry = (Entry) iterator.next();
		} while (!canBeSmelted(stack, (ItemStack) entry.getKey()));
		
		return (ItemStack) entry.getValue();
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
			
			entry = (Entry) iterator.next();
		} while (!canBeSmelted(stack, (ItemStack) entry.getKey()));
		
		if (stack.getItem().getSmeltingExperience(stack) != -1)
		{
			return stack.getItem().getSmeltingExperience(stack);
		}
		
		return ((Float) entry.getValue()).floatValue();
	}
}
