package net.einsteinsci.betterbeginnings.util;

import net.einsteinsci.betterbeginnings.config.json.RepairInfusionConfig;
import net.einsteinsci.betterbeginnings.inventory.InventoryInfusionRepair;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfusionRepairUtil
{
	public static Map<Integer, OreRecipeElement> enchantmentMapping = new HashMap<>();

	public static void registerVanillaEnchantsConfig()
	{
		RepairInfusionConfig.registerEnchantment(0, new OreRecipeElement(Blocks.iron_bars, 2));
		RepairInfusionConfig.registerEnchantment(1, new OreRecipeElement(Items.bucket));
		RepairInfusionConfig.registerEnchantment(2, new OreRecipeElement(Items.feather, 2));
		RepairInfusionConfig.registerEnchantment(3, new OreRecipeElement("cobblestone", 8));
		RepairInfusionConfig.registerEnchantment(4, new OreRecipeElement(Items.snowball, 4));
		RepairInfusionConfig.registerEnchantment(5, new OreRecipeElement(Items.glass_bottle));
		RepairInfusionConfig.registerEnchantment(6, new OreRecipeElement(Blocks.clay));
		RepairInfusionConfig.registerEnchantment(7, new OreRecipeElement(Blocks.cactus, 4));
		RepairInfusionConfig.registerEnchantment(8, new OreRecipeElement(Blocks.prismarine));

		RepairInfusionConfig.registerEnchantment(16, new OreRecipeElement("gemQuartz", 4));
		RepairInfusionConfig.registerEnchantment(17, new OreRecipeElement(Blocks.soul_sand, 2));
		RepairInfusionConfig.registerEnchantment(18, new OreRecipeElement(Items.fermented_spider_eye));
		RepairInfusionConfig.registerEnchantment(19, new OreRecipeElement(Blocks.piston));
		RepairInfusionConfig.registerEnchantment(20, new OreRecipeElement(Items.blaze_powder, 2));
		RepairInfusionConfig.registerEnchantment(21, new OreRecipeElement("ingotGold", 2));

		RepairInfusionConfig.registerEnchantment(32, new OreRecipeElement(Items.sugar, 4));
		RepairInfusionConfig.registerEnchantment(33, new OreRecipeElement(RegisterItems.cloth, 8));
		RepairInfusionConfig.registerEnchantment(34, new OreRecipeElement(Blocks.obsidian));
		RepairInfusionConfig.registerEnchantment(35, new OreRecipeElement("gemLapis", 4));

		RepairInfusionConfig.registerEnchantment(48, new OreRecipeElement(RegisterItems.leatherStrip));
		RepairInfusionConfig.registerEnchantment(49, new OreRecipeElement(Items.gunpowder));
		RepairInfusionConfig.registerEnchantment(50, new OreRecipeElement(Items.fire_charge));
		RepairInfusionConfig.registerEnchantment(51, new OreRecipeElement(Items.arrow, 16));

		RepairInfusionConfig.registerEnchantment(61, new OreRecipeElement(Blocks.waterlily));
		RepairInfusionConfig.registerEnchantment(62, new OreRecipeElement(Items.fish, 1, 3));
	}

	public static void registerEnchantment(int enchID, OreRecipeElement stack)
	{
		enchantmentMapping.put(enchID, stack);
	}

	public static boolean canRepair(InventoryInfusionRepair repairTable, EntityPlayer player)
	{
		return canRepairIgnoreXp(repairTable) && player.experienceLevel >= getNeededLevels(repairTable);
	}

	public static boolean canRepairIgnoreXp(InventoryInfusionRepair repairTable)
	{
		ArrayList<OreRecipeElement> requiredItems = getRequiredStacks(repairTable);

		if (repairTable.getStackInSlot(0) == null)
		{
			return false;
		}

		if (!repairTable.getStackInSlot(0).isItemStackDamageable())
		{
			return false;
		}

		for (OreRecipeElement needed : requiredItems)
		{
			boolean foundIt = false;
			for (int j = 1; j < repairTable.getSizeInventory(); ++j)
			{
				if (repairTable.getStackInSlot(j) != null)
				{
					ItemStack tested = repairTable.getStackInSlot(j);
					if (needed.matches(tested))
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
		return getNeededLevels(repairTable.getStackInSlot(0));
	}

	public static int getNeededLevels(ItemStack infusedItem)
	{
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
					if (material == RegisterItems.NOOBWOOD)
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
				case LEATHER:
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

	public static ArrayList<OreRecipeElement> getRequiredStacks(InventoryInfusionRepair repairTable)
	{
		return getRequiredStacks(repairTable.getStackInSlot(0));
	}

	public static ArrayList<OreRecipeElement> getRequiredStacks(ItemStack repaired)
	{
		ArrayList<OreRecipeElement> requiredItems = new ArrayList<>();

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
			requiredItems.add(new OreRecipeElement("stickWood", 2));
			requiredItems.add(new OreRecipeElement("itemString", 2));
		}

		if (repaired.getItem() == RegisterItems.noobWoodSword)
		{
			requiredItems.add(new OreRecipeElement("plankWood"));
		}
		else if (repaired.getItem() == RegisterItems.flintKnife || repaired.getItem() == RegisterItems.flintHatchet)
		{
			requiredItems.add(new OreRecipeElement(Items.flint));
		}
		else if (repaired.getItem() == RegisterItems.boneKnife)
		{
			requiredItems.add(new OreRecipeElement(Items.bone));
		}
		else if (repaired.getItem() == RegisterItems.bonePickaxe)
		{
			requiredItems.add(new OreRecipeElement(RegisterItems.boneShard));
		}
		else if (repaired.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool)repaired.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(tool.getToolMaterialName());

			switch (material)
			{
				case WOOD:
					requiredItems.add(new OreRecipeElement("plankWood"));
					break;
				case STONE:
					requiredItems.add(new OreRecipeElement("stone"));
					break;
				case IRON:
					requiredItems.add(new OreRecipeElement("ingotIron"));
					break;
				case GOLD:
					requiredItems.add(new OreRecipeElement("nuggetGold", 2));
					break;
				case EMERALD: // See "WTF" below.
					requiredItems.add(new OreRecipeElement("dustRedstone", 24));
					break;
				default:
					requiredItems.add(new OreRecipeElement("gemEmerald", 4));
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
					requiredItems.add(new OreRecipeElement("plankWood"));
					break;
				case STONE:
					requiredItems.add(new OreRecipeElement("stone"));
				case IRON:
					requiredItems.add(new OreRecipeElement("ingotIron"));
					break;
				case GOLD:
					requiredItems.add(new OreRecipeElement("nuggetGold", 2));
					break;
				case EMERALD: // See "WTF" below.
					requiredItems.add(new OreRecipeElement("dustRedstone", 16));
					break;
				default:
					if (material == RegisterItems.NOOBWOOD)
					{
						requiredItems.add(new OreRecipeElement("plankWood"));
					}
					else
					{
						requiredItems.add(new OreRecipeElement("gemEmerald", 4));
					}
					break;
			}
		}
		else if (repaired.getItem() instanceof ItemArmor)
		{
			ItemArmor armor = (ItemArmor)repaired.getItem();

			switch (armor.getArmorMaterial())
			{
				case LEATHER:
					requiredItems.add(new OreRecipeElement(Items.leather));
					break;
				case CHAIN:
					requiredItems.add(new OreRecipeElement("nuggetIron", 6));
					break;
				case IRON:
					requiredItems.add(new OreRecipeElement("ingotIron", 3));
					break;
				case GOLD:
					requiredItems.add(new OreRecipeElement("nuggetGold", 2));
					break;
				case DIAMOND: // WTF MOJANG?! (See above)
					requiredItems.add(new OreRecipeElement("dustRedstone", 24));
					break;
				default:
					requiredItems.add(new OreRecipeElement("gemEmerald", 4));
					break;
			}
		}
		else if (repaired.getItem() == Items.shears)
		{
			requiredItems.add(new OreRecipeElement("ingotIron"));
		}
		else if (repaired.getItem() == Items.fishing_rod)
		{
			requiredItems.add(new OreRecipeElement("itemString", 4));
		}
		else if (repaired.getItem() == Items.flint_and_steel)
		{
			requiredItems.add(new OreRecipeElement(Items.flint));
		}

		if (repaired.isItemEnchanted())
		{
			if (repaired.getTagCompound() != null)
			{
				if (repaired.getTagCompound().getTag("ench") != null)
				{
					NBTTagList enchants = (NBTTagList)repaired.getTagCompound().getTag("ench");
					requiredItems.addAll(getEnchantmentItems(enchants));
				}
			}
		}

		return requiredItems;
	}

	public static List<OreRecipeElement> getEnchantmentItems(NBTTagList enchants)
	{
		List<OreRecipeElement> requiredItems = new ArrayList<>();

		for (int i = 0; i < enchants.tagCount(); i++)
		{
			NBTTagCompound enchant = enchants.getCompoundTagAt(i);
			int enchId = enchant.getInteger("id");
			int level = enchant.getInteger("lvl");

			OreRecipeElement associated = enchantmentMapping.get(enchId);

			if (associated != null)
			{
				OreRecipeElement req = associated.copy();
				req.stackSize *= level;
				requiredItems.add(req);
			}
			else
			{
				//LogUtil.log(Level.ERROR, "No repair infusion item associated with enchantment #" + enchId); // This spams the console
			}
		}

		return requiredItems;
	}

	public static boolean isToolValidForDiffusion(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}

		if (!stack.isItemEnchanted())
		{
			return false;
		}

		if (stack.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool)stack.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(tool.getToolMaterialName());

			return material == Item.ToolMaterial.EMERALD || material == Item.ToolMaterial.GOLD;
		}

		if (stack.getItem() instanceof ItemSword)
		{
			ItemSword sword = (ItemSword)stack.getItem();
			Item.ToolMaterial material = Item.ToolMaterial.valueOf(sword.getToolMaterialName());

			return material == Item.ToolMaterial.EMERALD || material == Item.ToolMaterial.GOLD;
		}

		if (stack.getItem() instanceof ItemArmor)
		{
			ItemArmor armor = (ItemArmor)stack.getItem();
			ItemArmor.ArmorMaterial material = armor.getArmorMaterial();

			return material == ItemArmor.ArmorMaterial.DIAMOND || material == ItemArmor.ArmorMaterial.GOLD;
		}

		return false;
	}

	public static int getTakenLevels(InventoryInfusionRepair repairTable)
	{
		return getTakenLevels(repairTable.getStackInSlot(0));
	}

	public static int getTakenLevels(ItemStack infusedItem)
	{
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
					return 2;
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
					if (material == RegisterItems.NOOBWOOD)
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
				case LEATHER:
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
		else if (infusedItem.getItem() == Items.bow)
		{
			return  5;
		}

		return 0;
	}
}
