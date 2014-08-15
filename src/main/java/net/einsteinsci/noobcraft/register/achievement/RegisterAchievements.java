package net.einsteinsci.noobcraft.register.achievement;

import net.einsteinsci.noobcraft.register.RegisterBlocks;
import net.einsteinsci.noobcraft.register.RegisterItems;
import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;

public class RegisterAchievements
{
	public static final RegisterAchievements instance = new RegisterAchievements();
	
	public static Achievement flintKnife;
	public static Achievement makeSticks;
	public static Achievement startFire;
	
	public static RegisterAchievements getInstance()
	{
		return instance;
	}
	
	public static void addAchievements()
	{
		flintKnife =
			new Achievement("achievement.flintKnife", "flintKnife", 0, 0, RegisterItems.flintKnife, (Achievement)null)
				.initIndependentStat().registerStat();
		makeSticks =
			new Achievement("achievement.makeSticks", "makeSticks", 2, 1, Items.stick, flintKnife).registerStat();
		startFire = 
			new Achievement("achievement.startFire", "startFire", 4,2, RegisterBlocks.campfire, makeSticks).registerStat();
		
	}
	
	
	public static Achievement[] getAchievements()
	{
		addAchievements();
		
		Achievement[] result = new Achievement[] { flintKnife, makeSticks, startFire };
		
		return result;
	}
}
