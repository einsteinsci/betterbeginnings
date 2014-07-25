package net.einsteinsci.noobcraft.register.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BrickOvenShapelessRecipe implements IBrickOvenRecipe
{
	/** Is the ItemStack that you get when craft the recipe. */
	private final ItemStack recipeOutput;
	/** Is a List of ItemStack that composes the recipe. */
	public final List recipeItems;
	
	public BrickOvenShapelessRecipe(ItemStack output, List input)
	{
		recipeOutput = output;
		recipeItems = input;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return recipeOutput;
	}
	
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(TileEntityBrickOven oven, World world)
	{
		ArrayList arraylist = new ArrayList(recipeItems);
		
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				ItemStack itemstack = oven.getStackInRowAndColumn(j, i);
				
				if (itemstack != null)
				{
					boolean flag = false;
					Iterator iterator = arraylist.iterator();
					
					while (iterator.hasNext())
					{
						ItemStack itemstack1 = (ItemStack)iterator.next();
						
						if (itemstack.getItem() == itemstack1.getItem() &&
							(itemstack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack.getItemDamage() == itemstack1
							.getItemDamage()))
						{
							flag = true;
							arraylist.remove(itemstack1);
							break;
						}
					}
					
					if (!flag)
					{
						return false;
					}
				}
			}
		}
		
		return arraylist.isEmpty();
	}
	
	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(TileEntityBrickOven oven)
	{
		return recipeOutput.copy();
	}
	
	/**
	 * Returns the size of the recipe area
	 */
	@Override
	public int getRecipeSize()
	{
		return recipeItems.size();
	}
}
