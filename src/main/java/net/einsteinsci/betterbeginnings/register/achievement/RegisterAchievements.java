package net.einsteinsci.betterbeginnings.register.achievement;

import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;

import java.util.HashMap;
import java.util.Map;

public class RegisterAchievements
{
	public static final RegisterAchievements instance = new RegisterAchievements();

	public static Map<String, Achievement> achievements;
	
	public static RegisterAchievements getInstance()
	{
		return instance;
	}

	public static Achievement[] getAchievements()
	{
		addAchievements();

		return achievements.values().toArray(new Achievement[1]);
	}

	public static void addAchievements()
	{
		achievements = new HashMap<String, Achievement>();

		achievements.put("flintKnife", new Achievement("achievement.flintKnife", "flintKnife", 0, 0,
		                                               RegisterItems.flintKnife, null).initIndependentStat()
				.registerStat());
		achievements.put("makeSticks", new Achievement("achievement.makeSticks", "makeSticks", 2, 1, Items.stick,
		                                               get("flintKnife")).registerStat());
	}

	public static Achievement get(String key)
	{
		return achievements.get(key);
	}
}
