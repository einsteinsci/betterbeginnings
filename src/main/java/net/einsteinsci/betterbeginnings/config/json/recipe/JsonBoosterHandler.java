package net.einsteinsci.betterbeginnings.config.json.recipe;

import java.util.ArrayList;
import java.util.List;

public class JsonBoosterHandler
{
	private List<JsonBooster> boosters = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();

	public List<JsonBooster> getBoosters()
	{
		return boosters;
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
