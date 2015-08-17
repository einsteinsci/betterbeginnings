package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.GuiDoubleWorkbench;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedCraftingHandler;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

public class NEIAdvancedCraftingHandler extends TemplateRecipeHandler
{
	public class AdvancedCachedRecipe extends CachedRecipe
	{
		PositionedStack[] inputs;
		PositionedStack[] catalysts;
		PositionedStack output;

		public AdvancedCachedRecipe(AdvancedRecipe rec)
		{
			inputs = new PositionedStack[9];
			output = new PositionedStack(rec.getRecipeOutput(), 132, 51);

			ItemStack[] grid = rec.getThreeByThree();
			for (int y = 0; y < 3; y++)
			{
				for (int x = 0; x < 3; x++)
				{
					int i = y * 3 + x;

					if (grid[i] == null)
					{
						continue;
					}

					inputs[i] = new PositionedStack(grid[i], 38 + x * 18, 33 + y * 18);
				}
			}

			catalysts = new PositionedStack[4];
			OreRecipeElement[] side = rec.getNeededMaterials();
			for (int i = 0; i < side.length; i++)
			{
				if (side[i] == null)
				{
					continue;
				}

				catalysts[i] = new PositionedStack(side[i].getFirst(), 11, 23 + i * 18);
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
			for (int i = 0; i < catalysts.length; i++)
			{
				if (catalysts[i] != null)
				{
					buf.add(catalysts[i]);
				}
			}

			return getCycledIngredients(cycleticks / 48, buf);
		}

		@Override
		public PositionedStack getResult()
		{
			return output;
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (AdvancedRecipe adv : AdvancedCraftingHandler.getRecipeList())
		{
			if (adv.hideFromNEI)
			{
				continue;
			}

			if (adv.getRecipeOutput().getItem() == result.getItem() &&
				(result.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
					adv.getRecipeOutput().getItemDamage() == result.getItemDamage()))
			{
				arecipes.add(new AdvancedCachedRecipe(adv));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		List<ItemStack> foundOutputs = new ArrayList<>();

		for (AdvancedRecipe adv : AdvancedCraftingHandler.getRecipeList())
		{
			if (adv.hideFromNEI)
			{
				continue;
			}

			boolean found = false;
			OreRecipeElement[] inp = adv.recipeItems;
			for (int i = 0; i < inp.length; i++)
			{
				OreRecipeElement ore = inp[i];

				if (ore == null)
				{
					continue;
				}

				if (ore.matches(ingredient))
				{
					found = true;
					break;
				}
			}

			if (found)
			{
				arecipes.add(new AdvancedCachedRecipe(adv));
			}

			found = false;
			OreRecipeElement[] ores = adv.getNeededMaterials();
			for (int i = 0; i < ores.length; i++)
			{
				OreRecipeElement ore = ores[i];

				if (ore == null)
				{
					continue;
				}

				if (ore.matches(ingredient))
				{
					found = true;
					break;
				}
			}

			if (found)
			{
				arecipes.add(new AdvancedCachedRecipe(adv));
			}
		}
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiDoubleWorkbench.class;
	}

	@Override
	public String getGuiTexture()
	{
		return ModMain.MODID + ":textures/gui/container/doubleWorkbench_MC18.png";
	}

	@Override
	public String getRecipeName()
	{
		return I18n.format("container.craftingdouble");
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "doubleworkbench";
	}

	// typeo
	@Override
	public int recipiesPerPage()
	{
		return 1;
	}

	public void drawBackground(int recipe)
	{
		GlStateManager.color(1, 1, 1, 1);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(8, 20, 25, 4, 150, 75);
	}
}
