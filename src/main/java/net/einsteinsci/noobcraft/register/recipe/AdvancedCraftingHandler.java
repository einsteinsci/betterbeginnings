package net.einsteinsci.noobcraft.register.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.einsteinsci.noobcraft.inventory.InventoryWorkbenchAdditionalMaterials;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class AdvancedCraftingHandler
{
	private static final AdvancedCraftingHandler CRAFTING = new AdvancedCraftingHandler();
	public List recipes = new ArrayList();
	
	public AdvancedCraftingHandler()
	{
		
	}
	
	public static AdvancedCraftingHandler crafting()
	{
		return CRAFTING;
	}
	
	public static void addAdvancedRecipe(ItemStack result, ItemStack[] additionalMaterials, Object... args)
	{
		crafting().addRecipe(result, additionalMaterials, args);
	}
	
	public AdvancedRecipe addRecipe(ItemStack result, ItemStack[] additionalMaterials, Object... args)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		
		if (args[i] instanceof String[])
		{
			String[] astring = (String[]) args[i++];
			
			for (int l = 0; l < astring.length; ++l)
			{
				String s1 = astring[l];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		else
		{
			while (args[i] instanceof String)
			{
				String s2 = (String) args[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}
		
		HashMap hashmap;
		
		for (hashmap = new HashMap(); i < args.length; i += 2)
		{
			Character character = (Character) args[i];
			ItemStack itemstack1 = null;
			
			if (args[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item) args[i + 1]);
			}
			else if (args[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block) args[i + 1], 1, OreDictionary.WILDCARD_VALUE);
			}
			else if (args[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack) args[i + 1];
			}
			
			hashmap.put(character, itemstack1);
		}
		
		ItemStack[] aitemstack = new ItemStack[j * k];
		
		for (int i1 = 0; i1 < j * k; ++i1)
		{
			char c0 = s.charAt(i1);
			
			if (hashmap.containsKey(Character.valueOf(c0)))
			{
				aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}
		
		AdvancedRecipe advancedrecipes = new AdvancedRecipe(j, k, aitemstack, result, additionalMaterials);
		recipes.add(advancedrecipes);
		return advancedrecipes;
	}
	
	public static AdvancedRecipe AdvancedRecipeByResult(ItemStack result)
	{
		for (Object obj : crafting().recipes)
		{
			if (obj instanceof AdvancedRecipe)
			{
				AdvancedRecipe recipe = (AdvancedRecipe) obj;
				
				if (recipe.getRecipeOutput().getItem() == result.getItem())
				{
					return recipe;
				}
			}
		}
		
		return null;
	}
	
	public boolean hasRecipe(InventoryCrafting crafting, World world)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;
		
		for (j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = crafting.getStackInSlot(j);
			
			if (itemstack2 != null)
			{
				if (i == 0)
				{
					itemstack = itemstack2;
				}
				
				if (i == 1)
				{
					itemstack1 = itemstack2;
				}
				
				++i;
			}
		}
		
		for (j = 0; j < recipes.size(); ++j)
		{
			AdvancedRecipe advrecipe = (AdvancedRecipe) recipes.get(j);
			
			if (advrecipe.matchesMostly(crafting, world))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public ItemStack findMatchingRecipe(InventoryCrafting crafting,
		InventoryWorkbenchAdditionalMaterials materials, World world)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;
		
		for (j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = crafting.getStackInSlot(j);
			
			if (itemstack2 != null)
			{
				if (i == 0)
				{
					itemstack = itemstack2;
				}
				
				if (i == 1)
				{
					itemstack1 = itemstack2;
				}
				
				++i;
			}
		}
		
		if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1
			&& itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
		{
			Item item = itemstack.getItem();
			int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
			int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
			int l = j1 + k + item.getMaxDamage() * 5 / 100;
			int i1 = item.getMaxDamage() - l;
			
			if (i1 < 0)
			{
				i1 = 0;
			}
			
			return new ItemStack(itemstack.getItem(), 1, i1);
		}
		else
		{
			for (j = 0; j < recipes.size(); ++j)
			{
				AdvancedRecipe advrecipe = (AdvancedRecipe) recipes.get(j);
				
				if (advrecipe.matches(crafting, materials, world))
				{
					return advrecipe.getCraftingResult(crafting);
				}
			}
			
			return null;
		}
	}
}
