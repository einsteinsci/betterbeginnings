package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonKilnRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonKilnRecipeHandler;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KilnConfig implements IJsonConfig
{
	public static final KilnConfig INSTANCE = new KilnConfig();

	private static JsonKilnRecipeHandler initialRecipes = new JsonKilnRecipeHandler();

	private JsonKilnRecipeHandler mainRecipes = new JsonKilnRecipeHandler();
	private JsonKilnRecipeHandler customRecipes = new JsonKilnRecipeHandler();

	private List<JsonKilnRecipeHandler> includes = new ArrayList<>();

	public static void addRecipe(ItemStack input, ItemStack output, float xp)
	{
		initialRecipes.getRecipes().add(new JsonKilnRecipe(input, output, xp));
	}
	public static void addRecipe(Item input, ItemStack output, float xp)
	{
		addRecipe(new ItemStack(input), output, xp);
	}
	public static void addRecipe(Block input, ItemStack output, float xp)
	{
		addRecipe(new ItemStack(input), output, xp);
	}
	public static void addRecipe(String input, ItemStack output, float xp)
	{
		initialRecipes.getRecipes().add(new JsonKilnRecipe(JsonLoadedItem.makeOreDictionary(input),
			new JsonLoadedItemStack(output), xp));
	}

	@Override
	public String getSubFolder()
	{
		return "Kiln";
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
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonKilnRecipeHandler.class);
		for (JsonKilnRecipe j : mainRecipes.getRecipes())
		{
			j.register();
		}

		customRecipes = BBJsonLoader.deserializeObject(customJson, JsonKilnRecipeHandler.class);
		for (JsonKilnRecipe r : customRecipes.getRecipes())
		{
			r.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonKilnRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonKilnRecipeHandler.class);

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

			for (JsonKilnRecipe r : handler.getRecipes())
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

	@Override
	public void saveAutoJson(File subfolder)
	{ }

	public JsonKilnRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}

	public JsonKilnRecipeHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
