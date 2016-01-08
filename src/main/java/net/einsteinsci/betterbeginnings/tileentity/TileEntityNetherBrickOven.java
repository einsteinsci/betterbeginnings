package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockNetherBrickOven;
import net.einsteinsci.betterbeginnings.inventory.ContainerNetherBrickOven;
import net.einsteinsci.betterbeginnings.inventory.TankNetherBrickOvenFuel;
import net.einsteinsci.betterbeginnings.network.PacketNetherBrickOvenFuelLevel;
import net.einsteinsci.betterbeginnings.register.recipe.NetherBrickOvenRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by einsteinsci on 8/21/2014.
 */
public class TileEntityNetherBrickOven extends TileEntitySpecializedFurnace implements IFluidHandler, IInteractionObject
{
	public static final int FUELINPUT = 0;
	public static final int OUTPUT = 1;
	public static final int INPUTSTART = 2;
	
	/**
	 * Fuel used in mb per operation *
	 */
	public static final float FUELFORLAVA = 8; // 512 mb per stack
	public static final int MINIMUMTEMPERATURE = 500;
	public TankNetherBrickOvenFuel fuelTank;
	private int[] slotsInput = new int[] {FUELINPUT};
	private int[] slotsOutput = new int[] {OUTPUT};

	public TileEntityNetherBrickOven()
	{
		super(11);
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

		/*
		if (resource.isFluidEqual(fuelTank.getFluid()))
		{
			return fuelTank.drain(resource.amount, doDrain);
		}
		else
		{
			return fuelTank.drain(0, doDrain);
		}
		*/
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
		//return fuelTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		return true;

		/*
		if (fuelTank.getFluid() == null)
		{
			return true;
		}

		return fuelTank.getFluidAmount() < fuelTank.getCapacity() && fuelTank.getFluid().getFluid() == fluid;
		*/
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid)
	{
		return false;

		/*
		if (fuelTank.getFluid() == null)
		{
			return false;
		}

		return fuelTank.getFluidAmount() > 0 && fuelTank.getFluid().getFluid() == fluid;
		*/
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
		boolean flag = canSmelt();
		boolean flag1 = false;

		if (!worldObj.isRemote)
		{
			if (canSmelt())
			{
				++cookTime;
				if (cookTime == processTime)
				{
					cookTime = 0;
					smeltItem();
					flag1 = true;
				}
			}
			else
			{
				cookTime = 0;
			}
			BlockNetherBrickOven.updateBlockState(canSmelt(), worldObj, pos);

			if (specialFurnaceStacks[FUELINPUT] != null)
			{
				if (fuelTank.fillFromContainer(specialFurnaceStacks[FUELINPUT]))
				{
					specialFurnaceStacks[FUELINPUT] = specialFurnaceStacks[FUELINPUT].getItem().getContainerItem(specialFurnaceStacks[FUELINPUT]);

					NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
							worldObj.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 16.0d);
					ModMain.network.sendToAllAround(new PacketNetherBrickOvenFuelLevel(
							pos, fuelTank.getFluid()), point);
				}
			}
		}

		if (flag != canSmelt())
		{
			flag1 = true;
		}

		if (flag1)
		{
			markDirty();
		}
	}

	public boolean canSmelt()
	{
		if (fuelTank.getFluidAmount() <= 0 || getFuelNeededForSmelt() > fuelTank.getFluidAmount())
		{
			return false;
		}

		boolean empty = true;
		for (int i = INPUTSTART; i < specialFurnaceStacks.length; ++i)
		{
			if (specialFurnaceStacks[i] != null)
			{
				empty = false;
				break;
			}
		}

		if (empty)
		{
			return false;
		}
		else
		{
			ItemStack stack = NetherBrickOvenRecipeHandler.instance().findMatchingRecipe(this);
			if (stack == null)
			{
				return false;
			}

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				return true;
			}
			if (!specialFurnaceStacks[OUTPUT].isItemEqual(stack))
			{
				return false;
			}

			int outputNewSize = specialFurnaceStacks[OUTPUT].stackSize + stack.stackSize;
			boolean result = outputNewSize <= getInventoryStackLimit() && outputNewSize <= specialFurnaceStacks[OUTPUT]
					.getMaxStackSize();
			return result;
		}
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = NetherBrickOvenRecipeHandler.instance().findMatchingRecipe(this);

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += itemStack.stackSize;
			}

			for (int i = INPUTSTART; i < specialFurnaceStacks.length; ++i)
			{
				ItemStack stack = specialFurnaceStacks[i];

				if (stack != null)
				{
					ItemStack containerItem = null;

					if (specialFurnaceStacks[i].getItem().hasContainerItem(specialFurnaceStacks[i]))
					{
						containerItem = specialFurnaceStacks[i].getItem().getContainerItem(specialFurnaceStacks[i]);
					}

					--specialFurnaceStacks[i].stackSize;

					if (specialFurnaceStacks[i].stackSize <= 0)
					{
						specialFurnaceStacks[i] = null;
					}

					if (containerItem != null)
					{
						specialFurnaceStacks[i] = containerItem;
					}
				}
			}

			fuelTank.getFluid().amount -= getFuelNeededForSmelt();
		}

		NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
				worldObj.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 16.0d);
		ModMain.network.sendToAllAround(new PacketNetherBrickOvenFuelLevel(pos, fuelTank.getFluid()), point);
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

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
		{
			return slotsOutput;
		}
		else
		{
			return slotsInput;
		}
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, EnumFacing side)
	{
		return isItemValidForSlot(par1, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
	{
		return side == EnumFacing.DOWN;
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
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

	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return getStackInSlot(INPUTSTART + row + column * 3);
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

	//I think this will only be called on the client side
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
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(pos) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq((double)pos.getX() + 0.5d, (double)pos.getY() + 0.5d,
			                            (double)pos.getZ() + 0.5d) <= 64.0d;
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == OUTPUT ? false : slot == FUELINPUT ? isItemFuelContainer(stack) : true;
	}
}
