package net.einsteinsci.betterbeginnings.event;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.items.ItemHammer;
import net.einsteinsci.betterbeginnings.items.ItemKnife;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class BBEventHandler
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e)
	{
		if (BBConfig.greetUser)
		{
			ChatUtil.sendModChatToPlayer(e.player, ChatUtil.LIME + "Better Beginnings " + ModMain.VERSION +
				" loaded successfully.");
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e)
	{
		if (e.modID.equals(ModMain.MODID))
		{
			BBConfig.syncConfig(ModMain.configFile);
		}
	}
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent e)
	{
		Item item = e.itemStack.getItem();
		
		if (item == RegisterItems.charredMeat)
		{
			e.toolTip.add("Not to be confused with charcoal");
		}
		
		if (item == RegisterItems.ironNugget)
		{
			e.toolTip.add("Good for hinges and rivets");
		}
		
		if (item == RegisterItems.flintKnife)
		{
			e.toolTip.add("Don't bring it to a gunfight");
		}

		if (item == RegisterItems.infusionScroll)
		{
			e.toolTip.add("Allows you to decipher tool infusion.");
		}

		if (item == RegisterItems.noobWoodSword && EnchantmentHelper.getEnchantments(e.itemStack).isEmpty())
		{
			e.toolTip.add(ChatUtil.BLUE + "+0 Attack Damage");
		}

		if (item == RegisterItems.testItem)
		{
			e.toolTip.add(ChatUtil.PINK + "For dev testing only. What it does");
			e.toolTip.add(ChatUtil.PINK + "changes from one version to the next.");
		}

		if (item == RegisterItems.pan)
		{
			e.toolTip.add(ChatUtil.BLUE + "Fry stuff over a campfire!");
		}

		if (isWIP(e.itemStack))
		{
			e.toolTip.add(ChatUtil.RED + "WIP. May not be fully functional.");
		}
	}

	public boolean isWIP(ItemStack stack)
	{
		List<ItemStack> wip = new ArrayList<>();

		wip.add(new ItemStack(RegisterItems.clothBoots));
		wip.add(new ItemStack(RegisterItems.clothPants));
		wip.add(new ItemStack(RegisterItems.clothShirt));
		wip.add(new ItemStack(RegisterItems.clothHat));
		wip.add(new ItemStack(RegisterItems.roastingStick));
		wip.add(new ItemStack(RegisterItems.roastingStickRawMallow));
		wip.add(new ItemStack(RegisterItems.roastingStickCookedMallow));
		wip.add(new ItemStack(RegisterItems.fireBow));
		wip.add(new ItemStack(RegisterBlocks.campfire));

		for (ItemStack test : wip)
		{
			if (stack.getItem() == test.getItem())
			{
				return true;
			}
		}

		return false;
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e)
	{
		// Let's leave this to the professionals
		BlockBreakHelper.handleBlockBreaking(e);
	}

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent e)
	{
		if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (e.entityPlayer.getHeldItem() != null)
			{
				ItemStack stack = e.entityPlayer.getHeldItem();
				Item item = stack.getItem();

				if (item == Items.flint_and_steel || item == RegisterItems.fireBow)
				{
					Block b = e.world.getBlockState(e.pos).getBlock();

					if (b == RegisterBlocks.campfire || b == RegisterBlocks.campfireLit)
					{
						TileEntityCampfire campfire = (TileEntityCampfire)e.world.getTileEntity(e.pos);

						campfire.lightFuel(); // Light it.
						e.entityPlayer.getHeldItem().damageItem(1, e.entityPlayer);
						if (item == Items.flint_and_steel)
						{
							e.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockDrops(BlockEvent.HarvestDropsEvent e)
	{
		Block block = e.state.getBlock();
		EntityPlayer player = e.harvester;

		// All onBlockDrops activity that does not have to do with players must
		// occur before here.
		if (player == null)
		{
			return;
		}

		ItemStack held = player.getHeldItem();
		Random rand = player.worldObj.rand;

		// Knife silk-touching for vines
		if (block == Blocks.vine && !e.isSilkTouching)
		{
			if (held != null)
			{
				if (held.getItem() instanceof ItemKnife)
				{
					if (rand.nextInt(8) == 0)
					{
						e.drops.add(new ItemStack(Blocks.vine));
					}
				}
			}
		}

		// Knife silk-touching for grass/bushes
		if ((block == Blocks.tallgrass || block == Blocks.deadbush) && !e.isSilkTouching)
		{
			if (held != null)
			{
				if (held.getItem() instanceof ItemKnife)
				{
					if (rand.nextInt(8) == 0)
					{
						int meta = block.getMetaFromState(e.state);
						e.drops.add(new ItemStack(block, 1, meta));
					}
				}
			}
		}

		// Hammer
		if (held != null)
		{
			if (held.getItem() instanceof ItemHammer)
			{
				ItemStack crushResult = ItemHammer.getCrushResult(block);
				if (crushResult != null)
				{
					e.drops.clear();
					e.drops.add(crushResult);
				}
			}
		}

		// Makes sure emergency escape mechanic does not let blocks fall out (like logs)
		ItemStack heldItemStack = player.getHeldItem();

		int neededHarvestLevel = block.getHarvestLevel(e.state);
		String neededToolClass = block.getHarvestTool(e.state);
		int usedHarvestLevel = 0;
		String usedToolClass = null;
		if (heldItemStack != null)
		{
			for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack))
			{
				int hl = heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass);
				if (hl >= usedHarvestLevel)
				{
					usedHarvestLevel = hl;
					usedToolClass = toolClass;
				}
			}
		}

		if (neededToolClass == null || neededToolClass.equalsIgnoreCase("shovel") ||
				neededToolClass.equalsIgnoreCase("null"))
		{
			return;
		}

		if (usedToolClass == null || !usedToolClass.equalsIgnoreCase(neededToolClass) ||
				usedHarvestLevel < neededHarvestLevel)
		{
			e.drops.clear();
		}
	}

	@SubscribeEvent
	public void onItemSmelted(PlayerEvent.ItemSmeltedEvent e)
	{
		if (e.smelting.getItem() == Items.golden_apple && e.smelting.getItemDamage() == 1)
		{
			RegisterAchievements.achievementGet(e.player, "notchApple");
		}
	}
	
	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent e)
	{
		// To fix duplication glitch (#27)
		if (e.crafting.getItem() instanceof ItemKnife)
		{
			for (int i = 0; i < e.craftMatrix.getSizeInventory(); i++)
			{
				ItemStack stack = e.craftMatrix.getStackInSlot(i);
				if (stack != null)
				{
					if (stack.getItem() instanceof ItemKnife)
					{
						--stack.stackSize;

						if (stack.stackSize <= 0)
						{
							e.craftMatrix.setInventorySlotContents(i, null);
						}
						else
						{
							e.craftMatrix.setInventorySlotContents(i, stack);
						}
					}
				}
			}
		}

		if (e.crafting.getItem() == RegisterItems.flintKnife)
		{
			RegisterAchievements.achievementGet(e.player, "flintKnife");
		}
		else if (e.crafting.getItem() instanceof ItemKnife)
		{
			RegisterAchievements.achievementGet(e.player, "upgradeKnife");
		}

		if (e.crafting.getItem() == Items.string)
		{
			RegisterAchievements.achievementGet(e.player, "makeString");
		}

		if (e.crafting.getItem() == RegisterItems.twine)
		{
			RegisterAchievements.achievementGet(e.player, "makeTwine");
		}

		for (int i = 0; i < e.craftMatrix.getSizeInventory(); ++i)
		{
			if (e.craftMatrix.getStackInSlot(i) != null)
			{
				ItemStack current = e.craftMatrix.getStackInSlot(i);
				if (current.getItem() instanceof ItemKnife && e.crafting.getItem() == Items.stick)
				{
					RegisterAchievements.achievementGet(e.player, "makeSticks");
				}
			}
		}

		if (e.crafting.getItem() == RegisterItems.flintHatchet)
		{
			RegisterAchievements.achievementGet(e.player, "makeHatchet");
		}

		if (e.crafting.getItem() instanceof ItemSword)
		{
			RegisterAchievements.achievementGet(e.player, "makeSword");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.infusionRepairStation))
		{
			RegisterAchievements.achievementGet(e.player, "infusionRepair");
		}

		if (e.crafting.getItem() == RegisterItems.boneShard)
		{
			RegisterAchievements.achievementGet(e.player, "boneShards");
		}

		if (e.crafting.getItem() == RegisterItems.bonePickaxe)
		{
			RegisterAchievements.achievementGet(e.player, "bonePick");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.kiln))
		{
			RegisterAchievements.achievementGet(e.player, "makeKiln");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.obsidianKiln))
		{
			RegisterAchievements.achievementGet(e.player, "obsidianKiln");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.smelter))
		{
			RegisterAchievements.achievementGet(e.player, "makeSmelter");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.enderSmelter))
		{
			RegisterAchievements.achievementGet(e.player, "enderSmelter");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.brickOven))
		{
			RegisterAchievements.achievementGet(e.player, "makeBrickOven");
		}

		if (e.crafting.getItem() == Items.cake)
		{
			RegisterAchievements.achievementGet(e.player, "cake");
		}

		if (e.crafting.getItem() == Item.getItemFromBlock(RegisterBlocks.netherBrickOven))
		{
			RegisterAchievements.achievementGet(e.player, "netherBrickOven");
		}
	}

	@SubscribeEvent
	public void onEntityLivingDrops(LivingDropsEvent e)
	{
		Random rand = e.entity.worldObj.rand;

		if (e.entityLiving instanceof EntitySpider) // Includes cave spiders
		{
			if (!BBConfig.spidersDropString)
			{
				Iterator iterator = e.drops.iterator();
				while (iterator.hasNext())
				{
					EntityItem entityItem = (EntityItem)iterator.next();
					Item item = entityItem.getEntityItem().getItem();
					if (item == Items.string)
					{
						iterator.remove();
					}
				}
			}

			int dropCount = rand.nextInt(3 + e.lootingLevel) + 2;
			for (int i = 0; i < dropCount; ++i)
			{
				e.entityLiving.dropItem(RegisterItems.silk, 1);
			}
		}

		if (BBConfig.moreBones)
		{
			if (!BBConfig.moreBonesPeacefulOnly || e.entity.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
			{
				int maxBones = 0;
				int maxShards = 0;

				if (e.entityLiving instanceof EntityCow)
				{
					maxBones = 4;
				}
				if (e.entityLiving instanceof EntitySheep || e.entityLiving instanceof EntityPig)
				{
					maxBones = 3;
				}
				if (e.entityLiving instanceof EntityChicken || e.entityLiving instanceof EntityOcelot)
				{
					maxShards = 3;
				}
				if (e.entityLiving instanceof EntityZombie)
				{
					maxBones = 2;
					maxShards = 3;
				}

				if (maxBones > 0 && e.recentlyHit && !e.entityLiving.isChild())
				{
					int dropCount = rand.nextInt(maxBones + e.lootingLevel);
					for (int i = 0; i < dropCount; ++i)
					{
						e.entityLiving.dropItem(Items.bone, 1);
					}
				}
				if (maxShards > 0 && e.recentlyHit && !e.entityLiving.isChild())
				{
					int dropCount = rand.nextInt(maxShards + e.lootingLevel);
					for (int i = 0; i < dropCount; ++i)
					{
						e.entityLiving.dropItem(RegisterItems.boneShard, 1);
					}
				}
			}

			if (e.entityLiving instanceof EntitySkeleton)
			{
				int dropCount = rand.nextInt(3 + e.lootingLevel);
				e.entityLiving.dropItem(Items.bone, 1);
				e.entityLiving.dropItem(RegisterItems.boneShard, dropCount);
			}
		}

		if (BBConfig.flamingAnimalsDropCharredMeat)
		{
			// Flaming mobs drop charred meat instead of cooked meats
			if (e.entityLiving instanceof EntityCow || e.entityLiving instanceof EntityPig ||
					e.entityLiving instanceof EntityChicken)
			{
				int charredDrops = 0;

				Iterator iterator = e.drops.iterator();
				while (iterator.hasNext())
				{
					EntityItem entityItem = (EntityItem)iterator.next();
					Item item = entityItem.getEntityItem().getItem();
					if (item == Items.cooked_beef || item == Items.cooked_porkchop || item == Items.cooked_chicken)
					{
						iterator.remove();
						charredDrops += entityItem.getEntityItem().stackSize;
					}
				}

				e.entityLiving.dropItem(RegisterItems.charredMeat, charredDrops);
			}
		}
	}
}
