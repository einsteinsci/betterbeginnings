package net.einsteinsci.betterbeginnings.config.json.recipe;

import java.util.ArrayList;
import java.util.List;

public class JsonRepairInfusionHandler
{
	private List<JsonRepairInfusionAssociation> enchantmentAssociations = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();

	public List<JsonRepairInfusionAssociation> getEnchantmentAssociations()
	{
		return enchantmentAssociations;
	}

	public List<String> getIncludes()
	{
		return includes;
	}

	public List<String> getModDependencies()
	{
		return modDependencies;
	}
}
