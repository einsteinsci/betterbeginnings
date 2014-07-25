package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.blocks.BlockBrickOven;
import net.einsteinsci.noobcraft.blocks.BlockDoubleWorkbench;
import net.einsteinsci.noobcraft.blocks.BlockKiln;
import net.einsteinsci.noobcraft.blocks.BlockSmelter;
import net.minecraft.block.Block;

public class RegisterBlocks
{
	public static final Block blockKiln = new BlockKiln(false);
	public static final Block blockKilnLit = new BlockKiln(true);
	public static final Block blockDoubleWorkbench = new BlockDoubleWorkbench();
	public static final Block blockBrickOven = new BlockBrickOven(false);
	public static final Block blockBrickOvenLit = new BlockBrickOven(true);
	public static final Block smelter = new BlockSmelter(false);
	public static final Block smelterLit = new BlockSmelter(true);
	
	public static void register()
	{
		RegisterHelper.registerBlock(blockKiln);
		RegisterHelper.registerBlock(blockKilnLit);
		
		RegisterHelper.registerBlock(blockDoubleWorkbench);
		
		RegisterHelper.registerBlock(blockBrickOven);
		RegisterHelper.registerBlock(blockBrickOvenLit);
		
		RegisterHelper.registerBlock(smelter);
		RegisterHelper.registerBlock(smelterLit);
	}
}
