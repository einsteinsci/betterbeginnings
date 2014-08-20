package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

import static net.einsteinsci.betterbeginnings.ModMain.Log;

public class TileEntityRepairTable extends TileEntity implements IInventory
{
	public List<ItemStack> requiredItems = new ArrayList<ItemStack>();
	public int requiredLevels = 0;
	public int takenLevels = 0;
	public ItemStack[] stacks = new ItemStack[9];
	public String customName;

	public TileEntityRepairTable()
	{
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Items", 10);

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < stacks.length)
			{
				stacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		if (tagCompound.hasKey("CustomName", 8))
		{
			customName = tagCompound.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < stacks.length; ++i)
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
		if (hasCustomInventoryName())
		{
			tagCompound.setString("CustomName", customName);
		}
	}

	@Override
	public void updateEntity()
	{
		getRequiredItems();
	}

	// WARNING: REALLY BIG METHOD
	@Deprecated
	public void getRequiredItems()
	{
		ItemStack repaired = getStackInSlot(0);
		requiredItems.clear();
		requiredLevels = 0;
		takenLevels = 0;

		if (repaired == null)
		{
			return;
		}

		if (repaired.getItem() == RegisterItems.noobWoodSword)
		{
			requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
			requiredLevels = 1;
			takenLevels = 0;
		}
		else if (repaired.getItem() == RegisterItems.flintKnife || repaired.getItem() == RegisterItems.flintHatchet)
		{
			requiredItems.add(new ItemStack(Items.flint));
			requiredLevels = 1;
			takenLevels = 0;
		}
		else if (repaired.getItem() == RegisterItems.boneKnife)
		{
			requiredItems.add(new ItemStack(Items.bone));
			requiredLevels = 5;
			takenLevels = 1;
		}
		else if (repaired.getItem() == RegisterItems.bonePickaxe)
		{
			requiredItems.add(new ItemStack(RegisterItems.boneShard));
			requiredLevels = 1;
			takenLevels = 0;
		}
		else if (repaired.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool)repaired.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(tool.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
					requiredLevels = 1;
					takenLevels = 0;
					break;
				case STONE:
					requiredItems.add(new ItemStack(Blocks.stone, 1));
					requiredLevels = 5;
					takenLevels = 1;
					break;
				case IRON:
					requiredItems.add(new ItemStack(Items.iron_ingot));
					requiredLevels = 10;
					takenLevels = 2;
					break;
				case GOLD:
					requiredItems.add(new ItemStack(Items.gold_nugget, 2));
					requiredLevels = 20;
					takenLevels = 2;
					break;
				case EMERALD: // See "WTF" below.
					requiredItems.add(new ItemStack(Items.redstone, 32));
					requiredLevels = 20;
					takenLevels = 5;
					break;
				default:
					requiredItems.add(new ItemStack(Items.emerald, 4));
					requiredLevels = 20;
					takenLevels = 5;
					break;
			}
		}
		else if (repaired.getItem() instanceof ItemSword)
		{
			ItemSword sword = (ItemSword)repaired.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(sword.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
					requiredLevels = 1;
					takenLevels = 0;
					break;
				case STONE:
					requiredItems.add(new ItemStack(Blocks.stone, 1));
					requiredLevels = 5;
					takenLevels = 1;
				case IRON:
					requiredItems.add(new ItemStack(Items.iron_ingot));
					requiredLevels = 10;
					takenLevels = 2;
					break;
				case GOLD:
					requiredItems.add(new ItemStack(Items.gold_nugget, 2));
					requiredLevels = 20;
					takenLevels = 2;
					break;
				case EMERALD: // See "WTF" below.
					requiredItems.add(new ItemStack(Items.redstone, 32));
					requiredLevels = 20;
					takenLevels = 5;
					break;
				default:
					if (material == RegisterItems.noobWood)
					{
						requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
						requiredLevels = 1;
						takenLevels = 0;
					}
					else
					{
						requiredItems.add(new ItemStack(Items.emerald, 4));
						requiredLevels = 20;
						takenLevels = 5;
					}
					break;
			}
		}
		else if (repaired.getItem() instanceof ItemArmor)
		{
			ItemArmor armor = (ItemArmor)repaired.getItem();

			switch (armor.getArmorMaterial())
			{
				case CLOTH:
					requiredItems.add(new ItemStack(Items.leather));
					requiredLevels = 1;
					takenLevels = 0;
					break;
				case CHAIN:
					requiredItems.add(new ItemStack(RegisterItems.ironNugget));
					requiredLevels = 5;
					takenLevels = 1;
					break;
				case IRON:
					requiredItems.add(new ItemStack(Items.iron_ingot));
					requiredLevels = 10;
					takenLevels = 2;
				case GOLD:
					requiredItems.add(new ItemStack(Items.gold_nugget, 2));
					requiredLevels = 20;
					takenLevels = 2;
				case DIAMOND: // WTF MOJANG?! (See above)
					requiredItems.add(new ItemStack(Items.redstone, 32));
					requiredLevels = 20;
					takenLevels = 5;
				default:
					requiredItems.add(new ItemStack(Items.emerald, 4));
					requiredLevels = 20;
					takenLevels = 5;
					break;
			}
		}
		else if (repaired.getItem() == Items.shears)
		{
			requiredItems.add(new ItemStack(Items.iron_ingot));
			requiredLevels = 5;
			takenLevels = 1;
		}
		else if (repaired.getItem() == Items.fishing_rod)
		{
			requiredItems.add(new ItemStack(Items.string, 4));
			requiredLevels = 5;
			takenLevels = 1;
		}
		else if (repaired.getItem() == Items.flint_and_steel)
		{
			requiredItems.add(new ItemStack(Items.flint));
			requiredLevels = 1;
			takenLevels = 0;
		}

