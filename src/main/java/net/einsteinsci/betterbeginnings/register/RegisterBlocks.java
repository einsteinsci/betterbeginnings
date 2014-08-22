package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.blocks.*;
import net.minecraft.block.Block;

public class RegisterBlocks
{
	public static final Block kiln = new BlockKiln(false);
	public static final Block kilnLit = new BlockKiln(true);
	public static final Block doubleWorkbench = new BlockDoubleWorkbench();
	public static final Block brickOven = new BlockBrickOven(false);
	public static final Block brickOvenLit = new BlockBrickOven(true);
	public static final Block smelter = new BlockSmelter(false);
	public static final Block smelterLit = new BlockSmelter(true);
	public static final Block infusionRepairStation = new BlockInfusionRepairStation();
	public static final Block campfire = new BlockCampfire(false);
	public static final Block campfireLit = new BlockCampfire(true);
	public static final Block obsidianKiln = new BlockObsidianKiln(false);
	public static final Block obsidianKilnLit = new BlockObsidianKiln(true);
	public static final Block netherBrickOven = new BlockNetherBrickOven(false);
	public static final Block netherBrickOvenLit = new BlockNetherBrickOven(true);
	public static final Block enderSmelter = new BlockEnderSmelter(false);
	public static final Block enderSmelterLit = new BlockEnderSmelter(true);

	public static void register()
	{
		RegisterHelper.registerBlock(kiln);
		RegisterHelper.registerBlock(kilnLit);

		RegisterHelper.registerBlock(doubleWorkbench);

		RegisterHelper.registerBlock(brickOven);
		RegisterHelper.registerBlock(brickOvenLit);

		RegisterHelper.registerBlock(smelter);
		RegisterHelper.registerBlock(smelterLit);

		RegisterHelper.registerBlock(infusionRepairStation);

		RegisterHelper.registerBlock(campfire);
		RegisterHelper.registerBlock(campfireLit);

		RegisterHelper.registerBlock(obsidianKiln);
		RegisterHelper.registerBlock(obsidianKilnLit);

		RegisterHelper.registerBlock(netherBrickOven);
		RegisterHelper.registerBlock(netherBrickOvenLit);

		RegisterHelper.registerBlock(enderSmelter);
		RegisterHelper.registerBlock(enderSmelterLit);
	}
}
