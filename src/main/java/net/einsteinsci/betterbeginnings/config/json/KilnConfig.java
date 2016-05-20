package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KilnConfig implements IJsonConfig
{
	public static final KilnConfig INSTANCE = new KilnConfig();

	private JsonKilnRecipeHandler mainRecipes = new JsonKilnRecipeHandler();

	private List<JsonKilnRecipeHandler> includes = new ArrayList<>();

	@Override
	public String getSubFolder()
	{
		return "kiln";
	}

	@Override
	public String getMainJson(File subfolder)
	{
		File mainf = new File(subfolder, "main.json");
		String json = FileUtil.readAllText(mainf);
		if (json == null)
		{
			json = "{}";
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
		return "{}";
	}

	@Override
	public List<String> getIncludedJson(File subfolder)
	{
		List<String> res = new ArrayList<>();
		for (String fileName : mainRecipes.getIncludes())
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
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonKilnRecipeHandler.class);

		for (JsonKilnRecipe j : mainRecipes.getRecipes())
		{
			j.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonKilnRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonKilnRecipeHandler.class);
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
	}

	public JsonKilnRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}
}
