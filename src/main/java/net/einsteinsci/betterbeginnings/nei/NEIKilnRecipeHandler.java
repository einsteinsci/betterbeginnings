package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.*;
import net.einsteinsci.betterbeginnings.gui.GuiKiln;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class NEIKilnRecipeHandler extends TemplateRecipeHandler
{
	public class KilnCachedRecipe extends CachedRecipe
	{
		PositionedStack input;
		PositionedStack output;

		public KilnCachedRecipe(ItemStack _input, ItemStack _output)
		{
			input = new PositionedStack(_input, 51, 6);
			output = new PositionedStack(_output, 111, 24);
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 48, Collections.singletonList(input));
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
		return GuiKiln.class;
	}

	@Override
	public String getGuiTexture()
	{
		// vanilla
		return "minecraft:textures/gui/container/furnace.png";
	}

	@Override
	public String getRecipeName()
	{
		return I18n.format("container.kiln");
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (Object obj : KilnRecipeHandler.getSmeltingList().entrySet())
		{
			Map.Entry entry = (Map.Entry)obj;

			ItemStack inp = (ItemStack)entry.getKey();
			ItemStack outp = (ItemStack)entry.getValue();

			if (outp.getItem() == result.getItem() &&
				(result.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
				outp.getItemDamage() == result.getItemDamage()))
			{
				arecipes.add(new KilnCachedRecipe(inp, outp));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (Object obj : KilnRecipeHandler.getSmeltingList().entrySet())
		{
			Map.Entry entry = (Map.Entry)obj;

			ItemStack inp = (ItemStack)entry.getKey();
			ItemStack outp = (ItemStack)entry.getValue();

			if (inp.getItem() == ingredient.getItem() &&
				(inp.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
				inp.getItemDamage() == ingredient.getItemDamage()))
			{
				arecipes.add(new KilnCachedRecipe(inp, outp));
			}
		}
	}

	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
		drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "kiln";
	}

	private static Set<Item> excludedFuels()
	{
		Set<Item> efuels = new HashSet<>();
		efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
		efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
		efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
		efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
		efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
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

			int burnTime = TileEntityKiln.getItemBurnTimeStatic(item);
			if (burnTime > 0)
			{
				afuels.add(new FurnaceRecipeHandler.FuelPair(item.copy(), burnTime));
			}
		}
	}
}