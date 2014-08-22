package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;

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

	public boolean fillFromContainer(ItemStack container)
	{
		FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(container);
		if (stack != null)
		{
			if (getCapacity() - getFluidAmount() > stack.amount)
			{
				if (getFluid() != null)
				{
					if (getFluid().isFluidEqual(stack))
					{
						fluid.amount += stack.amount;
						return true;
					}
				}
				else
				{
					fluid = stack.copy();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getFluidAmount()
	{
		if (fluid == null)
		{
			return 0;
		}
		return fluid.amount;
	}
}
