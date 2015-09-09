package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.register.InfusionRepairUtil;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static net.einsteinsci.betterbeginnings.util.NBTUtil.*;

public class TileEntityInfusionRepair extends TileEntity implements IUpdatePlayerListBox, IInventory
{
	public ItemStack[] stacks = new ItemStack[10];

	public static final int SLOT_TOOL = 0;
	public static final int SLOT_OUTPUT = 9;
	public static final int SLOT_INPUT_START = 1;

	private String tileName;

	private short levelsToFill;
	private short levelsTaken;

	private int ticksAge = 0;

	public TileEntityInfusionRepair()
	{
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Items", TAG_COMPOUND);
		stacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < stacks.length)
			{
				stacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		if (tagCompound.hasKey("CustomName", TAG_STRING))
		{
			tileName = tagCompound.getString("CustomName");
		}

		levelsToFill = tagCompound.getShort("LevelsNeeded");
		levelsTaken = tagCompound.getShort("LevelsTaken");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < stacks.length; i++)
		{
			if (stacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				stacks[i].writeToNBT(itemTag);
				itemTag.setByte("Slot", (byte)i);
				tagList.appendTag(itemTag);
			}
		}
		tagCompound.setTag("Items", tagList);

		if (hasCustomName())
		{
			tagCompound.setString("CustomName", tileName);
		}

