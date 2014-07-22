package net.einsteinsci.noobcraft.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RemoveRecipes 
{	
	public static void remove()
	{
		List<ItemStack> removedRecipes = new ArrayList<ItemStack>();
		
		//Be sure to get the correct quantity
		removedRecipes.add(new ItemStack(Items.wooden_sword));
		removedRecipes.add(new ItemStack(Blocks.furnace));
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> iterator = recipes.iterator();
				          
		while (iterator.hasNext()) 
		{
			ItemStack stack = iterator.next().getRecipeOutput();
			if (stack != null && removedRecipes.contains(stack))
			{
				iterator.remove();
			}
		}
	}
}
