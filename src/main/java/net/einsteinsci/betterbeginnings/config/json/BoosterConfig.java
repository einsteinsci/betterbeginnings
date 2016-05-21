package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBooster;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonBoosterHandler;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoosterConfig implements IJsonConfig
{
	public static final BoosterConfig INSTANCE = new BoosterConfig();

	private static JsonBoosterHandler initialAssociations = new JsonBoosterHandler();

	private JsonBoosterHandler mainBoosters = new JsonBoosterHandler();
	private JsonBoosterHandler customBoosters = new JsonBoosterHandler();

	private List<JsonBoosterHandler> includes = new ArrayList<>();

	public static void registerBooster(ItemStack booster, float amount)
	{
		initialAssociations.getBoosters().add(new JsonBooster(booster, amount));
	}

	@Override
	public String getSubFolder()
	{
		return "SmelterBoosters";
	}

	@Override
	public String getMainJson(File subfolder)
	{
		File mainf = new File(subfolder, "main.json");
		String json = FileUtil.readAllText(mainf);
		if (json == null)
		{
			// Kind of inefficient, but it's easiest this way.
			json = BBJsonLoader.serializeObject(initialAssociations);
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
		for (String fileName : customBoosters.getIncludes())
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
		mainBoosters = BBJsonLoader.deserializeObject(mainJson, JsonBoosterHandler.class);
		for (JsonBooster j : mainBoosters.getBoosters())
		{
			j.register();
		}

		customBoosters = BBJsonLoader.deserializeObject(customJson, JsonBoosterHandler.class);
		for (JsonBooster j : customBoosters.getBoosters())
		{
			j.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonBoosterHandler handler = BBJsonLoader.deserializeObject(json, JsonBoosterHandler.class);

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

			for (JsonBooster r : handler.getBoosters())
			{
				r.register();
			}
		}
	}

	@Override
	public void savePostLoad(File subfolder)
	{
		String json = BBJsonLoader.serializeObject(mainBoosters);
		File mainf = new File(subfolder, "main.json");
		FileUtil.overwriteAllText(mainf, json);

		json = BBJsonLoader.serializeObject(customBoosters);
		File customf = new File(subfolder, "custom.json");
		FileUtil.overwriteAllText(customf, json);
	}

	public JsonBoosterHandler getMainBoosters()
	{
		return mainBoosters;
	}

	public JsonBoosterHandler getCustomBoosters()
	{
		return customBoosters;
	}
}
