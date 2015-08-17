package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.*;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.GuiKiln;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipes;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

// region interface-only
/*
// Can't use TemplateRecipeHandler as it's not an interface :`(
@Optional.InterfaceList( {
	@Optional.Interface(iface = "codechicken.nei.recipe.ICraftingHandler", modid = "NotEnoughItems"),
	@Optional.Interface(iface = "codechicken.nei.recipe.IUsageHandler", modid = "NotEnoughItems") }
)
public class KilnRecipeHandler implements ICraftingHandler, IUsageHandler
{
	@Override
	public ICraftingHandler getRecipeHandler(String outputId, Object... results)
	{
		return null;
	}

	@Override
	public IUsageHandler getUsageHandler(String inputId, Object... ingredients)
	{
		return null;
	}

	@Override
	public String getRecipeName()
	{
		return null;
	}

	@Override
	public int numRecipes()
	{
		return 0;
	}

	@Override
	public void drawBackground(int recipe)
	{

	}

	@Override
	public void drawForeground(int recipe)
	{

	}

	@Override
	public List<PositionedStack> getIngredientStacks(int recipe)
	{
		return null;
	}

	@Override
	public List<PositionedStack> getOtherStacks(int recipetype)
	{
		return null;
	}

	@Override
	public PositionedStack getResultStack(int recipe)
	{
		return null;
	}

	@Override
	public void onUpdate()
	{

	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
	{
		return false;
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe)
	{
		return null;
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe)
	{
		return null;
	}

	@Override
	public int recipiesPerPage()
	{
		return 0;
	}

	@Override
	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
	{
		return null;
	}

	@Override
	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
	{
		return null;
	}

	@Override
	public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe)
	{
		return false;
	}

	@Override
	public boolean mouseClicked(GuiRecipe gui, int button, int recipe)
	{
		return false;
	}
} */
// endregion

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
		for (Object obj : KilnRecipes.getSmeltingList().entrySet())
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
		for (Object obj : KilnRecipes.getSmeltingList().entrySet())
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

			int burnTime = TileEntityKiln.getItemBurnTime(item);
			if (burnTime > 0)
			{
				afuels.add(new FurnaceRecipeHandler.FuelPair(item.copy(), burnTime));
			}
		}
	}
}