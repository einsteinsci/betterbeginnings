package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBrickOvenShapelessRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.einsteinsci.betterbeginnings.util.RegistryUtil;
import net.einsteinsci.betterbeginnings.util.Util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.*;

public class BrickOvenConfig implements IJsonConfig
{
	public static final BrickOvenConfig INSTANCE = new BrickOvenConfig();

	public static final List<ItemStack> AFFECTED_OUTPUTS = new ArrayList<>();

	private static JsonBrickOvenRecipeHandler initialRecipes = new JsonBrickOvenRecipeHandler();

	private JsonBrickOvenRecipeHandler mainRecipes = new JsonBrickOvenRecipeHandler();
	private JsonBrickOvenRecipeHandler customRecipes = new JsonBrickOvenRecipeHandler();
	private JsonBrickOvenRecipeHandler autoRecipes = new JsonBrickOvenRecipeHandler();

	private List<JsonBrickOvenRecipeHandler> includes = new ArrayList<>();

	public static void addShapedRecipe(ItemStack output, Object... args)
	{
		initialRecipes.getShaped().add(new JsonBrickOvenShapedRecipe(output, args));
	}
	public static void addShapelessRecipe(ItemStack output, Object... args)
	{
		initialRecipes.getShapeless().add(new JsonBrickOvenShapelessRecipe(output, args));
	}

	// Not entirely sure how reliable these methods will be.
	public static JsonBrickOvenShapedRecipe convert(ShapedRecipes recipe)
	{
		char current = '1';
		Map<Object, Character> map = new HashMap<>();

		List<String> rows = new ArrayList<>();
		for (int y = 0; y < recipe.recipeHeight; y++)
		{
			String row = "";
			for (int x = 0; x < recipe.recipeWidth; x++)
			{
				int i = y * 3 + x;
				Object obj = recipe.recipeItems[i];

				char token = current;
				Tuple res = RegistryUtil.getRecipeCharacter(map, obj, current);
				if ((Boolean)res.getFirst())
				{
					current++;
				}

				row += token;
			}

			rows.add(row);
		}

		List<Object> res = new ArrayList<>();
		res.addAll(rows);

		for (Map.Entry<Object, Character> kvp : map.entrySet())
		{
			res.add(kvp.getValue());
			res.add(kvp.getKey());
		}

		return new JsonBrickOvenShapedRecipe(recipe.getRecipeOutput(), res.toArray());
	}
	public static JsonBrickOvenShapedRecipe convert(ShapedOreRecipe recipe)
	{
		// Yeah...I know. But there's no other way to find the width and height of a ShapedOreRecipe.
		Integer width = Util.getPrivateVariable(recipe, "width");
		Integer height = Util.getPrivateVariable(recipe, "height");

		if (width == null || height == null)
		{
			return null;
		}

		char current = '1';
		Map<Object, Character> map = new HashMap<>();

		List<String> rows = new ArrayList<>();
		for (int y = 0; y < height; y++)
		{
			String row = "";
			for (int x = 0; x < width; x++)
			{
				int i = y * 3 + x;
				Object obj = recipe.getInput()[i];

				char token = current;
				Tuple res = RegistryUtil.getRecipeCharacter(map, obj, current);
				if ((Boolean)res.getFirst())
				{
					current++;
				}

				row += token;
			}

			rows.add(row);
		}

		List<Object> res = new ArrayList<>();
		res.addAll(rows);

		for (Map.Entry<Object, Character> kvp : map.entrySet())
		{
			res.add(kvp.getValue());
			res.add(kvp.getKey());
		}

		return new JsonBrickOvenShapedRecipe(recipe.getRecipeOutput(), res.toArray());
	}
	public static JsonBrickOvenShapelessRecipe convert(ShapelessRecipes recipe)
	{
		return new JsonBrickOvenShapelessRecipe(recipe.getRecipeOutput(), recipe.recipeItems);
	}
	public static JsonBrickOvenShapelessRecipe convert(ShapelessOreRecipe recipe)
	{
		List<Object> inputs = new ArrayList<>();
		for (Object obj : recipe.getInput())
		{
			if (obj instanceof ItemStack)
			{
				inputs.add(obj);
			}
			else if (obj instanceof List)
			{
				try
				{
					String ore = RegistryUtil.getCommonOreDictName((List<ItemStack>)obj);
					inputs.add(ore);
				}
				catch (ClassCastException ex)
				{
					LogUtil.log(Level.ERROR, "Failed to cast list in ore dictionary conversion: " + ex.toString());
				}
			}
		}

		return new JsonBrickOvenShapelessRecipe(recipe.getRecipeOutput(), inputs);
	}
	public static JsonBrickOvenShapelessRecipe convertFurnace(ItemStack input, ItemStack output)
	{
		return new JsonBrickOvenShapelessRecipe(output, input);
	}

	private static boolean hasGenerated = false;

	@Override
	public String getSubFolder()
	{
		return "BrickOven";
	}

	@Override
	public String getMainJson(File subfolder)
	{
		File mainf = new File(subfolder, "main.json");
		String json = FileUtil.readAllText(mainf);
		if (json == null)
		{
			// Kind of inefficient, but it's easiest this way.
			json = BBJsonLoader.serializeObject(initialRecipes);
		}

		return json;
	}

	@Override
	public String getAutoJson(File subfolder)
	{
		File autof = new File(subfolder, "auto.json");
		String json = FileUtil.readAllText(autof);
		if (json == null)
		{
			json = "{}";
		}

		return json;
	}

