package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.GuiSmelter;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntitySmelter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class NEISmelterRecipeHandler extends TemplateRecipeHandler
{
	public class SmelterCachedRecipe extends CachedRecipe
	{
		PositionedStack input;
		PositionedStack output;
		PositionedStack gravel;

		public SmelterCachedRecipe(ItemStack _input, ItemStack _output, int _gravel)
		{
			input = new PositionedStack(_input, 41, 6);
			output = new PositionedStack(_output, 111, 24);
			gravel = new PositionedStack(new ItemStack(Blocks.gravel, _gravel), 61, 6);
		}
		public SmelterCachedRecipe(SmelterRecipe sr)
		{
			this(sr.getInput(), sr.getOutput(), sr.getGravel());
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 48, Arrays.asList(input, gravel));
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

	public static ArrayList<FurnaceRecipeHandler.FuelPair> afuels;

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
		return GuiSmelter.class;
	}

	@Override
	public String getGuiTexture()
	{
		return ModMain.MODID + ":textures/gui/container/smelter.png";
	}

	@Override
	public String getRecipeName()
	{
		return I18n.format("container.smelter");
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "smelter";
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (SmelterRecipe sr : SmelterRecipeHandler.getRecipes())
		{
			if (sr.getOutput().getItem() == result.getItem() &&
				(result.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
				sr.getOutput().getItemDamage() == result.getItemDamage()))
			{
				arecipes.add(new SmelterCachedRecipe(sr));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		if (ingredient.getItem() == Item.getItemFromBlock(Blocks.gravel))
		{
			for (SmelterRecipe sr : SmelterRecipeHandler.getRecipes())
			{
				arecipes.add(new SmelterCachedRecipe(sr));
			}
		}
		else
		{
			for (SmelterRecipe sr : SmelterRecipeHandler.getRecipes())
			{
				if (sr.getInput().getItem() == ingredient.getItem() &&
					(sr.getInput().getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					sr.getInput().getItemDamage() == ingredient.getItemDamage()))
				{
					arecipes.add(new SmelterCachedRecipe(sr));
				}
			}
		}
	}

	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
		drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
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

			int burnTime = TileEntitySmelter.getItemBurnTime(item);
			if (burnTime > 0)
			{
				afuels.add(new FurnaceRecipeHandler.FuelPair(item.copy(), burnTime));
			}
		}
	}
}
