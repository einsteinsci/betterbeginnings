package net.einsteinsci.noobcraft.register.recipe;

import net.einsteinsci.noobcraft.inventory.InventoryWorkbenchAdditionalMaterials;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

@Deprecated
public class BrickOvenRecipe
{
	/** How many horizontal slots this recipe is wide. */
	public final int recipeWidth;
	/** How many vertical slots this recipe uses. */
	public final int recipeHeight;
	/** Is a array of ItemStack that composes the recipe. */
	public final ItemStack[] recipeItems;
	/** Is the ItemStack that you get when craft the recipe. */
	private ItemStack recipeOutput;
	
	// ...something...
	private boolean strangeFlag;
	
	public BrickOvenRecipe(int width, int height, ItemStack[] items, ItemStack output)
	{
		recipeWidth = width;
		recipeHeight = height;
		recipeItems = items;
		recipeOutput = output;
	}
	
	public boolean matches(InventoryCrafting invCrafting, InventoryWorkbenchAdditionalMaterials materials,
		World p_77569_2_)
	{
		for (int i = 0; i <= 3 - recipeWidth; ++i)
		{
			for (int j = 0; j <= 3 - recipeHeight; ++j)
			{
				if (checkMatch(invCrafting, i, j, true))
				{
					return true;
				}
				
				if (checkMatch(invCrafting, i, j, false))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ItemStack getCraftingResult(InventoryCrafting crafting)
	{
		ItemStack itemstack = getRecipeOutput().copy();
		
		if (strangeFlag)
		{
			for (int i = 0; i < crafting.getSizeInventory(); ++i)
			{
				ItemStack itemstack1 = crafting.getStackInSlot(i);
				
				if (itemstack1 != null && itemstack1.hasTagCompound())
				{
					itemstack.setTagCompound((NBTTagCompound) itemstack1.stackTagCompound.copy());
				}
			}
		}
		
		return itemstack;
	}
	
	// @Override
	public int getRecipeSize()
	{
		return recipeWidth * recipeHeight;
	}
	
	// @Override
	public ItemStack getRecipeOutput()
	{
		return recipeOutput;
	}
	
	public BrickOvenRecipe func_92100_c()
	{
		strangeFlag = true;
		return this;
	}
	
	public boolean checkMatch(InventoryCrafting crafting, int width, int height, boolean flag4)
	{
		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 3; ++l)
			{
				int i1 = k - width;
				int j1 = l - height;
				ItemStack neededCraftingStack = null;
				
				if (i1 >= 0 && j1 >= 0 && i1 < recipeWidth && j1 < recipeHeight)
				{
					if (flag4)
					{
						neededCraftingStack = recipeItems[recipeWidth - i1 - 1 + j1 * recipeWidth];
					}
					else
					{
						neededCraftingStack = recipeItems[i1 + j1 * recipeWidth];
					}
				}
				
				ItemStack craftingStackInQuestion = crafting.getStackInRowAndColumn(k, l);
				
				if (craftingStackInQuestion != null || neededCraftingStack != null)
				{
					if (craftingStackInQuestion == null && neededCraftingStack != null
						|| craftingStackInQuestion != null && neededCraftingStack == null)
					{
						return false;
					}
					
					if (neededCraftingStack.getItem() != craftingStackInQuestion.getItem())
					{
						return false;
					}
					
					if (neededCraftingStack.getItemDamage() != OreDictionary.WILDCARD_VALUE
						&& neededCraftingStack.getItemDamage() != craftingStackInQuestion.getItemDamage())
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
