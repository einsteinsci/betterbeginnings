package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonRemoveRecipesHandler;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonRemovedCraftingRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonRemovedSmeltingRecipe;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RemovalConfig implements IJsonConfig
{
	public static final RemovalConfig INSTANCE = new RemovalConfig();

	private JsonRemoveRecipesHandler customRecipes = new JsonRemoveRecipesHandler();

	private List<JsonRemoveRecipesHandler> includes = new ArrayList<>();

	@Override
	public String getSubFolder()
	{
		return "Remove";
	}

	@Override
	public String getMainJson(File subfolder)
	{
		return "{}"; // No main, only custom.
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
		// no main, only custom.

		customRecipes = BBJsonLoader.deserializeObject(customJson, JsonRemoveRecipesHandler.class);
		for (JsonRemovedCraftingRecipe r : customRecipes.getCraftingRemoved())
		{
			r.register();
		}
		for (JsonRemovedSmeltingRecipe r : customRecipes.getSmeltingRemoved())
		{
			r.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonRemoveRecipesHandler handler = BBJsonLoader.deserializeObject(json, JsonRemoveRecipesHandler.class);

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

			for (JsonRemovedCraftingRecipe r : handler.getCraftingRemoved())
			{
				r.register();
			}
			for (JsonRemovedSmeltingRecipe r : handler.getSmeltingRemoved())
			{
				r.register();
			}
		}
	}

	@Override
	public void savePostLoad(File subfolder)
	{
		String json = BBJsonLoader.serializeObject(customRecipes);
		File customf = new File(subfolder, "custom.json");
		if (Files.notExists(customf.toPath()))
		{
			FileUtil.overwriteAllText(customf, json);
		}
	}

	@Override
	public void saveAutoJson(File subfolder)
	{ }

	public JsonRemoveRecipesHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
