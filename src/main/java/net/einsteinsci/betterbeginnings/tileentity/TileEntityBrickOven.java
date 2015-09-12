package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockBrickOven;
import net.einsteinsci.betterbeginnings.inventory.ContainerBrickOven;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;

public class TileEntityBrickOven extends TileEntitySpecializedFurnace implements IInteractionObject
{
	public static final int FUEL = 0;
	public static final int OUTPUT = 1;
	public static final int INPUTSTART = 2;
	private static final int[] slotsInput = new int[] {FUEL};
	private static final int[] slotsOutput = new int[] {OUTPUT};

	public TileEntityBrickOven()
	{
		super(11);
		processTime = 120;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[1]);
	}

	public static int getItemBurnTime(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}
		else
		{
			Item item = itemStack.getItem();
			ItemStack clone = itemStack.copy();

			if (item instanceof ItemCoal)
			{
				clone.setItemDamage(0);
			}

			return TileEntityKiln.getItemBurnTime(itemStack);
		}
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
		return slot == OUTPUT ? false : slot == FUEL ? isItemFuel(stack) : true;
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.brickoven";
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			boolean flag = burnTime > 0;
			boolean flag1 = false;

			if (burnTime > 0)
			{
				--burnTime;
			}

			if (burnTime == 0 && canSmelt())
			{
				currentItemBurnLength = burnTime = getItemBurnTime(specialFurnaceStacks[FUEL]);

				if (burnTime > 0)
				{
					flag1 = true;
					if (specialFurnaceStacks[FUEL] != null)
					{
						--specialFurnaceStacks[FUEL].stackSize;

						if (specialFurnaceStacks[FUEL].stackSize == 0)
						{
							specialFurnaceStacks[FUEL] = specialFurnaceStacks[FUEL].getItem().getContainerItem(specialFurnaceStacks[FUEL]);
						}
					}
				}
			}

			if (isBurning() && canSmelt())
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

			if (flag != burnTime > 0)
			{
				flag1 = true;
				BlockBrickOven.updateBlockState(burnTime > 0, worldObj, pos);
			}

			if (flag1)
			{
				markDirty();
			}
		}
	}

	private boolean canSmelt()
	{
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
			ItemStack stack = BrickOvenRecipeHandler.instance().findMatchingRecipe(this);
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

			int result = specialFurnaceStacks[OUTPUT].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= specialFurnaceStacks[OUTPUT].getMaxStackSize();
		}
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = BrickOvenRecipeHandler.instance().findMatchingRecipe(this);

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
		}
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
	
	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
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

	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return getStackInSlot(INPUTSTART + row + column * 3);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerBrickOven(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":brickOven";
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, 1, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
	}
}