	@Override
	public String getCustomJson(File subfolder)
	{
		File customf = new File(subfolder, "custom.json");
		String json = FileUtil.readAllText(customf);
		if (json == null)
		{
			json = "{}";
		}

		return json;
	}

	@Override
	public List<String> getIncludedJson(File subfolder)
	{
		List<String> res = new ArrayList<>();
		for (String fileName : customRecipes.getIncludes())
		{
			File incf = new File(subfolder, fileName);
			String json = FileUtil.readAllText(incf);
			res.add(json);
		}

		return res;
	}

	@Override
	public void loadJsonConfig(FMLInitializationEvent e, String mainJson, String autoJson, String customJson)
	{
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonBrickOvenRecipeHandler.class);
		for (JsonBrickOvenShapedRecipe j : mainRecipes.getShaped())
		{
			j.register();
		}
		for (JsonBrickOvenShapelessRecipe j : mainRecipes.getShapeless())
		{
			j.register();
		}

		customRecipes = BBJsonLoader.deserializeObject(customJson, JsonBrickOvenRecipeHandler.class);
		for (JsonBrickOvenShapedRecipe r : customRecipes.getShaped())
		{
			r.register();
		}
		for (JsonBrickOvenShapelessRecipe r : customRecipes.getShapeless())
		{
			r.register();
		}

		autoRecipes = BBJsonLoader.deserializeObject(autoJson, JsonBrickOvenRecipeHandler.class);
		for (JsonBrickOvenShapedRecipe r : autoRecipes.getShaped())
		{
			r.register();
		}
		for (JsonBrickOvenShapelessRecipe r : autoRecipes.getShapeless())
		{
			r.register();
		}
	}

	public void generateAutoConfig()
	{
		for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (!(obj instanceof IRecipe))
			{
				continue;
			}

			IRecipe r = (IRecipe)obj;
			ItemStack output = r.getRecipeOutput();

			if (output == null)
			{
				continue; // no idea why this happens
			}

			Item item = output.getItem();
			if (item != null && item instanceof ItemFood &&
				!RegistryUtil.getModOwner(item).equals("minecraft"))
			{
				if (Util.listContainsItemStackIgnoreSize(AFFECTED_OUTPUTS, r.getRecipeOutput()))
				{
					AFFECTED_OUTPUTS.add(r.getRecipeOutput());
				}

				// Don't add recipes for items already added.
				if (BrickOvenRecipeHandler.instance().existsRecipeFor(output))
				{
					continue;
				}

				if (r instanceof ShapedOreRecipe)
				{
					autoRecipes.getShaped().add(convert((ShapedOreRecipe)r));
				}
				else if (r instanceof ShapedRecipes)
				{
					autoRecipes.getShaped().add(convert((ShapedRecipes)r));
				}
				else if (r instanceof ShapelessOreRecipe)
				{
					autoRecipes.getShapeless().add(convert((ShapelessOreRecipe)r));
				}
				else if (r instanceof ShapelessRecipes)
				{
					autoRecipes.getShapeless().add(convert((ShapelessRecipes)r));
				}
			}
		}

		hasGenerated = true;
	}

	// generates affected outputs without adding recipes
	public void generateAffectedOutputs()
	{
		if (!hasGenerated)
		{
			for (Object obj : CraftingManager.getInstance().getRecipeList())
			{
				if (!(obj instanceof IRecipe))
				{
					continue;
				}

				IRecipe r = (IRecipe)obj;
				ItemStack output = r.getRecipeOutput();

				if (output == null)
				{
					continue; // no idea why this happens
				}

				Item item = output.getItem();
				if (item != null && item instanceof ItemFood &&
					!RegistryUtil.getModOwner(item).equals("minecraft"))
				{
					if (Util.listContainsItemStackIgnoreSize(AFFECTED_OUTPUTS, r.getRecipeOutput()))
					{
						AFFECTED_OUTPUTS.add(r.getRecipeOutput());
					}
				}
			}

			hasGenerated = true;
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonBrickOvenRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonBrickOvenRecipeHandler.class);

			boolean missingDependencies = false;
			for (String mod : handler.getModDependencies())
			{
				if (!Loader.isModLoaded(mod))
				{
					LogUtil.log(Level.WARN, "Mod '" + mod + "' missing, skipping all recipes in file.");
					missingDependencies = true;
					break;
				}
			}

			if (missingDependencies)
			{
				continue;
			}

			includes.add(handler);

			for (JsonBrickOvenShapedRecipe r : handler.getShaped())
			{
				r.register();
			}

			for (JsonBrickOvenShapelessRecipe r : handler.getShapeless())
			{
				r.register();
			}
		}
	}

	@Override
	public void savePostLoad(File subfolder)
	{
		String json = BBJsonLoader.serializeObject(mainRecipes);
		File mainf = new File(subfolder, "main.json");
		FileUtil.overwriteAllText(mainf, json);

		json = BBJsonLoader.serializeObject(customRecipes);
		File customf = new File(subfolder, "custom.json");
		FileUtil.overwriteAllText(customf, json);
	}

	public void saveAutoJson(File subfolder)
	{
		String json = BBJsonLoader.serializeObject(autoRecipes);
		File autof = new File(subfolder, "auto.json");
		FileUtil.overwriteAllText(autof, json);
	}

	public JsonBrickOvenRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}

	public JsonBrickOvenRecipeHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