		tagCompound.setShort("LevelsToFill", levelsToFill);
		tagCompound.setShort("LevelsTaken", levelsTaken);
	}

	// region slot stuff

	@Override
	public int getSizeInventory()
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return stacks[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		if (stacks[slot] != null)
		{
			ItemStack stack;
			if (stacks[slot].stackSize <= count)
			{
				stack = stacks[slot];
				stacks[slot] = null;
				return stack;
			}
			else
			{
				stack = stacks[slot].splitStack(count);

				if (stacks[slot].stackSize == 0)
				{
					stacks[slot] = null;
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
		if (stacks[slot] != null)
		{
			ItemStack stack = stacks[slot];
			stacks[slot] = null;
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
		stacks[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(pos) != this)
		{
			return false;
		}

		BlockPos buf = new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		return player.getDistanceSq(buf) <= 64.0;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{ }

	@Override
	public void closeInventory(EntityPlayer player)
	{ }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		// no pipes here
		return false;
	}

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

	@Override
	public void clear()
	{
		for (int i = 0; i < stacks.length; i++)
		{
			stacks[i] = null;
		}
	}

	// endregion

	public int getTicksAge()
	{
		return ticksAge;
	}

	public boolean isRepairComplete()
	{
		ItemStack st = stackTool();
		int dmg = st != null ? st.getItemDamage() : 0;
		return st != null && dmg == 0;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			Ingredient next = getNextIngredient();
			if (next == null)
			{
				return;
			}

			List<EntityItem> entitiesOnTop = getItemsOnTop();

			if (next.isXP)
			{
				if (levelsToFill != next.count)
				{
					levelsToFill = (short)next.count;

					worldObj.markBlockForUpdate(pos);
					markDirty();
				}

				EntityPlayer victim = getVictim();
				if (victim != null && (victim.experienceLevel > 0 || victim.capabilities.isCreativeMode) &&
					levelsTaken < levelsToFill && ticksAge % 5 == 0 && !isRepairComplete())
				{
					if (!victim.capabilities.isCreativeMode)
					{
						victim.removeExperienceLevel(1);
					}
					levelsTaken++;

					worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "random.orb", 1.0f, 1.0f);
					worldObj.markBlockForUpdate(pos);
					markDirty();
				}
				else if (levelsTaken == levelsToFill)
				{
					worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "random.levelup", 1.0f, 1.0f);
					stackTool().setItemDamage(0);
					for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT; i++)
					{
						stacks[i] = null;
					}
					levelsTaken = 0;

					worldObj.markBlockForUpdate(pos);
					markDirty();
				}
			}

			for (EntityItem ei : entitiesOnTop)
			{
				ItemStack stack = ei.getEntityItem();

				if (stack.getItem() == next.item && stack.stackSize >= next.count &&
					stack.getItemDamage() == next.damage || next.damage == OreDictionary.WILDCARD_VALUE)
				{
					stack.stackSize -= next.count;
					addInput(new ItemStack(next.item, next.count, next.damage));

					if (stack.stackSize <= 0)
					{
						ei.setDead();
					}

					worldObj.markBlockForUpdate(pos);
					markDirty();
				}
			}
		}

		ticksAge = (ticksAge + 1) % 240;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		writeToNBT(syncData);
		return new S35PacketUpdateTileEntity(pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
	}

	public ItemStack stackTool()
	{
		return stacks[SLOT_TOOL];
	}
	public ItemStack stackOutput()
	{
		return stacks[SLOT_OUTPUT];
	}
	public ItemStack stackInput(int infusedSlotId)
	{
		if (infusedSlotId < 0)
		{
			infusedSlotId = 0;
		}
		if (infusedSlotId > 8)
		{
			infusedSlotId = 8;
		}

		return stacks[SLOT_INPUT_START + infusedSlotId];
	}

	public List<EntityItem> getItemsOnTop()
	{
		List list = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
			pos.add(0, 1, 0), pos.add(1, 2, 1)), IEntitySelector.IS_STANDALONE);

		List<EntityItem> entities = new ArrayList<>();
		for (Object obj : list)
		{
			if (obj instanceof EntityItem)
			{
				EntityItem items = (EntityItem)obj;
				entities.add(items);
			}
		}

		return entities;
	}

	public EntityPlayer getVictim()
	{
		List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(
			pos.add(0, 1, 0), pos.add(1, 2, 1)), IEntitySelector.NOT_SPECTATING);

		for (Object obj : list)
		{
			if (obj instanceof EntityPlayer)
			{
				return (EntityPlayer)obj;
			}
		}

		return null;
	}

	public void addInput(ItemStack stack)
	{
		for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT; i++)
		{
			if (stacks[i] != null && stacks[i].getItem().equals(stack.getItem()))
			{
				stacks[i].stackSize += stack.stackSize;
				return;
			}
		}

		for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT; i++)
		{
			if (stacks[i] == null)
			{
				stacks[i] = stack;
				return;
			}
		}
	}

	public static boolean isValidTool(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}

		Item item = stack.getItem();

		return item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemArmor;
	}

	// when TE is clicked by player
	public void onClick(EntityPlayer player)
	{
		ItemStack held = player.getHeldItem();

		if (held != null && isValidTool(held))
		{
			// eat the tool
			stacks[SLOT_TOOL] = held.copy();
			player.setCurrentItemOrArmor(0, null);

			// om nom
			worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "random.pop", 1.0f, 1.0f, true);
		}
		else if (held == null && stackTool() != null)
		{
			// give it back
			player.setCurrentItemOrArmor(0, stacks[SLOT_TOOL]);
			stacks[SLOT_TOOL] = null;

			// ptooey
			worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "random.pop", 1.0f, 1.0f, true);
		}
	}

	public Ingredient getNextIngredient()
	{
		if (stackTool() == null || stackTool().getItemDamage() == 0)
		{
			return null;
		}

		List<ItemStack> req = InfusionRepairUtil.getRequiredStacks(stackTool());

		if (req == null || req.isEmpty())
		{
			return null;
		}

		for (ItemStack sHas : stacks)
		{
			if (sHas == null)
			{
				continue;
			}

			ItemStack needs = null;
			for (ItemStack s : req)
			{
				if (s.getItem().equals(sHas.getItem()) && sHas.stackSize >= s.stackSize &&
					(s.getItemDamage() == sHas.getItemDamage() || s.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				{
					needs = s;
					break;
				}
			}

			if (needs != null)
			{
				req.remove(needs);
			}
		}

		if (req.isEmpty())
		{
			return new Ingredient(InfusionRepairUtil.getTakenLevels(stackTool()));
		}

		ItemStack stack = req.get(0);
		return new Ingredient(stack.getItem(), stack.stackSize, stack.getItemDamage());
	}

	@Override
	public String getCommandSenderName()
	{
		return tileName;
	}

	@Override
	public boolean hasCustomName()
	{
		return tileName != null && !tileName.isEmpty();
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(getCommandSenderName());
	}

	public static class Ingredient
	{
		public Item item;
		public int count;
		public int damage;

		public boolean isXP;

		public Ingredient(Item _item, int _count, int _damage)
		{
			isXP = false;
			item = _item;
			count = _count;
			damage = _damage;
		}

		public Ingredient(int levelCount)
		{
			isXP = true;
			count = levelCount;
		}

		@Override
		public String toString()
		{
			if (isXP)
			{
				return "XP: " + count + "L";
			}

			return count + "x" + item.getUnlocalizedName() + "@" + damage;
		}
	}
}
