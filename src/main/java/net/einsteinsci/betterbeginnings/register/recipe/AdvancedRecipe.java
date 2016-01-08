package net.einsteinsci.betterbeginnings.register.recipe;

import org.apache.logging.log4j.Level;

import net.einsteinsci.betterbeginnings.inventory.InventoryWorkbenchAdditionalMaterials;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AdvancedRecipe
{
	/**
	 * How many horizontal slots this recipe is wide.
	 */
	public final int recipeWidth;
	/**
	 * How many vertical slots this recipe uses.
	 */
	public final int recipeHeight;
	/**
	 * Is a array of ItemStack that composes the recipe.
	 */
	public final OreRecipeElement[] recipeItems;
	/**
	 * Is the ItemStack that you get when craft the recipe.
	 */
	private ItemStack recipeOutput;
	/**
	 * Whether the recipe should be valid when mirrored
	 */
	protected boolean mirrored = true;
	
	public final boolean hideFromNEI;

	

	// additional materials in the side slots
	private OreRecipeElement[] addedMaterials;

	// ...something...
	private boolean strangeFlag;

	public AdvancedRecipe(int width, int height, OreRecipeElement[] items, ItemStack output,
	                      OreRecipeElement[] materials, boolean hide)
	{
		recipeWidth = width;
		recipeHeight = height;
		recipeItems = items;
		recipeOutput = output;
		addedMaterials = materials;
		hideFromNEI = hide;
	}

	// @Override
	public boolean matches(InventoryCrafting invCrafting, InventoryWorkbenchAdditionalMaterials materials,
	                       World world)
	{
		for (int i = 0; i <= 3 - recipeWidth; ++i)
		{
			for (int j = 0; j <= 3 - recipeHeight; ++j)
			{
				if (checkMatch(invCrafting, materials, i, j, false))
				{
					return true;
				}

				if (mirrored && checkMatch(invCrafting, materials, i, j, true))
				{
					return true;
				}
			}
		}

		return false;
	}

	// Not sure what that fourth flag is...(it's mirroring)
	private boolean checkMatch(InventoryCrafting crafting, InventoryWorkbenchAdditionalMaterials materials, int startX,
	                           int startY, boolean mirror)
	{
		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 3; ++l)
			{
				int i1 = k - startX;
				int j1 = l - startY;

				OreRecipeElement neededCraftingStack = null;

				if (i1 >= 0 && j1 >= 0 && i1 < recipeWidth && j1 < recipeHeight)
				{
					if (mirror)
					{
						neededCraftingStack = recipeItems[recipeWidth - i1 - 1 + j1 * recipeWidth];
					}
					else
					{
						neededCraftingStack = recipeItems[i1 + j1 * recipeWidth];
					}
				}

				ItemStack craftingStackInQuestion = crafting.getStackInRowAndColumn(k, l);

				if (craftingStackInQuestion != null && neededCraftingStack != null)
				{
					if (!neededCraftingStack.matches(craftingStackInQuestion))
					{
						return false;
					}
				}
				else if ((craftingStackInQuestion == null) ^ (neededCraftingStack == null))
				{
					return false;
				}
			}
		}

		if(addedMaterials.length > materials.getSizeInventory())
		{
			LogUtil.log(Level.ERROR, "Recipe for " + this.recipeOutput.getDisplayName() + " has too many catalysts required.");
			return false;
		}
		
		for (int i2 = 0; i2 < addedMaterials.length; ++i2)
		{
			OreRecipeElement requiredMatStack = addedMaterials[i2];

			// we're out of added mats
			if( requiredMatStack == null )
			{
				break;
			}
			
			if (!requiredMatStack.matchesCheckSize(materials.getStackInSlot(i2)))
			{
				return false;
			}
		}

		return true;
	}

	public boolean matchesMostly(InventoryCrafting invCrafting, World world)
	{
		for (int i = 0; i <= 3 - recipeWidth; ++i)
		{
			for (int j = 0; j <= 3 - recipeHeight; ++j)
			{
				if (checkMatchMostly(invCrafting, i, j, false))
				{
					return true;
				}

				if (mirrored && checkMatchMostly(invCrafting, i, j, true))
				{
					return true;
				}
			}
		}

		return false;
	}

	public boolean checkMatchMostly(InventoryCrafting crafting, int width, int height, boolean flag4)
	{
		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 3; ++l)
			{
				int i1 = k - width;
				int j1 = l - height;
				OreRecipeElement neededCraftingStack = null;

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

				if (craftingStackInQuestion != null && neededCraftingStack != null)
				{
					if (!neededCraftingStack.matches(craftingStackInQuestion))
					{
						return false;
					}
				}
				else if ((craftingStackInQuestion == null) ^ (neededCraftingStack == null))
				{
					return false;
				}

			}
		}

		return true;
	}

	public int getNeededMaterialCount(Item material)
	{
		for (OreRecipeElement stack : addedMaterials)
		{
			if (stack.matches(new ItemStack(material)))
			{
				return stack.stackSize;
			}
		}

		return 0;
	}

	public OreRecipeElement[] getNeededMaterials()
	{
		return addedMaterials;
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
					itemstack.setTagCompound((NBTTagCompound)itemstack1.getTagCompound().copy());
				}
			}
		}

		return itemstack;
	}

	public ItemStack getRecipeOutput()
	{
		return recipeOutput;
	}

	public int getRecipeSize()
	{
		return recipeWidth * recipeHeight;
	}

	public ItemStack[] getThreeByThree()
	{
		ItemStack[] res = new ItemStack[9];

		int y = 0, x = 0;
		int v = 0, u = 0;
		for (int i = 0; i < getRecipeSize(); i++)
		{
			OreRecipeElement ore = recipeItems[u + v * recipeWidth];

			if (ore != null)
			{
				res[x + y * 3] = ore.getFirst();
			}

			u++;
			if (u >= recipeWidth)
			{
				u = 0;
				v++;
			}

			x = u;
			y = v;
		}

		return res;
	}

	public AdvancedRecipe func_92100_c()
	{
		strangeFlag = true;
		return this;
	}

	@Override
	public String toString()
	{
		return "Crafts " + recipeOutput.toString() + " with " + addedMaterials.length + " catalysts";
	}

	public boolean hasMaterial(ItemStack stack)
	{
		for (OreRecipeElement s : addedMaterials)
		{
			if (s.matches(stack))
			{
				return true;
			}
		}
		return false;
	}
}
