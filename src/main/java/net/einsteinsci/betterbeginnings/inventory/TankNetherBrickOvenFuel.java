package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

/**
 * Created by einsteinsci on 8/21/2014.
 */
public class TankNetherBrickOvenFuel extends FluidTank implements IFluidTank
{
	public TankNetherBrickOvenFuel(TileEntityNetherBrickOven oven, int capacity)
	{
		super(capacity);

		tile = oven;
	}
}
