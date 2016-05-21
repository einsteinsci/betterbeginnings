package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonAdvancedCraftingHandler;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonAdvancedRecipe;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdvancedCraftingConfig implements IJsonConfig
{
	public static final AdvancedCraftingConfig INSTANCE = new AdvancedCraftingConfig();

	private static JsonAdvancedCraftingHandler initialRecipes = new JsonAdvancedCraftingHandler();

	private JsonAdvancedCraftingHandler mainRecipes = new JsonAdvancedCraftingHandler();
	private JsonAdvancedCraftingHandler customRecipes = new JsonAdvancedCraftingHandler();

	private List<JsonAdvancedCraftingHandler> includes = new ArrayList<>();

	public static void addAdvancedRecipe(ItemStack result, Object[] additionalMaterials, Object... args)
	{
		addAdvancedRecipe(result, false, additionalMaterials, args);
	}
	public static void addAdvancedRecipe(ItemStack result, boolean hide, Object[] additionalMaterials, Object... args)
	{
		initialRecipes.getRecipes().add(new JsonAdvancedRecipe(result, hide, additionalMaterials, args));
	}

	@Override
	public String getSubFolder()
	{
		return "advancedcrafting";
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
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonAdvancedCraftingHandler.class);
		for (JsonAdvancedRecipe j : mainRecipes.getRecipes())
		{
			j.register();
		}

		customRecipes = BBJsonLoader.deserializeObject(customJson, JsonAdvancedCraftingHandler.class);
		for (JsonAdvancedRecipe r : customRecipes.getRecipes())
		{
			r.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonAdvancedCraftingHandler handler = BBJsonLoader.deserializeObject(json, JsonAdvancedCraftingHandler.class);

			if (handler == null)
			{
				LogUtil.log(Level.ERROR, "Could not deserialize included json.");
				continue;
			}

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

			for (JsonAdvancedRecipe r : handler.getRecipes())
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
	}

	public JsonAdvancedCraftingHandler getMainRecipes()
	{
		return mainRecipes;
	}

	public JsonAdvancedCraftingHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
