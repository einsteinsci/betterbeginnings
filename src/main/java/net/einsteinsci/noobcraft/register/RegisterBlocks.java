package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.blocks.*;
import net.minecraft.block.Block;

public class RegisterBlocks 
{
	public static final Block blockKiln = new BlockKiln(false);
	public static final Block blockKilnLit = new BlockKiln(true);
	
	public static void register()
	{
		RegisterHelper.registerBlock(blockKiln);
		RegisterHelper.registerBlock(blockKilnLit);
	}
}
