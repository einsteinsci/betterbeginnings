package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.blocks.*;
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
	public static final Block repairTable = new BlockRepairTable();
	public static final Block campfire = new BlockCampfire(false, false);
	public static final Block campfireLit = new BlockCampfire(true, false);
	public static final Block campfireKindling = new BlockCampfire(false, true);
	
	public static void register()
	{
		RegisterHelper.registerBlock(kiln);
		RegisterHelper.registerBlock(kilnLit);
		
		RegisterHelper.registerBlock(doubleWorkbench);
		
		RegisterHelper.registerBlock(brickOven);
		RegisterHelper.registerBlock(brickOvenLit);
		
		RegisterHelper.registerBlock(smelter);
		RegisterHelper.registerBlock(smelterLit);
		
		RegisterHelper.registerBlock(repairTable);
		
		//RegisterHelper.registerBlock(campfire);
		//RegisterHelper.registerBlock(campfireLit);
		//RegisterHelper.registerBlock(campfireKindling);
	}
}
