package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.config.json.recipe.JsonRepairInfusionAssociation;
import net.einsteinsci.betterbeginnings.config.json.recipe.JsonRepairInfusionHandler;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.einsteinsci.betterbeginnings.util.FileUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RepairInfusionConfig implements IJsonConfig
{
	public static final RepairInfusionConfig INSTANCE = new RepairInfusionConfig();

	private static JsonRepairInfusionHandler initialAssociations = new JsonRepairInfusionHandler();

	private JsonRepairInfusionHandler mainAssociations = new JsonRepairInfusionHandler();
	private JsonRepairInfusionHandler customAssociations = new JsonRepairInfusionHandler();

	private List<JsonRepairInfusionHandler> includes = new ArrayList<>();

	public static void registerEnchantment(int enchID, OreRecipeElement associatedItem)
	{
		initialAssociations.getEnchantmentAssociations().add(new JsonRepairInfusionAssociation(enchID, associatedItem));
	}

	@Override
	public String getSubFolder()
	{
		return "RepairInfusion";
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
		for (String fileName : customAssociations.getIncludes())
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
		mainAssociations = BBJsonLoader.deserializeObject(mainJson, JsonRepairInfusionHandler.class);
		for (JsonRepairInfusionAssociation j : mainAssociations.getEnchantmentAssociations())
		{
			j.register();
		}

		customAssociations = BBJsonLoader.deserializeObject(customJson, JsonRepairInfusionHandler.class);
		for (JsonRepairInfusionAssociation r : customAssociations.getEnchantmentAssociations())
		{
			r.register();
		}
	}

	@Override
	public void loadIncludedConfig(FMLInitializationEvent e, List<String> includedJsons)
	{
		for (String json : includedJsons)
		{
			JsonRepairInfusionHandler handler = BBJsonLoader.deserializeObject(json, JsonRepairInfusionHandler.class);

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

			for (JsonRepairInfusionAssociation r : handler.getEnchantmentAssociations())
			{
				r.register();
			}
		}
	}

	@Override
	public void savePostLoad(File subfolder)
	{
		String json = BBJsonLoader.serializeObject(mainAssociations);
		File mainf = new File(subfolder, "main.json");
		FileUtil.overwriteAllText(mainf, json);

		json = BBJsonLoader.serializeObject(customAssociations);
		File customf = new File(subfolder, "custom.json");
		FileUtil.overwriteAllText(customf, json);
	}

	@Override
	public void saveAutoJson(File subfolder)
	{ }

	public JsonRepairInfusionHandler getMainAssociations()
	{
		return mainAssociations;
	}

	public JsonRepairInfusionHandler getCustomAssociations()
	{
		return customAssociations;
	}
}
