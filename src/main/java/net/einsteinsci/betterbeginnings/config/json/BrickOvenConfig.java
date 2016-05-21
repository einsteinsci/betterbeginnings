package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBrickOvenShapelessRecipe;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BrickOvenConfig implements IJsonConfig
{
	public static final BrickOvenConfig INSTANCE = new BrickOvenConfig();

	private static JsonBrickOvenRecipeHandler initialRecipes = new JsonBrickOvenRecipeHandler();

	private JsonBrickOvenRecipeHandler mainRecipes = new JsonBrickOvenRecipeHandler();
	private JsonBrickOvenRecipeHandler customRecipes = new JsonBrickOvenRecipeHandler();

	private List<JsonBrickOvenRecipeHandler> includes = new ArrayList<>();

	public static void addShapedRecipe(ItemStack output, Object... args)
	{
		initialRecipes.getShaped().add(new JsonBrickOvenShapedRecipe(output, args));
	}
	public static void addShapelessRecipe(ItemStack output, Object... args)
	{
		initialRecipes.getShapeless().add(new JsonBrickOvenShapelessRecipe(output, args));
	}

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
		return "{}";
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

	public JsonBrickOvenRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}

	public JsonBrickOvenRecipeHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
