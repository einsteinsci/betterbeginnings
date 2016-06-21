package net.einsteinsci.betterbeginnings.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntitySpecializedFurnace extends TileEntity implements ISidedInventory, IUpdatePlayerListBox
{
	protected ItemStack[] specialFurnaceStacks;
	protected String customName;
	public int burnTime;
	public int currentItemBurnLength;
	public int cookTime;
	protected int processTime;

	protected TileEntitySpecializedFurnace(int numStacks)
	{
		specialFurnaceStacks = new ItemStack[numStacks];
	}

	// Client
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int progress)
	{
		return cookTime * progress / processTime;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int time)
	{
		if (currentItemBurnLength <= 0)
		{
			currentItemBurnLength = processTime;
		}

		return burnTime * time / currentItemBurnLength;
	}
	
	// Inventory
	@Override
	public void clear()
	{
		for (int i = 0; i < specialFurnaceStacks.length; i++)
		{
			specialFurnaceStacks[i] = null;
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (specialFurnaceStacks[slot] != null)
		{
			ItemStack stack;
			if (specialFurnaceStacks[slot].stackSize <= amount)
			{
				stack = specialFurnaceStacks[slot];
				specialFurnaceStacks[slot] = null;
				return stack;
			}
			else
			{
				stack = specialFurnaceStacks[slot].splitStack(amount);

				if (specialFurnaceStacks[slot].stackSize == 0)
				{
					specialFurnaceStacks[slot] = null;
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
	public ItemStack getStackInSlot(int i)
	{
		return specialFurnaceStacks[i];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (specialFurnaceStacks[slot] != null)
		{
			ItemStack stack = specialFurnaceStacks[slot];
			specialFurnaceStacks[slot] = null;
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
		specialFurnaceStacks[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getSizeInventory()
	{
		return specialFurnaceStacks.length;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	//TE related
	@Override
	public void markDirty()
	{ }
	
	//Name
	@Override
	public String getCommandSenderName() 
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return customName != null && customName.length() > 0;
	}

	public void setBlockName(String displayName)
	{
		customName = displayName;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(getCommandSenderName());
	}

	// NBT
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		specialFurnaceStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < specialFurnaceStacks.length)
			{
				specialFurnaceStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}	
		
		burnTime = tagCompound.getShort("BurnTime");
		cookTime = tagCompound.getShort("CookTime");
		
		if (tagCompound.hasKey("CustomName"))
		{
			customName = tagCompound.getString("CustomName");
		}
	}		

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) 
	{
		super.writeToNBT(tagCompound);

		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < specialFurnaceStacks.length; ++i)
		{
			if (specialFurnaceStacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				specialFurnaceStacks[i].writeToNBT(itemTag);
				itemTag.setByte("Slot", (byte)i);
				tagList.appendTag(itemTag);
			}
		}

		tagCompound.setTag("Items", tagList);

		//The Nether Brick oven doesn't use burnTime
		if (!(this instanceof TileEntityNetherBrickOven))
		{
			tagCompound.setShort("BurnTime", (short)burnTime);
		}

		tagCompound.setShort("CookTime", (short)cookTime);
		
		if (hasCustomName())
		{
			tagCompound.setString("CustomName", customName);
		}
	}

	public abstract void smeltItem();

	public abstract boolean canSmelt();

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
	
	// Miscellaneous
	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{ }

	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	public boolean isBurning()
	{
		return burnTime > 0;
	}
}
