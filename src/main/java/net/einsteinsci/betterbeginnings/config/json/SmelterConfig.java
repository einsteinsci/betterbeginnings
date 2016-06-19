package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonSmelterRecipe;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonSmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.einsteinsci.betterbeginnings.util.RegistryUtil;
import net.einsteinsci.betterbeginnings.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmelterConfig implements IJsonConfig
{
	public static final SmelterConfig INSTANCE = new SmelterConfig();

	public static final List<ItemStack> AFFECTED_INPUTS = new ArrayList<>();

	private static JsonSmelterRecipeHandler initialRecipes = new JsonSmelterRecipeHandler();

	private JsonSmelterRecipeHandler mainRecipes = new JsonSmelterRecipeHandler();
	private JsonSmelterRecipeHandler customRecipes = new JsonSmelterRecipeHandler();
	private JsonSmelterRecipeHandler autoRecipes = new JsonSmelterRecipeHandler();

	private List<JsonSmelterRecipeHandler> includes = new ArrayList<>();

	private static boolean hasGenerated = false;

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

	public static JsonSmelterRecipe convert(ItemStack input, ItemStack output)
	{
		return new JsonSmelterRecipe(input, output, 0.1f, 1, 1);
	}

	@Override
	public String getSubFolder()
	{
		return "Smelter";
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

		autoRecipes = BBJsonLoader.deserializeObject(autoJson, JsonSmelterRecipeHandler.class);

		for (JsonSmelterRecipe j : autoRecipes.getRecipes())
		{
			j.register();
		}
	}

	public void generateAutoConfig()
	{
		for (Object obj : FurnaceRecipes.instance().getSmeltingList().entrySet())
		{
			if (!(obj instanceof Map.Entry))
			{
				continue; // No idea if this works.
			}

			Map.Entry<ItemStack, ItemStack> kvp = (Map.Entry<ItemStack, ItemStack>)obj;

			List<String> inputNames = RegistryUtil.getOreNames(kvp.getKey());
			boolean isOre = false;
			for (String s : inputNames)
			{
				if (s.toLowerCase().startsWith("ore"))
				{
					isOre = true;
					break;
				}
			}

			if (isOre && !RegistryUtil.getModOwner(kvp.getKey().getItem()).equals("minecraft"))
			{
				if (!Util.listContainsItemStackIgnoreSize(AFFECTED_INPUTS, kvp.getKey()))
				{
					AFFECTED_INPUTS.add(kvp.getKey());
				}

				if (!SmelterRecipeHandler.instance().existsRecipeFrom(kvp.getKey()))
				{
					JsonSmelterRecipe recipe = convert(kvp.getKey(), kvp.getValue());
					recipe.register();
					autoRecipes.getRecipes().add(recipe);
				}
			}
		}

		hasGenerated = true;
	}

	// generates affected outputs without adding recipes
	public void generateAffectedInputs()
	{
		if (!hasGenerated)
		{
			for (Object obj : FurnaceRecipes.instance().getSmeltingList().entrySet())
			{
				if (!(obj instanceof Map.Entry))
				{
					continue; // No idea if this works.
				}

				Map.Entry<ItemStack, ItemStack> kvp = (Map.Entry<ItemStack, ItemStack>)obj;

				List<String> inputNames = RegistryUtil.getOreNames(kvp.getKey());
				boolean isOre = false;
				for (String s : inputNames)
				{
					if (s.toLowerCase().startsWith("ore"))
					{
						isOre = true;
						break;
					}
				}

				if (isOre && !RegistryUtil.getModOwner(kvp.getKey().getItem()).equals("minecraft"))
				{
					if (!Util.listContainsItemStackIgnoreSize(AFFECTED_INPUTS, kvp.getKey()))
					{
						AFFECTED_INPUTS.add(kvp.getKey());
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
			JsonSmelterRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonSmelterRecipeHandler.class);

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

		json = BBJsonLoader.serializeObject(customRecipes);
		File customf = new File(subfolder, "custom.json");
		FileUtil.overwriteAllText(customf, json);
	}

	@Override
	public void saveAutoJson(File subfolder)
	{
		String json = BBJsonLoader.serializeObject(autoRecipes);
		File autof = new File(subfolder, "auto.json");
		FileUtil.overwriteAllText(autof, json);
	}

	public JsonSmelterRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}

	public JsonSmelterRecipeHandler getCustomRecipes()
	{
		return customRecipes;
	}
}
