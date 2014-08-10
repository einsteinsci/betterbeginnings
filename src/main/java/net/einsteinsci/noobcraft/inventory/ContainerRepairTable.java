package net.einsteinsci.noobcraft.inventory;

import java.util.ArrayList;
import java.util.List;

import net.einsteinsci.noobcraft.network.RepairTableRepairPacket;
import net.einsteinsci.noobcraft.register.RegisterBlocks;
import net.einsteinsci.noobcraft.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;

import static net.einsteinsci.noobcraft.ModMain.*;

public class ContainerRepairTable extends Container
{
	InventoryRepairTable repairTableInventory = new InventoryRepairTable(this, 9);
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;
	
	public List<ItemStack> requiredItems = new ArrayList<ItemStack>();
	public int requiredLevels = 0;
	public int takenLevels = 0;
	
	public Slot[] circleSlots = new Slot[9];
	
	public ContainerRepairTable(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		worldObj = world;
		posX = x;
		posY = y;
		posZ = z;
		int l;
		int i1;
		circleSlots[0] = new Slot(repairTableInventory, 0, 116, 35);
		
		circleSlots[1] = new Slot(repairTableInventory, 1, 116, 6);
		circleSlots[2] = new Slot(repairTableInventory, 2, 139, 12);
		circleSlots[3] = new Slot(repairTableInventory, 3, 145, 35);
		circleSlots[4] = new Slot(repairTableInventory, 4, 139, 58);
		circleSlots[5] = new Slot(repairTableInventory, 5, 116, 63);
		circleSlots[6] = new Slot(repairTableInventory, 6, 93, 58);
		circleSlots[7] = new Slot(repairTableInventory, 7, 87, 35);
		circleSlots[8] = new Slot(repairTableInventory, 8, 93, 12);
		
		for (Slot slot : circleSlots)
		{
			addSlotToContainer(slot);
		}
		
		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 9; ++i1)
			{
				addSlotToContainer(new Slot(invPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
			}
		}
		
		for (l = 0; l < 9; ++l)
		{
			addSlotToContainer(new Slot(invPlayer, l, 8 + l * 18, 142));
		}
	}
	
	public boolean canRepair(EntityPlayer player)
	{
		if (circleSlots[0].getStack() != null)
		{
			if (!circleSlots[0].getStack().isItemDamaged())
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
			for (int j = 1; j < circleSlots.length; ++j)
			{
				if (circleSlots[j].getStack() != null)
				{
					ItemStack tested = circleSlots[j].getStack();
					if (tested.getItem() == needed.getItem() &&
						(tested.getItemDamage() == needed.getItemDamage() || needed.getItemDamage() == OreDictionary.WILDCARD_VALUE) &&
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
			for (int j = 1; j < circleSlots.length; ++j)
			{
				if (circleSlots[j].getStack() != null)
				{
					ItemStack tested = circleSlots[j].getStack();
					if (tested.getItem() == needed.getItem() &&
						(tested.getItemDamage() == needed.getItemDamage() || needed.getItemDamage() == OreDictionary.WILDCARD_VALUE) &&
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
			
			// circleSlots[0].getStack().setItemDamage(0);
			// repairTableInventory.getStackInSlot(0).setItemDamage(0);
			// ItemStack stack2 = circleSlots[0].getStack().copy();
			// stack2.setItemDamage(0);
			// repairTableInventory.setInventorySlotContents(0, stack2);
			
			// ========================================================================================
			// BUG HERE
			// ========================================================================================
			network.sendToServer(new RepairTableRepairPacket(circleSlots[0].getStack()));
			// ========================================================================================
			
			
			player.inventoryContainer.detectAndSendChanges();
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		boolean flag = false;
		for (int i = 0; i < inventorySlots.size(); ++i)
		{
			ItemStack itemstack = ((Slot)inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack)inventoryItemStacks.get(i);
			
			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
			{
				itemstack1 = itemstack == null ? null : itemstack.copy();
				inventoryItemStacks.set(i, itemstack1);
				
				for (Object obj : crafters)
				{
					if (obj != null)
					{
						((ICrafting) obj).sendSlotContents(this, i, itemstack1);
					}
				}
				
				flag = true;
			}
		}
		
		if (flag)
		{
			getRequiredItems();
		}
	}
	
	// Warning: REALLY BIG METHOD
	public void getRequiredItems()
	{
		ItemStack repaired = repairTableInventory.getStackInSlot(0);
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
			ToolMaterial material = ToolMaterial.valueOf(tool.getToolMaterialName());
			
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
			ToolMaterial material = ToolMaterial.valueOf(sword.getToolMaterialName());
			
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
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return worldObj.getBlock(posX, posY, posZ) == RegisterBlocks.repairTable &&
			worldObj.getBlockMetadata(posX, posY, posZ) == 0 &&
			player.getDistanceSq(posX + 0.5D, posY + 0.5D, posZ + 0.5D) <= 64.0D;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		
		if (!worldObj.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack = repairTableInventory.getStackInSlotOnClosing(i);
				
				if (itemstack != null)
				{
					player.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(slotId);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (slotId < 9)
			{
				if (!mergeItemStack(itemstack1, 9, 45, false))
				{
					return null;
				}
			}
			else if (slotId >= 9 && slotId <= 44)
			{
				if (itemstack != null)
				{
					if (itemstack.getItem().isDamageable())
					{
						if (!mergeItemStack(itemstack1, 0, 1, false))
						{
							return null;
						}
					}
					else if (!mergeItemStack(itemstack1, 1, 9, false))
					{
						return null;
					}
				}
			}
			else if (!mergeItemStack(itemstack1, 9, 45, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}
}
