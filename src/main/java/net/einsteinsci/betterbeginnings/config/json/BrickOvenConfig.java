package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BrickOvenConfig implements IJsonConfig
{
	public static final BrickOvenConfig INSTANCE = new BrickOvenConfig();

	private JsonBrickOvenRecipeHandler mainRecipes = new JsonBrickOvenRecipeHandler();
	private List<JsonBrickOvenRecipeHandler> includes = new ArrayList<>();

	@Override
	public String getSubFolder()
	{
		return "brickoven";
	}

	@Override
	public String getMainJson(File subfolder)
	{
		File mainf = new File(subfolder, "main.json");
		if (!mainf.exists())
		{
			return "{}";
		}

		try
		{
			return new String(Files.readAllBytes(mainf.toPath()));
		}
		catch (IOException e)
		{
			LogUtil.log(Level.ERROR, "IOException occurred opening config/betterbeginnings/brickoven/main.json!");
			LogUtil.log("");
			LogUtil.log(Level.ERROR, e.toString());

			return "{}";
		}
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
			if (!incf.exists())
			{
				LogUtil.log(Level.ERROR, "Included file not found: config/betterbeginnings/brickoven/" +
					fileName + " - Skipping.");
				continue;
			}

			try
			{
				res.add(new String(Files.readAllBytes(incf.toPath())));
			}
			catch (IOException ex)
			{
				LogUtil.log(Level.ERROR, "IOException occurred opening config/betterbeginnings/brickoven/" + fileName);
				LogUtil.log("");
				LogUtil.log(Level.ERROR, ex.toString());
			}
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
		mainRecipes = BBJsonLoader.deserializeObject(mainJson, JsonBrickOvenRecipeHandler.class);

		for (JsonBrickOvenShapedRecipe j : mainRecipes.getShaped())
		{
			j.register();
		}

		for (JsonBrickOvenShapelessRecipe j : mainRecipes.getShapeless())
		{
			j.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonBrickOvenRecipeHandler handler = BBJsonLoader.deserializeObject(json, JsonBrickOvenRecipeHandler.class);
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
		try
		{
			File mainf = new File(subfolder, "main.json");
			Files.write(mainf.toPath(), json.getBytes(), StandardOpenOption.CREATE);
		}
		catch (IOException e)
		{
			LogUtil.log(Level.ERROR, "IOException occurred saving config/betterbeginnings/brickoven/main.json!");
			LogUtil.log("");
			LogUtil.log(Level.ERROR, e.toString());
		}
	}

	public JsonBrickOvenRecipeHandler getMainRecipes()
	{
		return mainRecipes;
	}
}
