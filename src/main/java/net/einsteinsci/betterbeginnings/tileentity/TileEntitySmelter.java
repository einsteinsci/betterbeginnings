package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.blocks.BlockSmelter;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class TileEntitySmelter extends TileEntitySpecializedFurnace implements IInteractionObject
{
	public static final int INPUT = 0;
	public static final int FUEL = 1;
	public static final int OUTPUT = 2;
	public static final int GRAVEL = 3;
	private static final int[] slotsTop = new int[] {GRAVEL, INPUT};
	private static final int[] slotsBottom = new int[] {OUTPUT};
	private static final int[] slotsSides = new int[] {FUEL, GRAVEL, INPUT};

	public TileEntitySmelter()
	{
		super(4);
		processTime = 160;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[FUEL]);
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

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
			{
				Block block = Block.getBlockFromItem(item);
			}

			// Charcoal
			if (item == Items.coal && itemStack.getItemDamage() == 1)
			{
				return 1600;
			}

			// Blaze Rods and Lava are valid fuel sources for a Smelter.
			if (item == Items.blaze_rod)
			{
				return 600;
			}
			if (item == Items.lava_bucket)
			{
				return 7200;
			}

			return GameRegistry.getFuelValue(itemStack);
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
		if (stack == null || slot == OUTPUT)
		{
			return false;
		}

		if (slot == GRAVEL && stack.getItem() == Item.getItemFromBlock(Blocks.gravel))
		{
			return true;
		}

		if (slot == FUEL && getItemBurnTime(stack) > 0)
		{
			return true;
		}

		return slot == INPUT;
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.smelter";
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
				BlockSmelter.updateBlockState(burnTime > 0, worldObj, pos);
			}

			if (flag1)
			{
				markDirty();
			}
		}
	}

	private boolean canSmelt()
	{
		if (specialFurnaceStacks[INPUT] == null || specialFurnaceStacks[GRAVEL] == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = SmelterRecipeHandler.smelting().getSmeltingResult(specialFurnaceStacks[INPUT]);
			int gravelNeeded = SmelterRecipeHandler.smelting().getGravelCount(specialFurnaceStacks[INPUT]);

			if (stack == null)
			{
				return false;
			}

			if (gravelNeeded > specialFurnaceStacks[GRAVEL].stackSize)
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
			ItemStack itemStack = SmelterRecipeHandler.smelting().getSmeltingResult(specialFurnaceStacks[INPUT]);

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += itemStack.stackSize;
			}

			int gravelUsed = SmelterRecipeHandler.smelting().getGravelCount(specialFurnaceStacks[INPUT]);

			--specialFurnaceStacks[INPUT].stackSize;

			if (specialFurnaceStacks[INPUT].stackSize <= 0)
			{
				specialFurnaceStacks[INPUT] = null;
			}
			specialFurnaceStacks[GRAVEL].stackSize -= gravelUsed;

			if (specialFurnaceStacks[GRAVEL].stackSize <= 0)
			{
				specialFurnaceStacks[GRAVEL] = null;
			}
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? slotsBottom : side == EnumFacing.UP ? slotsTop : slotsSides;
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, EnumFacing par3)
	{
		return isItemValidForSlot(par1, stack);
	}

	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack stack, EnumFacing par3)
	{
		return par3 != EnumFacing.DOWN || par1 != 1 || stack.getItem() == Items.bucket;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return null;
	}

	@Override
	public String getGuiID()
	{
		return null;
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
