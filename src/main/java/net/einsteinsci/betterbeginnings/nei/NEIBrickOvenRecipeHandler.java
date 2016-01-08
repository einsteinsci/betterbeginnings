package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.GuiBrickOven;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapelessRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.IBrickOvenRecipe;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class NEIBrickOvenRecipeHandler extends TemplateRecipeHandler
{
	public class BrickOvenCachedRecipe extends CachedRecipe
	{
		PositionedStack[] inputs;
		PositionedStack output;

		public BrickOvenCachedRecipe(IBrickOvenRecipe ibr)
		{
			inputs = new PositionedStack[9];

			if (ibr instanceof BrickOvenShapedRecipe)
			{
				BrickOvenShapedRecipe shaped = (BrickOvenShapedRecipe)ibr;

				output = new PositionedStack(shaped.getRecipeOutput(), 119, 10);
				ItemStack[] grid = shaped.getThreeByThree();
				for (int y = 0; y < 3; y++)
				{
					for (int x = 0; x < 3; x++)
					{
						int i = y * 3 + x;

						if (grid[i] == null)
						{
							continue;
						}

						inputs[i] = new PositionedStack(grid[i], 25 + x * 18, 6 + y * 18);
					}
				}
			}
			else if (ibr instanceof BrickOvenShapelessRecipe)
			{
				BrickOvenShapelessRecipe shapeless = (BrickOvenShapelessRecipe)ibr;

				output = new PositionedStack(shapeless.getRecipeOutput(), 119, 10);
				ItemStack[] stacks = shapeless.getInputs();
				for (int y = 0; y < 3; y++)
				{
					for (int x = 0; x < 3; x++)
					{
						int i = y * 3 + x;

						if (stacks[i] == null)
						{
							continue;
						}

						inputs[i] = new PositionedStack(stacks[i], 25 + x * 18, 6 + y * 18);
					}
				}
			}
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			List<PositionedStack> buf = new ArrayList<>();
			for (int i = 0; i < inputs.length; i++)
			{
				if (inputs[i] != null)
				{
					buf.add(inputs[i]);
				}
			}

			return getCycledIngredients(cycleticks / 48, buf);
		}

		@Override
		public PositionedStack getOtherStack() {
			return afuels.get((cycleticks / 48) % afuels.size()).stack;
		}

		@Override
		public PositionedStack getResult()
		{
			return output;
		}
	}

	public static class FuelPair
	{
		public FuelPair(ItemStack ingred, int burnTime) {
			this.stack = new PositionedStack(ingred, 87, 47, false);
			this.burnTime = burnTime;
		}

		public PositionedStack stack;
		public int burnTime;
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (IBrickOvenRecipe ibr : BrickOvenRecipeHandler.getRecipeList())
		{
			if (ibr.getRecipeOutput().getItem() == result.getItem() &&
				(result.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					ibr.getRecipeOutput().getItemDamage() == result.getItemDamage()))
			{
				arecipes.add(new BrickOvenCachedRecipe(ibr));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (IBrickOvenRecipe ibr : BrickOvenRecipeHandler.getRecipeList())
		{
			boolean found = false;
			ItemStack[] inp = ibr.getInputs();
			for (int i = 0; i < inp.length; i++)
			{
				ItemStack is = inp[i];

				if (is == null)
				{
					continue;
				}

				if (is.getItem() == ingredient.getItem() &&
					(is.getItemDamage() == ingredient.getItemDamage() ||
					ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				{
					found = true;
					break;
				}
			}

			if (found)
			{
				arecipes.add(new BrickOvenCachedRecipe(ibr));
			}
		}
	}

	public static ArrayList<FuelPair> afuels;

	@Override
	public TemplateRecipeHandler newInstance()
	{
		if (afuels == null || afuels.isEmpty())
		{
			findFuels();
		}

		return super.newInstance();
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiBrickOven.class;
	}

	@Override
	public String getGuiTexture()
	{
		return ModMain.MODID + ":textures/gui/container/brickOven.png";
	}

	@Override
	public String getRecipeName()
	{
		return I18n.format("container.brickoven");
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "brickoven";
	}

	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(87, 30, 176, 0, 14, 14, 48, 7);
		drawProgressBar(84, 9, 176, 14, 24, 16, 48, 0);
	}

	private static Set<Item> excludedFuels()
	{
		Set<Item> efuels = new HashSet<>();
		efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
		efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
		efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
		efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
		efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
		efuels.add(Item.getItemFromBlock(Blocks.bedrock));
		return efuels;
	}

	private static void findFuels()
	{
		afuels = new ArrayList<>();
		Set<Item> efuels = excludedFuels();

		for (ItemStack item : ItemList.items)
		{
			Block block = Block.getBlockFromItem(item.getItem());
			if (block instanceof BlockDoor)
			{
				continue;
			}

			if (efuels.contains(item.getItem()))
			{
				continue;
			}

			int burnTime = TileEntityBrickOven.getItemBurnTimeStatic(item);
			if (burnTime > 0)
			{
				afuels.add(new FuelPair(item.copy(), burnTime));
			}
		}
	}
}
