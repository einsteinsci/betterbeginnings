package net.einsteinsci.betterbeginnings.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockUtil
{
	public static final int Bottom = 0;
	public static final int Top = 1;
	public static final int North = 2;
	public static final int South = 3;
	public static final int West = 4;
	public static final int East = 5;

	public static Block getBlockFromDirection(World world, int x, int y, int z, int direction)
	{
		switch (direction)
		{
			case Bottom:
				return world.getBlock(x, y - 1, z);
			case Top:
				return world.getBlock(x, y + 1, z);
			case North:
				return world.getBlock(x, y, z - 1);
			case South:
				return world.getBlock(x, y, z + 1);
			case West:
				return world.getBlock(x - 1, y, z);
			case East:
				return world.getBlock(x + 1, y, z);

			default:
				return null;
		}
	}
}
