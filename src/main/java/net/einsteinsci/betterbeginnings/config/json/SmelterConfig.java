package net.einsteinsci.betterbeginnings.config.json;

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

public class SmelterConfig implements IJsonConfig
{
	public static final SmelterConfig INSTANCE = new SmelterConfig();

	private static JsonSmelterRecipeHandler initialRecipes = new JsonSmelterRecipeHandler();

	private JsonSmelterRecipeHandler mainRecipes = new JsonSmelterRecipeHandler();
	private JsonSmelterRecipeHandler customRecipes = new JsonSmelterRecipeHandler();

	private List<JsonSmelterRecipeHandler> includes = new ArrayList<>();

	public static void addRecipe(String input, ItemStack output, float experience, int boosters, int bonus)
	{
		initialRecipes.getRecipes().add(new JsonSmelterRecipe(JsonLoadedItem.makeOreDictionary(input),
			new JsonLoadedItemStack(output), experience, boosters, bonus));
	}
	public static void addRecipe(ItemStack input, ItemStack output, float experience, int boosters, int bonus)
	{
		initialRecipes.getRecipes().add(new JsonSmelterRecipe(input, output, experience, boosters, bonus));
	}
	public static void addRecipe(Item input, ItemStack output, float experience, int boosters, int bonus)
	{
		initialRecipes.getRecipes().add(new JsonSmelterRecipe(new ItemStack(input), output, experience, boosters, bonus));
	}
	public static void addRecipe(Block input, ItemStack output, float experience, int boosters, int bonus)
	{
		initialRecipes.getRecipes().add(new JsonSmelterRecipe(new ItemStack(input), output, experience, boosters, bonus));
	}

	@Override
	public String getSubFolder()
	{
		return "smelter";
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
	public boolean isOnlyMain()
	{
		return true;
	}

	@Override
	public void loadJsonConfig(FMLInitializationEvent e, String mainJson, String autoJson, String customJson)
	{
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonSmelterRecipeHandler.class);

		for (JsonSmelterRecipe j : mainRecipes.getRecipes())
		{
			j.register();
		}

		customRecipes = BBJsonLoader.deserializeObject(customJson, JsonSmelterRecipeHandler.class);

		for (JsonSmelterRecipe j : customRecipes.getRecipes())
		{
			j.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonSmelterRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonSmelterRecipeHandler.class);

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

			for (JsonSmelterRecipe r : handler.getRecipes())
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

	public JsonSmelterRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}
}
