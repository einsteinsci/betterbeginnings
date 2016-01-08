package net.einsteinsci.betterbeginnings.tileentity;

import java.util.Random;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockEnderSmelter;
import net.einsteinsci.betterbeginnings.inventory.ContainerEnderSmelter;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityEnderSmelter extends TileEntitySpecializedFurnace implements IInteractionObject
{
	public static final int INPUT = 0;
	public static final int FUEL = 1;
	public static final int OUTPUT = 2;
	public static final int GRAVEL = 3;
	public static final Random random = new Random();
	private static final int[] slotsTop = new int[] {GRAVEL, INPUT};
	private static final int[] slotsBottom = new int[] {OUTPUT};
	private static final int[] slotsSides = new int[] {FUEL, GRAVEL, INPUT};
	public boolean oreDoubled = false;

	public TileEntityEnderSmelter()
	{
		super(4);
		processTime = 140;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[FUEL]);
		oreDoubled = tagCompound.getByte("oreDoubled") == 1;
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setByte("oreDoubled", oreDoubled ? (byte)1 : (byte)0);
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

			// Ender eyes and pearls are great fuel sources for the ender furnace.
			if (item == Items.ender_pearl)
			{
				return 1200;
			}
			if (item == Items.ender_eye)
			{
				return 2400;
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
		return hasCustomName() ? customName : "container.enderSmelter";
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
				BlockEnderSmelter.updateBlockState(burnTime > 0, worldObj, pos);
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
			ItemStack output = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);
			int gravelNeeded = SmelterRecipeHandler.instance().getGravelCount(specialFurnaceStacks[INPUT]);
			int bonus = SmelterRecipeHandler.instance().getBonus(specialFurnaceStacks[INPUT]);

			if (output == null)
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
			if (!specialFurnaceStacks[OUTPUT].isItemEqual(output))
			{
				return false;
			}

			int result = specialFurnaceStacks[OUTPUT].stackSize + output.stackSize + bonus;
			return result <= getInventoryStackLimit() && result <= specialFurnaceStacks[OUTPUT].getMaxStackSize();
		}
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);

			int bonus = SmelterRecipeHandler.instance().getBonus(specialFurnaceStacks[INPUT]);
			float chance = SmelterRecipeHandler.instance().getBonusChance(specialFurnaceStacks[INPUT]);
			int resultSize = itemStack.stackSize;
			if (random.nextFloat() < chance)
			{
				resultSize += bonus;
				oreDoubled = true;
			}

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += resultSize;
			}

			int gravelUsed = SmelterRecipeHandler.instance().getGravelCount(specialFurnaceStacks[INPUT]);

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
		if (side == EnumFacing.DOWN)
		{
			return slotsBottom;
		}
		else if (side == EnumFacing.UP)
		{
			return slotsTop;
		}

		return slotsSides;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side)
	{
		return isItemValidForSlot(slot, stack);
	}

	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
	{
		if (slot == OUTPUT)
		{
			return true;
		}
		else if (stack.getItem() instanceof ItemBucket)
		{
			return true;
		}
		else if (side != EnumFacing.UP)
		{
			return true;
		}

		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerEnderSmelter(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":enderSmelter";
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
