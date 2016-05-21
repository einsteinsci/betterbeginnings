package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonCampfirePanRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonCampfireRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonCampfireRecipeHandler;
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

public class CampfireConfig implements IJsonConfig
{
	public static final CampfireConfig INSTANCE = new CampfireConfig();

	private static JsonCampfireRecipeHandler initialRecipes = new JsonCampfireRecipeHandler();

	private JsonCampfireRecipeHandler mainRecipes = new JsonCampfireRecipeHandler();
	private JsonCampfireRecipeHandler customRecipes = new JsonCampfireRecipeHandler();

	private List<JsonCampfireRecipeHandler> includes = new ArrayList<>();

	public static void addRecipe(ItemStack input, ItemStack output, float xp)
	{
		initialRecipes.getRecipes().add(new JsonCampfireRecipe(input, output, xp));
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
		initialRecipes.getRecipes().add(new JsonCampfireRecipe(JsonLoadedItem.makeOreDictionary(input),
			new JsonLoadedItemStack(output), xp));
	}
	public static void addPanRecipe(ItemStack input, ItemStack output, float xp)
	{
		initialRecipes.getPanRecipes().add(new JsonCampfirePanRecipe(input, output, xp));
	}
	public static void addPanRecipe(Item input, ItemStack output, float xp)
	{
		addPanRecipe(new ItemStack(input), output, xp);
	}
	public static void addPanRecipe(Block input, ItemStack output, float xp)
	{
		addPanRecipe(new ItemStack(input), output, xp);
	}
	public static void addPanRecipe(String input, ItemStack output, float xp)
	{
		initialRecipes.getPanRecipes().add(new JsonCampfirePanRecipe(JsonLoadedItem.makeOreDictionary(input),
			new JsonLoadedItemStack(output), xp));
	}

	@Override
	public String getSubFolder()
	{
		return "Campfire";
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
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonCampfireRecipeHandler.class);
		for (JsonCampfireRecipe j : mainRecipes.getRecipes())
		{
			j.register();
		}
		for (JsonCampfirePanRecipe p : mainRecipes.getPanRecipes())
		{
			p.register();
		}

		customRecipes = BBJsonLoader.deserializeObject(customJson, JsonCampfireRecipeHandler.class);
		for (JsonCampfireRecipe r : customRecipes.getRecipes())
		{
			r.register();
		}
		for (JsonCampfirePanRecipe p : customRecipes.getPanRecipes())
		{
			p.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonCampfireRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonCampfireRecipeHandler.class);

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

			for (JsonCampfireRecipe r : handler.getRecipes())
			{
				r.register();
			}
			for (JsonCampfirePanRecipe p : handler.getPanRecipes())
			{
				p.register();
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

	public JsonCampfireRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}

	public JsonCampfireRecipeHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
