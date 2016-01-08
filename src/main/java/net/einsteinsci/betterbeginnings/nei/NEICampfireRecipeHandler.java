package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.GuiCampfire;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.register.recipe.CampfirePanRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.CampfireRecipeHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;

import java.util.*;

public class NEICampfireRecipeHandler extends TemplateRecipeHandler
{
	public class CampfireCachedRecipe extends TemplateRecipeHandler.CachedRecipe
	{
		PositionedStack input;
		PositionedStack output;
		PositionedStack pan;

		public CampfireCachedRecipe(ItemStack _input, ItemStack _output, boolean _pan)
		{
			input = new PositionedStack(_input, 53, 1);
			output = new PositionedStack(_output, 113, 23);
			if (_pan)
			{
				pan = new PositionedStack(new ItemStack(RegisterItems.pan), 27, 24);
			}
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			if (pan != null)
			{
				return getCycledIngredients(cycleticks / 48, Arrays.asList(new Object[]{input, pan}));
			}
			else
			{
				return getCycledIngredients(cycleticks / 48, Collections.singletonList(input));
			}
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
		public PositionedStack stack;
		public int burnTime;

		public FuelPair(ItemStack ingred, int burnTime)
		{
			this.stack = new PositionedStack(ingred, 53, 46, false);
			this.burnTime = burnTime;
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
		return GuiCampfire.class;
	}

	@Override
	public String getGuiTexture()
	{
		return ModMain.MODID + ":textures/gui/container/campfire.png";
	}

	@Override
	public String getRecipeName()
	{
		return I18n.format("container.campfire");
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (Object obj : CampfireRecipeHandler.getSmeltingList().entrySet())
		{
			Map.Entry entry = (Map.Entry)obj;

			ItemStack inp = (ItemStack)entry.getKey();
			ItemStack outp = (ItemStack)entry.getValue();

			if (outp.getItem() == result.getItem() &&
				(result.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					outp.getItemDamage() == result.getItemDamage()))
			{
				arecipes.add(new CampfireCachedRecipe(inp, outp, false));
			}
		}

		for (Object obj : CampfirePanRecipeHandler.getSmeltingList().entrySet())
		{
			Map.Entry entry = (Map.Entry)obj;

			ItemStack inp = (ItemStack)entry.getKey();
			ItemStack outp = (ItemStack)entry.getValue();

			if (outp.getItem() == result.getItem() &&
				(result.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					outp.getItemDamage() == result.getItemDamage()))
			{
				arecipes.add(new CampfireCachedRecipe(inp, outp, true));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (Object obj : CampfireRecipeHandler.getSmeltingList().entrySet())
		{
			Map.Entry entry = (Map.Entry)obj;

			ItemStack inp = (ItemStack)entry.getKey();
			ItemStack outp = (ItemStack)entry.getValue();

			if (inp.getItem() == ingredient.getItem() &&
				(inp.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					inp.getItemDamage() == ingredient.getItemDamage()))
			{
				arecipes.add(new CampfireCachedRecipe(inp, outp, false));
			}
		}

		for (Object obj : CampfirePanRecipeHandler.getSmeltingList().entrySet())
		{
			Map.Entry entry = (Map.Entry)obj;

			ItemStack inp = (ItemStack)entry.getKey();
			ItemStack outp = (ItemStack)entry.getValue();

			if (inp.getItem() == ingredient.getItem() &&
				(inp.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					inp.getItemDamage() == ingredient.getItemDamage()))
			{
				arecipes.add(new CampfireCachedRecipe(inp, outp, true));
			}
		}
	}

	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(53, 21, 176, 0, 14, 22, 48, 7);
		drawProgressBar(76, 22, 176, 22, 24, 16, 48, 0);
		drawProgressBar(72, 46, 176, 39, 3, 16, 48, 7);
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "campfire";
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

			int burnTime = TileEntityCampfire.getBurnTimeForFuel(item);
			if (burnTime > 0)
			{
				afuels.add(new FuelPair(item.copy(), burnTime));
			}
		}
	}
}
