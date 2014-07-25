package net.einsteinsci.noobcraft.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.einsteinsci.noobcraft.register.recipe.BrickOvenShapedRecipe;
import net.einsteinsci.noobcraft.register.recipe.BrickOvenShapelessRecipe;
import net.einsteinsci.noobcraft.register.recipe.IBrickOvenRecipe;
import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BrickOvenRecipeHandler
{
	private static final BrickOvenRecipeHandler INSTANCE = new BrickOvenRecipeHandler();
	private List recipes = new ArrayList();
	
	// private Map experienceList;
	
	public BrickOvenRecipeHandler()
	{
		
	}
	
	public static BrickOvenRecipeHandler instance()
	{
		return INSTANCE;
	}
	
	public static void addShapedRecipe(ItemStack output, Object... args)
	{
		instance().putShapedRecipe(output, args);
	}
	
	public static void addShapelessRecipe(ItemStack output, Object... args)
	{
		instance().putShapelessRecipe(output, args);
	}
	
	@Deprecated
	public void addLists(ItemStack output, Object... args)
	{
		// putLists(output, args);
	}
	
	@Deprecated
	public void putLists(ItemStack output, Object... args)
	{
		
	}
	
	public BrickOvenShapedRecipe putShapedRecipe(ItemStack result, Object... args)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		
		if (args[i] instanceof String[])
		{
			String[] astring = (String[])args[i++];
			
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
				String s2 = (String)args[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}
		
		HashMap hashmap;
		
		for (hashmap = new HashMap(); i < args.length; i += 2)
		{
			Character character = (Character)args[i];
			ItemStack itemstack1 = null;
			
			if (args[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item)args[i + 1]);
			}
			else if (args[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block)args[i + 1], 1, OreDictionary.WILDCARD_VALUE);
			}
			else if (args[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack)args[i + 1];
			}
			
			hashmap.put(character, itemstack1);
		}
		
		ItemStack[] aitemstack = new ItemStack[j * k];
		
		for (int i1 = 0; i1 < j * k; ++i1)
		{
			char c0 = s.charAt(i1);
			
			if (hashmap.containsKey(Character.valueOf(c0)))
			{
				aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}
		
		BrickOvenShapedRecipe ovenrecipe = new BrickOvenShapedRecipe(j, k, aitemstack, result);
		recipes.add(ovenrecipe);
		return ovenrecipe;
	}
	
	public BrickOvenShapelessRecipe putShapelessRecipe(ItemStack output, Object... args)
	{
		ArrayList arraylist = new ArrayList();
		Object[] aobject = args;
		int i = args.length;
		
		for (int j = 0; j < i; ++j)
		{
			Object object1 = aobject[j];
			
			if (object1 instanceof ItemStack)
			{
				arraylist.add(((ItemStack)object1).copy());
			}
			else if (object1 instanceof Item)
			{
				arraylist.add(new ItemStack((Item)object1));
			}
			else
			{
				if (!(object1 instanceof Block))
				{
					throw new RuntimeException("Invalid shapeless recipe!");
				}
				
				arraylist.add(new ItemStack((Block)object1));
			}
		}
		
		BrickOvenShapelessRecipe recipe = new BrickOvenShapelessRecipe(output, arraylist);
		recipes.add(recipe);
		return recipe;
	}
	
	public ItemStack findMatchingRecipe(TileEntityBrickOven oven, World world)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;
		
		for (j = TileEntityBrickOven.INPUTSTART; j < oven.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = oven.getStackInSlot(j);
			// System.out.println("\nBrickOvenRecipeHandler.findMatchingRecipe(): j = " + j);
			
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
		
		if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 &&
			itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
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
				IBrickOvenRecipe recipe = (IBrickOvenRecipe)recipes.get(j);
				
				if (recipe.matches(oven, world))
				{
					return recipe.getCraftingResult(oven);
				}
			}
			
			return null;
		}
	}
	
	@Deprecated
	private boolean canBeSmelted(ItemStack stack, ItemStack stack2)
	{
		// return stack2.getItem() == stack.getItem()
		// && (stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
		// stack2.getItemDamage() == stack
		// .getItemDamage());
		
		return false;
	}
	
	public List getRecipeList()
	{
		return recipes;
	}
}
