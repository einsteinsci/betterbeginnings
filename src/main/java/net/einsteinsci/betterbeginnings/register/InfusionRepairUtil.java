package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.inventory.InventoryInfusionRepair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * Created by einsteinsci on 8/20/2014.
 */
public class InfusionRepairUtil
{
	public static boolean canRepair(InventoryInfusionRepair repairTable, EntityPlayer player)
	{
		return canRepairIgnoreXp(repairTable) && player.experienceLevel >= getNeededLevels(repairTable);
	}

	public static boolean canRepairIgnoreXp(InventoryInfusionRepair repairTable)
	{
		ArrayList<ItemStack> requiredItems = getRequiredStacks(repairTable);

		if (repairTable.getStackInSlot(0) == null)
		{
			return false;
		}

		if (!repairTable.getStackInSlot(0).isItemStackDamageable())
		{
			return false;
		}

		for (ItemStack needed : requiredItems)
		{
			boolean foundIt = false;
			for (int j = 1; j < repairTable.getSizeInventory(); ++j)
			{
				if (repairTable.getStackInSlot(j) != null)
				{
					ItemStack tested = repairTable.getStackInSlot(j);
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

		return true;
	}

	public static int getNeededLevels(InventoryInfusionRepair repairTable)
	{
		ItemStack infusedItem = repairTable.getStackInSlot(0);

		if (infusedItem == null)
		{
			return 0;
		}

		if (infusedItem.getItem() instanceof ItemBow)
		{
			return 10;
		}

		if (infusedItem.getItem() == RegisterItems.noobWoodSword)
		{
			return 1;
		}
		else if (infusedItem.getItem() == RegisterItems.flintKnife || infusedItem
				.getItem() == RegisterItems.flintHatchet)
		{
			return 1;
		}
		else if (infusedItem.getItem() == RegisterItems.boneKnife)
		{
			return 5;
		}
		else if (infusedItem.getItem() == RegisterItems.bonePickaxe)
		{
			return 1;
		}
		else if (infusedItem.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool)infusedItem.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(tool.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					return 1;
				case STONE:
					return 5;
				case IRON:
					return 10;
				case GOLD:
					return 20;
				case EMERALD: // See "WTF" below.
					return 20;
				default:
					return 20;
			}
		}
		else if (infusedItem.getItem() instanceof ItemSword)
		{
			ItemSword sword = (ItemSword)infusedItem.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(sword.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					return 1;
				case STONE:
					return 5;
				case IRON:
					return 10;
				case GOLD:
					return 20;
				case EMERALD: // See "WTF" below.
					return 20;
				default:
					if (material == RegisterItems.noobWood)
					{
						return 1;
					}
					else
					{
						return 20;
					}
			}
		}
		else if (infusedItem.getItem() instanceof ItemArmor)
		{
			ItemArmor armor = (ItemArmor)infusedItem.getItem();

			switch (armor.getArmorMaterial())
			{
				case CLOTH:
					return 1;
				case CHAIN:
					return 5;
				case IRON:
					return 10;
				case GOLD:
					return 20;
				case DIAMOND: // WTF MOJANG?! (See above)
					return 20;
				default:
					return 20;
			}
		}
		else if (infusedItem.getItem() == Items.shears)
		{
			return 5;
		}
		else if (infusedItem.getItem() == Items.fishing_rod)
		{
			return 5;
		}
		else if (infusedItem.getItem() == Items.flint_and_steel)
		{
			return 1;
		}

		return 0;
	}

	public static ArrayList<ItemStack> getRequiredStacks(InventoryInfusionRepair repairTable)
	{
		ArrayList<ItemStack> requiredItems = new ArrayList<ItemStack>();

		ItemStack repaired = repairTable.getStackInSlot(0);

		if (repaired == null)
		{
			return requiredItems;
		}

		if (!repaired.isItemDamaged())
		{
			return requiredItems;
		}

		if (repaired.getItem() instanceof ItemBow)
		{
			requiredItems.add(new ItemStack(Items.stick, 2));
			requiredItems.add(new ItemStack(Items.string, 2));
		}

		if (repaired.getItem() == RegisterItems.noobWoodSword)
		{
			requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
		}
		else if (repaired.getItem() == RegisterItems.flintKnife || repaired.getItem() == RegisterItems.flintHatchet)
		{
			requiredItems.add(new ItemStack(Items.flint));
		}
		else if (repaired.getItem() == RegisterItems.boneKnife)
		{
			requiredItems.add(new ItemStack(Items.bone));
		}
		else if (repaired.getItem() == RegisterItems.bonePickaxe)
		{
			requiredItems.add(new ItemStack(RegisterItems.boneShard));
		}
		else if (repaired.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool)repaired.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(tool.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
					break;
				case STONE:
					requiredItems.add(new ItemStack(Blocks.stone, 1));
					break;
				case IRON:
					requiredItems.add(new ItemStack(Items.iron_ingot));
					break;
				case GOLD:
					requiredItems.add(new ItemStack(Items.gold_nugget, 2));
					break;
				case EMERALD: // See "WTF" below.
					requiredItems.add(new ItemStack(Items.redstone, 24));
					break;
				default:
					requiredItems.add(new ItemStack(Items.emerald, 4));
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
					break;
				case STONE:
					requiredItems.add(new ItemStack(Blocks.stone, 1));
				case IRON:
					requiredItems.add(new ItemStack(Items.iron_ingot));
					break;
				case GOLD:
					requiredItems.add(new ItemStack(Items.gold_nugget, 2));
					break;
				case EMERALD: // See "WTF" below.
					requiredItems.add(new ItemStack(Items.redstone, 16));
					break;
				default:
					if (material == RegisterItems.noobWood)
					{
						requiredItems.add(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
					}
					else
					{
						requiredItems.add(new ItemStack(Items.emerald, 4));
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
					break;
				case CHAIN:
					requiredItems.add(new ItemStack(RegisterItems.ironNugget));
					break;
				case IRON:
					requiredItems.add(new ItemStack(Items.iron_ingot));
				case GOLD:
					requiredItems.add(new ItemStack(Items.gold_nugget, 2));
				case DIAMOND: // WTF MOJANG?! (See above)
					requiredItems.add(new ItemStack(Items.redstone, 24));
				default:
					requiredItems.add(new ItemStack(Items.emerald, 4));
					break;
			}
		}
		else if (repaired.getItem() == Items.shears)
		{
			requiredItems.add(new ItemStack(Items.iron_ingot));
		}
		else if (repaired.getItem() == Items.fishing_rod)
		{
			requiredItems.add(new ItemStack(Items.string, 4));
		}
		else if (repaired.getItem() == Items.flint_and_steel)
		{
			requiredItems.add(new ItemStack(Items.flint));
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
								requiredItems.add(new ItemStack(RegisterItems.cloth, 8 * level));
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

		return requiredItems;
	}

	public static int getTakenLevels(InventoryInfusionRepair repairTable)
	{
		ItemStack infusedItem = repairTable.getStackInSlot(0);

		if (infusedItem == null)
		{
			return 0;
		}

		if (infusedItem.getItem() == RegisterItems.noobWoodSword)
		{
			return 1;
		}
		else if (infusedItem.getItem() == RegisterItems.flintKnife || infusedItem
				.getItem() == RegisterItems.flintHatchet)
		{
			return 1;
		}
		else if (infusedItem.getItem() == RegisterItems.boneKnife)
		{
			return 2;
		}
		else if (infusedItem.getItem() == RegisterItems.bonePickaxe)
		{
			return 1;
		}
		else if (infusedItem.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool)infusedItem.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(tool.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					return 1;
				case STONE:
					return 5;
				case IRON:
					return 5;
				case GOLD:
					return 3;
				case EMERALD: // See "WTF" below.
					return 8;
				default:
					return 8;
			}
		}
		else if (infusedItem.getItem() instanceof ItemSword)
		{
			ItemSword sword = (ItemSword)infusedItem.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(sword.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					return 1;
				case STONE:
					return 2;
				case IRON:
					return 5;
				case GOLD:
					return 3;
				case EMERALD: // See "WTF" below.
					return 8;
				default:
					if (material == RegisterItems.noobWood)
					{
						return 1;
					}
					else
					{
						return 8;
					}
			}
		}
		else if (infusedItem.getItem() instanceof ItemArmor)
		{
			ItemArmor armor = (ItemArmor)infusedItem.getItem();

			switch (armor.getArmorMaterial())
			{
				case CLOTH:
					return 1;
				case CHAIN:
					return 2;
				case IRON:
					return 5;
				case GOLD:
					return 3;
				case DIAMOND: // WTF MOJANG?! (See above)
					return 8;
				default:
					return 8;
			}
		}
		else if (infusedItem.getItem() == Items.shears)
		{
			return 2;
		}
		else if (infusedItem.getItem() == Items.fishing_rod)
		{
			return 2;
		}
		else if (infusedItem.getItem() == Items.flint_and_steel)
		{
			return 1;
		}

		return 0;
	}
}
