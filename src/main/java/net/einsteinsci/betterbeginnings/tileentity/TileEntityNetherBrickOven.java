package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockNetherBrickOven;
import net.einsteinsci.betterbeginnings.inventory.ContainerNetherBrickOven;
import net.einsteinsci.betterbeginnings.inventory.TankNetherBrickOvenFuel;
import net.einsteinsci.betterbeginnings.network.PacketNetherBrickOvenFuelLevel;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.NetherBrickOvenRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityNetherBrickOven extends TileEntityBrickOvenBase implements IFluidHandler, IInteractionObject
{
	/**
	 * Fuel used in mb per operation *
	 */
	public static final float FUELFORLAVA = 8; // 512 mb per stack
	public static final int MINIMUMTEMPERATURE = 500;

	public TankNetherBrickOvenFuel fuelTank;

	public TileEntityNetherBrickOven()
	{
		super();
		fuelTank = new TankNetherBrickOvenFuel(this, 8000);
		processTime = 80;
	}

	public FluidStack getFuelStack()
	{
		return fuelTank.getFluid();
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		int result = fuelTank.fill(resource, doFill);
		markDirty();
		worldObj.markBlockForUpdate(pos);
		return result;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
	{
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		fuelTank = (TankNetherBrickOvenFuel)fuelTank.readFromNBT(tagCompound);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		return true;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid)
	{
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		fuelTank.writeToNBT(tagCompound);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from)
	{
		return new FluidTankInfo[] {new FluidTankInfo(fuelTank.getFluid(), fuelTank.getCapacity())};
	}
	
	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.netherbrickoven";
	}

	@Override
	public void update()
	{
		boolean couldSmelt = canSmelt();
		boolean dirty = false;

		if (!worldObj.isRemote)
		{
			if (canSmelt())
			{
				++cookTime;
				if (cookTime == processTime)
				{
					cookTime = 0;
					smeltItem();
					dirty = true;
				}
			}
			else
			{
				cookTime = 0;
			}
			updateBlockState();

			if (specialFurnaceStacks[FUEL] != null)
			{
				if (fuelTank.fillFromContainer(specialFurnaceStacks[FUEL]))
				{
					specialFurnaceStacks[FUEL] = specialFurnaceStacks[FUEL].getItem().getContainerItem(specialFurnaceStacks[FUEL]);

					updateNetwork();
				}
			}
		}

		if (couldSmelt != canSmelt())
		{
			dirty = true;
		}

		if (dirty)
		{
			markDirty();
		}
	}

	@Override
	public void updateNetwork()
	{
		NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
			worldObj.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 16.0d);
		ModMain.network.sendToAllAround(new PacketNetherBrickOvenFuelLevel(
			pos, fuelTank.getFluid()), point);
	}

	@Override
	public void updateBlockState()
	{
		BlockNetherBrickOven.updateBlockState(canSmelt(), worldObj, pos);
	}

	@Override
	public boolean canSmelt()
	{
		if (fuelTank.getFluidAmount() <= 0 || getFuelNeededForSmelt() > fuelTank.getFluidAmount())
		{
			return false;
		}

		return super.canSmelt();
	}

	@Override
	public void smeltItem()
	{
		if (canSmelt())
		{
			fuelTank.getFluid().amount -= getFuelNeededForSmelt();
		}

		super.smeltItem();

		updateNetwork();
	}

	@Override
	public ItemStack findMatchingRecipe()
	{
		return NetherBrickOvenRecipeHandler.instance().findMatchingRecipe(this);
	}

	public int getFuelNeededForSmelt()
	{
		if (fuelTank.getFluid() == null)
		{
			return 0;
		}

		float temperature = (float)fuelTank.getFluid().getFluid().getTemperature();

		// Math!
		float precise = FUELFORLAVA * FluidRegistry.LAVA.getTemperature() / temperature;

		int result = (int)precise;

		if (result <= 0)
		{
			result = 1; // No free smelting if you have blazing pyrotheum or something even hotter.
		}

		return result;
	}

	public boolean isItemFuelContainer(ItemStack stack)
	{
		if (FluidContainerRegistry.isContainer(stack))
		{
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);

			if (fluid != null)
			{
				return fluid.getFluid().getTemperature() > MINIMUMTEMPERATURE;
			}
		}

		return false;
	}

	public int getFuelLevelScaled(int maxLevel)
	{
		float levelAbs = getFuelLevel();
		float capacity = fuelTank.getCapacity();
		float level = levelAbs / capacity;
		float scaled = level * (float)maxLevel;

		return (int)scaled;
	}

	public int getFuelLevel()
	{
		return fuelTank.getFluidAmount();
	}

	// I think this will only be called on the client side
	public void setFuelLevel(FluidStack fluid)
	{
		fuelTank.setFluid(fluid);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerNetherBrickOven(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":netherBrickOven";
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == OUTPUT ? false : slot == FUEL ? isItemFuelContainer(stack) : true;
	}
}
