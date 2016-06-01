package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.event.DamageSourceDiffusion;
import net.einsteinsci.betterbeginnings.items.*;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.einsteinsci.betterbeginnings.util.InfusionRepairUtil;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.einsteinsci.betterbeginnings.util.NBTUtil;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
import org.apache.logging.log4j.Level;

import java.util.*;

import static net.einsteinsci.betterbeginnings.util.NBTUtil.*;

public class TileEntityInfusionRepair extends TileEntity implements IUpdatePlayerListBox, IInventory
{
	public ItemStack[] stacks = new ItemStack[10];

	public static final int SLOT_CENTER = 0;
	public static final int SLOT_OUTPUT = 9;
	public static final int SLOT_INPUT_START = 1;

	private String tileName;

	private short levelsToFill;
	private short levelsTaken;
	private short healthTaken;

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

		if (tagCompound.hasKey("CustomName"))
		{
			tileName = tagCompound.getString("CustomName");
		}

		levelsToFill = tagCompound.getShort("LevelsNeeded");
		levelsTaken = tagCompound.getShort("LevelsTaken");
		healthTaken = tagCompound.getShort("HealthTaken");
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
		tagCompound.setShort("HealthTaken", healthTaken);
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

	public boolean isDiffusionMode()
	{
		ItemStack st = stackTool();
		if (st == null)
		{
			return false;
		}

		return st.getItem() instanceof ItemBBCloth;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			// diffusion mode
			if (isDiffusionMode())
			{
				List<EntityItem> entitiesOnTop = getItemsOnTop();
				for (EntityItem ei : entitiesOnTop)
				{
					ItemStack stack = ei.getEntityItem();

					boolean addit = false;
					if (!diffusionHasTool())
					{
						if (InfusionRepairUtil.isToolValidForDiffusion(stack))
						{
							addit = true;
						}
					}

					if (stack.getItem() instanceof ItemBook)
					{
						if (!diffusionReady() && diffusionHasTool())
						{
							addit = true;
						}
					}

					if (addit)
					{
						addInput(stack.copy());
						stack.stackSize--;

						if (stack.stackSize <= 0)
						{
							ei.setDead();
						}

						worldObj.markBlockForUpdate(pos);
						markDirty();
					}
				}

				if (diffusionReady())
				{
					if (healthTaken < BBConfig.diffusionHealthTaken)
					{
						EntityPlayer victim = getVictim();
						if (victim != null && ticksAge % 5 == 0)
						{
							if (!victim.capabilities.isCreativeMode)
							{
								victim.attackEntityFrom(new DamageSourceDiffusion(), 1.0f);
							}
							healthTaken++;

							worldObj.markBlockForUpdate(pos);
							markDirty();
						}
					}
					else if (healthTaken == BBConfig.diffusionHealthTaken)
					{
						worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "mob.zombie.unfect", 1.0f, 1.0f);

						ItemStack stackTool = null;
						int toolSlotID = -1;
						for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT; i++)
						{
							ItemStack stack = stacks[i];
							if (stack == null)
							{
								continue;
							}

							if (stack.getItem() instanceof ItemTool ||
								stack.getItem() instanceof ItemSword ||
								stack.getItem() instanceof ItemArmor)
							{
								stackTool = stack;
								toolSlotID = i;
								break;
							}
						}

						if (stackTool == null)
						{
							LogUtil.log(Level.ERROR, "Tool not found in diffusion!");
							ChatUtil.sendModChatToServer(ChatUtil.DARK_RED + "ERROR! Tool not found in diffusion!");
							healthTaken = 0;
							return;
						}

						NBTTagList enchList = stackTool.getEnchantmentTagList();
						if (enchList == null)
						{
							LogUtil.log(Level.ERROR, "Tool does not have enchantments!");
							healthTaken = 0;
							return;
						}

						Random rand = new Random();
						int enchSpot = rand.nextInt(enchList.tagCount());
						NBTTagCompound enchTag = enchList.getCompoundTagAt(enchSpot);
						short enchID = enchTag.getShort("id");
						short enchLvl = enchTag.getShort("lvl");
						Enchantment ench = Enchantment.getEnchantmentById(enchID);

						enchList.removeTag(enchSpot);
						if (enchList.hasNoTags())
						{
							stackTool.getTagCompound().removeTag("ench");
						}

						int dmgAmount = stackTool.getMaxDamage() / 5;
						int damagedMeta = stackTool.getItemDamage() + dmgAmount;
						damagedMeta = Math.min(damagedMeta, stackTool.getMaxDamage() - 1); // diffusion cannot break a tool completely
						stackTool.setItemDamage(damagedMeta);
						stacks[SLOT_CENTER] = stackTool;
						stacks[toolSlotID] = null;

						for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT; i++)
						{
							ItemStack stack = stacks[i];
							if (stack == null)
							{
								continue;
							}

							if (stack.getItem() instanceof ItemBook)
							{
								ItemStack enchBook = new ItemStack(Items.enchanted_book);
								NBTUtil.addBookEnchantment(enchBook, ench, enchLvl);
								EntityItem entity = new EntityItem(worldObj, pos.getX(), pos.getY() + 1, pos.getZ(),
									enchBook.copy());
								worldObj.spawnEntityInWorld(entity);
								stacks[i] = null;

								break;
							}
						}

						LogUtil.log(Level.INFO, "Enchantment diffusion complete.");

						healthTaken = 0;

						worldObj.markBlockForUpdate(pos);
						markDirty();
					}
				}
			}
			else // regular repair infusion
			{
				InfusionIngredient next = getNextIngredient();
				if (next == null)
				{
					return;
				}

				List<EntityItem> entitiesOnTop = getItemsOnTop();

				if (next.isXP)
				{
					if (levelsToFill != next.xp)
					{
						levelsToFill = (short)next.xp;

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
				else
				{
					for (EntityItem ei : entitiesOnTop)
					{
						ItemStack stack = ei.getEntityItem();

						if (next.ore == null)
						{
							LogUtil.logDebug(Level.ERROR, "next.ore is null!");
							continue;
						}

						if (next.ore.matches(stack) && stack.stackSize >= next.ore.stackSize)
						{
							stack.stackSize -= next.ore.stackSize;
							addInput(new ItemStack(stack.getItem(), next.ore.stackSize, stack.getMetadata()));

							if (stack.stackSize <= 0)
							{
								ei.setDead();
							}

							worldObj.markBlockForUpdate(pos);
							markDirty();
						}
					}
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
		return stacks[SLOT_CENTER];
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

	public static boolean isValidCenterSlot(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}

		Item item = stack.getItem();

		if (item instanceof ItemBBCloth)
		{
			return true;
		}

		return isValidTool(stack);
	}

	public static boolean isValidTool(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}

		Item item = stack.getItem();

		return item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemArmor ||
			item instanceof ItemBow || item instanceof ItemFishingRod || item instanceof ItemFlintAndSteel;
	}

	// when TE is clicked by player
	public void onClick(EntityPlayer player)
	{
		ItemStack held = player.getHeldItem();

		if (held != null && isValidCenterSlot(held))
		{
			// eat the tool
			stacks[SLOT_CENTER] = held.copy();
			player.setCurrentItemOrArmor(0, null);

			// om nom
			worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "random.pop", 1.0f, 1.0f, true);
		}
		else if (held == null && stackTool() != null)
		{
			// give it back
			player.setCurrentItemOrArmor(0, stacks[SLOT_CENTER]);
			stacks[SLOT_CENTER] = null;

			// ptooey
			worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "random.pop", 1.0f, 1.0f, true);
		}
	}

	public InfusionIngredient getNextIngredient()
	{
		if (stackTool() == null || stackTool().getItemDamage() == 0)
		{
			return null;
		}

		List<OreRecipeElement> req = InfusionRepairUtil.getRequiredStacks(stackTool());

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

			OreRecipeElement needs = null;
			for (OreRecipeElement s : req)
			{
				if (s.matches(sHas))
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
			return new InfusionIngredient(InfusionRepairUtil.getTakenLevels(stackTool()));
		}

		OreRecipeElement ore = req.get(0);
		return new InfusionIngredient(ore);
	}

	public boolean diffusionHasTool()
	{
		if (!isDiffusionMode())
		{
			return false;
		}

		for (int i = 0; i < 8; i++)
		{
			ItemStack stack = stackInput(i);
			if (stack == null)
			{
				continue;
			}

			if (stack.getItem() instanceof ItemBBCloth) // exclude cloth when looking for tool
			{
				continue;
			}

			if (isValidCenterSlot(stack))
			{
				return true;
			}
		}

		return false;
	}

	public boolean diffusionReady()
	{
		if (!diffusionHasTool())
		{
			return false;
		}

		for (int i = 0; i < 8; i++)
		{
			ItemStack stack = stackInput(i);
			if (stack == null)
			{
				continue;
			}

			if (stack.getItem() == Items.book)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? tileName : "container.infusion";
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

	public short getHealthTaken()
	{
		return healthTaken;
	}

	public static class InfusionIngredient
	{
		public OreRecipeElement ore;

		public boolean isXP;
		public int xp;

		public InfusionIngredient(Item item, int _count, int meta)
		{
			isXP = false;
			ore = new OreRecipeElement(new ItemStack(item, _count, meta));
		}

		public InfusionIngredient(OreRecipeElement _ore)
		{
			ore = _ore;
			isXP = false;
		}

		public InfusionIngredient(int levelCount)
		{
			isXP = true;
			xp = levelCount;
		}

		@Override
		public String toString()
		{
			if (isXP)
			{
				return "XP: " + xp + "L";
			}

			if (ore.getOreDictionaryEntry().isEmpty())
			{
				return ore.stackSize + "x" + ore.getFirst().getUnlocalizedName() + "@" + ore.getFirst().getItemDamage();
			}
			else
			{
				return ore.stackSize + "x" + ore.getOreDictionaryEntry();
			}
		}
	}
}
