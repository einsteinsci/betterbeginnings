package net.einsteinsci.betterbeginnings.tileentity;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockNetherBrickOven;
import net.einsteinsci.betterbeginnings.inventory.TankNetherBrickOvenFuel;
import net.einsteinsci.betterbeginnings.network.PacketNetherBrickOvenFuelLevel;
import net.einsteinsci.betterbeginnings.register.recipe.NetherBrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.util.BlockUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by einsteinsci on 8/21/2014.
 */
public class TileEntityNetherBrickOven extends TileEntity implements ISidedInventory, IFluidHandler
{
	public static final int FUELINPUT = 0;
	private int[] slotsInput = new int[] {FUELINPUT};
	public static final int OUTPUT = 1;
	private int[] slotsOutput = new int[] {OUTPUT};
	public static final int INPUTSTART = 2;
	public static final int COOKTIME = 80;

	/**
	 * Fuel used in mb per operation *
	 */
	public static final float FUELFORLAVA = 8; // 512 mb per stack
	public static final int MINIMUMTEMPERATURE = 500;
	public int ovenCookTime;
	public TankNetherBrickOvenFuel fuelTank;
	private ItemStack[] ovenStacks = new ItemStack[11];
	private String ovenName;

	public TileEntityNetherBrickOven()
	{
		super();
		fuelTank = new TankNetherBrickOvenFuel(this, 8000);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		ovenStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < ovenStacks.length)
			{
				ovenStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		// Cook Time
		ovenCookTime = tagCompound.getShort("CookTime");
		fuelTank = (TankNetherBrickOvenFuel)fuelTank.readFromNBT(tagCompound);

		if (tagCompound.hasKey("CustomName"))
		{
			ovenName = tagCompound.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setShort("CookTime", (short)ovenCookTime);
		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < ovenStacks.length; ++i)
		{
			if (ovenStacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				ovenStacks[i].writeToNBT(itemTag);
				itemTag.setByte("Slot", (byte)i);
				tagList.appendTag(itemTag);
			}
		}

		tagCompound.setTag("Items", tagList);

		fuelTank.writeToNBT(tagCompound);

		if (hasCustomInventoryName())
		{
			tagCompound.setString("CustomName", ovenName);
		}
	}

	@Override
	public void updateEntity()
	{
		boolean flag = canSmelt();
		boolean flag1 = false;

		if (!worldObj.isRemote)
		{
			if (canSmelt())
			{
				++ovenCookTime;
				if (ovenCookTime == COOKTIME)
				{
					ovenCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			}
			else
			{
				ovenCookTime = 0;
			}

			if (ovenStacks[FUELINPUT] != null)
			{
				if (fuelTank.fillFromContainer(ovenStacks[FUELINPUT]))
				{
					ovenStacks[FUELINPUT] = ovenStacks[FUELINPUT].getItem().getContainerItem(ovenStacks[FUELINPUT]);

					NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId,
					                                                                    xCoord, yCoord, zCoord, 16.0d);
					ModMain.network.sendToAllAround(
							new PacketNetherBrickOvenFuelLevel(xCoord, yCoord, zCoord, fuelTank.getFluid()), point);
				}
			}
		}

		if (flag != canSmelt())
		{
			flag1 = true;
			BlockNetherBrickOven.updateBlockState(canSmelt(), worldObj, xCoord, yCoord, zCoord);
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
		for (int i = INPUTSTART; i < ovenStacks.length; ++i)
		{
			if (ovenStacks[i] != null)
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

			if (ovenStacks[OUTPUT] == null)
			{
				return true;
			}
			if (!ovenStacks[OUTPUT].isItemEqual(stack))
			{
				return false;
			}

			int result = ovenStacks[OUTPUT].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= ovenStacks[OUTPUT].getMaxStackSize();
		}
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = NetherBrickOvenRecipeHandler.instance().findMatchingRecipe(this);

			if (ovenStacks[OUTPUT] == null)
			{
				ovenStacks[OUTPUT] = itemStack.copy();
			}
			else if (ovenStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				ovenStacks[OUTPUT].stackSize += itemStack.stackSize;
			}

			for (int i = INPUTSTART; i < ovenStacks.length; ++i)
			{
				ItemStack stack = ovenStacks[i];

				if (stack != null)
				{
					ItemStack containerItem = null;

					if (ovenStacks[i].getItem().hasContainerItem(ovenStacks[i]))
					{
						containerItem = ovenStacks[i].getItem().getContainerItem(ovenStacks[i]);
					}

					--ovenStacks[i].stackSize;

					if (ovenStacks[i].stackSize <= 0)
					{
						ovenStacks[i] = null;
					}

					if (containerItem != null)
					{
						ovenStacks[i] = containerItem;
					}
				}
			}

			fuelTank.getFluid().amount -= getFuelNeededForSmelt();
		}

		NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord,
		                                                                    yCoord, zCoord, 16.0d);
		ModMain.network.sendToAllAround(new PacketNetherBrickOvenFuelLevel(xCoord, yCoord, zCoord, fuelTank.getFluid()),
		                                point);
	}

	public int getFuelNeededForSmelt()
	{
		if (fuelTank.getFluid() == null)
		{
			return 0;
		}

		float temperature = (float)fuelTank.getFluid().getFluid().getTemperature();

		// Woohoo! Math!
		float precise = FUELFORLAVA * FluidRegistry.LAVA.getTemperature() / temperature;

		int result = (int)precise;

		if (result <= 0)
		{
			result = 1; // No free smelting if you have pyrotheum or something even hotter.
		}

		return result;
	}

	@Override
	public int getSizeInventory()
	{
		return ovenStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return ovenStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (ovenStacks[slot] != null)
		{
			ItemStack stack;
			if (ovenStacks[slot].stackSize <= amount)
			{
				stack = ovenStacks[slot];
				ovenStacks[slot] = null;
				return stack;
			}
			else
			{
				stack = ovenStacks[slot].splitStack(amount);

				if (ovenStacks[slot].stackSize == 0)
				{
					ovenStacks[slot] = null;
				}

				return stack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (ovenStacks[slot] != null)
		{
			ItemStack stack = ovenStacks[slot];
			ovenStacks[slot] = null;
			return stack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		ovenStacks[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? ovenName : "container.netherbrickoven";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return ovenName != null && ovenName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq(xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d) <= 64.0d;
		}
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == OUTPUT ? false : slot == FUELINPUT ? isItemFuelContainer(stack) : true;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		int result = fuelTank.fill(resource, doFill);
		markDirty();
		return result;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
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
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return null;
		//return fuelTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
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
	public boolean canDrain(ForgeDirection from, Fluid fluid)
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
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] {new FluidTankInfo(fuelTank.getFluid(), fuelTank.getCapacity())};
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		if (side == BlockUtil.Bottom)
		{
			return slotsOutput;
		}
		else
		{
			return slotsInput;
		}
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, int par3)
	{
		return isItemValidForSlot(par1, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		if (side == BlockUtil.Bottom)
		{
			return true;
		}
		return false;
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

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int progress)
	{
		return ovenCookTime * progress / COOKTIME;
	}

	public void furnaceName(String displayName)
	{
		ovenName = displayName;
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
}
