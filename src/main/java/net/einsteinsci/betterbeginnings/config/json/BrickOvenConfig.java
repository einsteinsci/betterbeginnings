package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class BrickOvenConfig implements IJsonConfig
{
	public static final BrickOvenConfig INSTANCE = new BrickOvenConfig();

	private JsonBrickOvenRecipeHandler mainRecipes = new JsonBrickOvenRecipeHandler();

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