		if (repaired.isItemEnchanted())
		{
			if (repaired.stackTagCompound != null)
			{
				if (repaired.stackTagCompound.getTag("ench") != null)
				{
					NBTTagList enchants = (NBTTagList)repaired.stackTagCompound.getTag("ench");
					for (int i = 0; i < enchants.tagCount(); i++)
					{
						NBTTagCompound enchant = enchants.getCompoundTagAt(i);
						int enchId = enchant.getInteger("id");
						int level = enchant.getInteger("lvl");

						switch (enchId)
						{
							case 0: // Protection
								requiredItems.add(new ItemStack(Blocks.iron_bars, 2 * level));
								break;
							case 1: // Fire Protection
								requiredItems.add(new ItemStack(Items.bucket, level));
								break;
							case 2: // Feather Falling
								requiredItems.add(new ItemStack(Items.feather, 2 * level));
								break;
							case 3: // Blast Protection
								requiredItems.add(new ItemStack(Blocks.cobblestone, 8 * level));
								break;
							case 4: // Projectile Protection
								requiredItems.add(new ItemStack(Items.snowball, 4 * level));
								break;
							case 5: // Respiration
								requiredItems.add(new ItemStack(Items.glass_bottle, level));
								break;
							case 6: // Aqua Affinity
								requiredItems.add(new ItemStack(Blocks.clay, level));
								break;
							case 7: // Thorns
								requiredItems.add(new ItemStack(Blocks.cactus, 4 * level));
								break;
							case 8: // Depth Strider
								// requiredItems.add(new ItemStack(Blocks.prismarine, level));
								break;
							case 16: // Sharpness
								requiredItems.add(new ItemStack(Items.quartz, 4 * level));
								break;
							case 17: // Smite
								requiredItems.add(new ItemStack(Blocks.soul_sand, 2 * level));
								break;
							case 18: // Anthropods
								requiredItems.add(new ItemStack(Items.fermented_spider_eye, level));
								break;
							case 19: // Knockback
								requiredItems.add(new ItemStack(Blocks.piston, level));
								break;
							case 20: // Fire Aspect
								requiredItems.add(new ItemStack(Items.blaze_powder, 2 * level));
								break;
							case 21: // Looting
								requiredItems.add(new ItemStack(Items.gold_ingot, 2 * level));
								break;
							case 32: // Efficiency
								requiredItems.add(new ItemStack(Items.sugar, 4 * level));
								break;
							case 33: // Silk Touch
								requiredItems.add(new ItemStack(Blocks.wool, 8 * level));
								break;
							case 34: // Unbreaking
								requiredItems.add(new ItemStack(Blocks.obsidian, level));
								break;
							case 35: // Fortune
								requiredItems.add(new ItemStack(Items.dye, 4 * level, 4));
								break;
							case 48: // Power
								requiredItems.add(new ItemStack(RegisterItems.leatherStrip, level));
								break;
							case 49: // Punch
								requiredItems.add(new ItemStack(Items.gunpowder, level));
								break;
							case 50: // Flame
								requiredItems.add(new ItemStack(Items.fire_charge, level));
								break;
							case 51: // Infinity
								requiredItems.add(new ItemStack(Items.arrow, 16 * level));
								break;
							case 61: // Luck of the Sea
								requiredItems.add(new ItemStack(Blocks.waterlily, level));
								break;
							case 62: // Lure
								requiredItems.add(new ItemStack(Items.fish, level, 3));
								break;
						}
					}
				}
			}
		}
	}

	public void repairCenter()
	{
		stacks[0].setItemDamage(0);
	}

	@Deprecated
	public void repair(EntityPlayer player)
	{
		if (!canRepair(player))
		{
			Log(Level.ERROR, "Player " + player.getDisplayName() + " is unable to repair tool.");
		}

		for (int i = 0; i < requiredItems.size(); ++i)
		{
			boolean foundIt = false;
			ItemStack needed = requiredItems.get(i);
			for (int j = 1; j < stacks.length; ++j)
			{
				if (stacks[j] != null)
				{
					ItemStack tested = stacks[j];
					if (tested.getItem() == needed.getItem() &&
							(tested.getItemDamage() == needed.getItemDamage() || needed
									.getItemDamage() == OreDictionary.WILDCARD_VALUE) &&
							tested.stackSize >= needed.stackSize)
					{
						tested.stackSize -= needed.stackSize;
						if (tested.stackSize == 0)
						{
							tested = null;
						}
						break;
					}
				}
			}
		}

		if (!player.capabilities.isCreativeMode)
		{
			player.experienceLevel -= takenLevels;
		}

		// circleSlots[0].getStack().setItemDamage(0);
		// repairTableInventory.getStackInSlot(0).setItemDamage(0);
		// ItemStack stack2 = circleSlots[0].getStack().copy();
		// stack2.setItemDamage(0);
		// repairTableInventory.setInventorySlotContents(0, stack2);

		// ========================================================================================
		// BUG HERE
		// ========================================================================================
		//ModMain.network.sendToServer(new RepairTableRepairPacket(circleSlots[0].getStack()));
		// ========================================================================================


		player.inventoryContainer.detectAndSendChanges();
	}

	public boolean canRepair(EntityPlayer player)
	{
		if (stacks[0] != null)
		{
			if (!stacks[0].isItemDamaged())
			{
				return false;
			}
		}
		else
		{
			return false;
		}

		for (ItemStack needed : requiredItems)
		{
			boolean foundIt = false;
			//ItemStack needed = requiredItems.get(i);
			for (int j = 1; j < stacks.length; ++j)
			{
				if (stacks[j] != null)
				{
					ItemStack tested = stacks[j];
					if (tested.getItem() == needed.getItem() &&
							(tested.getItemDamage() == needed.getItemDamage() || needed
									.getItemDamage() == OreDictionary.WILDCARD_VALUE) &&
							tested.stackSize >= needed.stackSize)
					{
						foundIt = true;
						break;
					}
				}
			}

			if (!foundIt)
			{
				return false;
			}
		}

		if (player.experienceLevel < requiredLevels && !player.capabilities.isCreativeMode)
		{
			return false;
		}

		return true;
	}

	@Override
	public int getSizeInventory()
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (stacks[slot] != null)
		{
			ItemStack stack;
			if (stacks[slot].stackSize <= amount)
			{
				stack = stacks[slot];
				stacks[slot] = null;
				return stack;
			}
			else
			{
				stack = stacks[slot].splitStack(amount);

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
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? customName : "container.repairTable";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return customName != null && customName.length() > 0;
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
		if (slot == 0)
		{
			return stack.isItemStackDamageable();
		}

		return true;
	}
}
