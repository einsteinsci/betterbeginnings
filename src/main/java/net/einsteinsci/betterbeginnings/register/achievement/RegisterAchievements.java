package net.einsteinsci.betterbeginnings.register.achievement;

import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import java.util.HashMap;
import java.util.Map;

public class RegisterAchievements
{
	public static final RegisterAchievements instance = new RegisterAchievements();

	private static Map<String, Achievement> achievements;
	
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
		achievements.put("makeString", new Achievement("achievement.makeString", "makeString", -2, -1,
		                                               Items.string, get("flintKnife")).registerStat());
		achievements.put("makeTwine", new Achievement("achievement.makeTwine", "makeTwine", 0, -2, RegisterItems.twine,
		                                              get("flintKnife")).registerStat());
		achievements.put("upgradeKnife", new Achievement("achievement.upgradeKnife", "upgradeKnife", 3, -1,
		                                                 RegisterItems.boneKnife, get("flintKnife")).registerStat());
		achievements.put("makeSticks", new Achievement("achievement.makeSticks", "makeSticks", -2, 1, Items.stick,
		                                               get("flintKnife")).registerStat());
		achievements.put("makeHatchet", new Achievement("achievement.makeHatchet", "makeHatchet", -1, 3,
		                                                RegisterItems.flintHatchet, get("makeSticks")).registerStat());
		achievements.put("doubleWorkbench", new Achievement("achievement.doubleWorkbench", "doubleWorkbench", -3, 4,
		                                                    RegisterBlocks.doubleWorkbench, get("makeHatchet"))
				.registerStat());
		achievements.put("makeSword", new Achievement("achievement.makeSword", "makeSword", -4, 6, Items.stone_sword,
		                                              get("doubleWorkbench")).registerStat());
		achievements.put("infusionRepair", new Achievement("achievement.infusionRepair", "infusionRepair", -2, 7,
		                                                   RegisterBlocks.infusionRepairStation, get("doubleWorkbench"))
				.registerStat());
		achievements.put("repairNoobSword", new Achievement("achievement.repairNoobSword", "repairNoobSword", -1, 9,
		                                                    RegisterItems.noobWoodSword, get("infusionRepair"))
				.registerStat());
		achievements.put("boneShards", new Achievement("achievement.boneShards", "boneShards", 3, 2,
		                                               RegisterItems.boneShard, get("flintKnife")).registerStat());
		achievements
				.put("bonePick", new Achievement("achievement.bonePick", "bonePick", 4, 4, RegisterItems.bonePickaxe,
				                                 get("boneShards")).registerStat());
		achievements.put("makeKiln", new Achievement("achievement.makeKiln", "makeKiln", 5, 6, RegisterBlocks.kiln,
		                                             get("bonePick")).registerStat());
		achievements.put("charredMeat",
		                 new Achievement("achievement.charredMeat", "charredMeat", 4, 8, RegisterItems.charredMeat,
		                                 get("makeKiln")).registerStat());
		achievements.put("obsidianKiln", new Achievement("achievement.obsidianKiln", "obsidianKiln", 6, 9,
		                                                 RegisterBlocks.obsidianKiln, get("makeKiln")).registerStat());
		achievements.put("makeSmelter", new Achievement("achievement.makeSmelter", "makeSmelter", 2, 6,
		                                                RegisterBlocks.smelter, get("makeKiln")).registerStat());
		achievements.put("enderSmelter", new Achievement("achievement.enderSmelter", "enderSmelter", 1, 8,
		                                                 RegisterBlocks.enderSmelter, get("makeSmelter"))
				.registerStat());
		//achievements.put("oreDoubling", new Achievement("achievement.oreDoubling", "oreDoubling", 5, 6,
		//                                                Items.gold_ingot, get("enderSmelter")).registerStat());
		achievements.put("makeBrickOven", new Achievement("achievement.makeBrickOven", "makeBrickOven", 8, 5,
		                                                  RegisterBlocks.brickOven, get("makeKiln")).registerStat());
		achievements.put("cake", new Achievement("achievement.cake", "cake", 7, 3, Items.cake, get("makeBrickOven"))
				.registerStat());
		achievements.put("netherBrickOven", new Achievement("achievement.netherBrickOven", "netherBrickOven", 9, 8,
		                                                    RegisterBlocks.netherBrickOven, get("makeBrickOven"))
				.registerStat());
		achievements.put("notchApple", new Achievement("achievement.notchApple", "notchApple", 11, 9,
		                                               new ItemStack(Items.golden_apple, 1, 0), get("netherBrickOven"))
				.registerStat());
	}

	public static Achievement get(String key)
	{
		return achievements.get(key);
	}

	public static void achievementGet(EntityPlayer player, String achievementKey)
	{
		player.addStat(RegisterAchievements.get(achievementKey), 1);
	}
}
