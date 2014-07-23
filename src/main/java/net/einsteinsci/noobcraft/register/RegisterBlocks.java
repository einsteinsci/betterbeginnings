package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.blocks.BlockDoubleWorkbench;
import net.einsteinsci.noobcraft.blocks.BlockKiln;
import net.minecraft.block.Block;

public class RegisterBlocks
{
	public static final Block blockKiln = new BlockKiln(false);
	public static final Block blockKilnLit = new BlockKiln(true);
	public static final Block blockDoubleWorkbench = new BlockDoubleWorkbench();
	
	public static void register()
	{
		RegisterHelper.registerBlock(blockKiln);
		RegisterHelper.registerBlock(blockKilnLit);
		
		RegisterHelper.registerBlock(blockDoubleWorkbench);
	}
}
