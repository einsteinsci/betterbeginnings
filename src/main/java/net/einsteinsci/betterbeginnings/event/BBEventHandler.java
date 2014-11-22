package net.einsteinsci.betterbeginnings.event;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameData;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.items.ItemHammer;
import net.einsteinsci.betterbeginnings.items.ItemKnife;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.*;

public class BBEventHandler
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e)
	{
		if (BBConfig.greetUser)
		{
			ChatUtil.sendChatToPlayer(e.player, ChatUtil.LIME + "Better Beginnings loaded successfully.");
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

		if (item == RegisterItems.noobWoodSword && EnchantmentHelper.getEnchantments(e.itemStack).isEmpty())
		{
			e.toolTip.add(ChatUtil.BLUE + "+0 Attack Damage");
		}

		if (item == RegisterItems.testItem)
		{
			e.toolTip.add(ChatUtil.PINK + "For dev testing only. What it does changes from one version to the next.");
		}

		if (isWIP(e.itemStack))
		{
			e.toolTip.add(ChatUtil.RED + "WIP. May not be fully functional.");
		}
	}

	public boolean isWIP(ItemStack stack)
	{
		List<ItemStack> wip = new ArrayList<ItemStack>();

		wip.add(new ItemStack(RegisterItems.fireBow));
		wip.add(new ItemStack(RegisterBlocks.campfire));
		wip.add(new ItemStack(RegisterItems.clothBoots));
		wip.add(new ItemStack(RegisterItems.clothPants));
		wip.add(new ItemStack(RegisterItems.clothShirt));
		wip.add(new ItemStack(RegisterItems.clothHat));
		wip.add(new ItemStack(RegisterItems.roastingStick));
		wip.add(new ItemStack(RegisterItems.roastingStickrawMallow));
		wip.add(new ItemStack(RegisterItems.roastingStickcookedMallow));
		wip.add(new ItemStack(RegisterItems.marshmallow));
		wip.add(new ItemStack(RegisterItems.marshmallowCooked));
		wip.add(new ItemStack(RegisterItems.rockHammer));

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
		Block block = e.block;
		EntityPlayer player = e.getPlayer();
		ItemStack heldItemStack = player.getHeldItem();

		handleWrongToolCompat(e, block, player, heldItemStack);
	}

	// Here it is!
	public void handleWrongToolCompat(BlockEvent.BreakEvent e, Block block, EntityPlayer player, ItemStack held)
	{
		boolean correctTool = false;
		boolean usedFace = false;
		if (held == null)
		{
			usedFace = true;
		}
		else
		{
			if (!held.isItemStackDamageable())
			{
				usedFace = true;
			}
			correctTool = ForgeHooks.isToolEffective(held, block, e.blockMetadata);
		}

		String intended = block.getHarvestTool(e.blockMetadata);

		if (held != null)
		{
			String name = Item.itemRegistry.getNameForObject(held.getItem());
			if (held.getItem() instanceof ItemHammer ||
					intended == "axe" && BBConfig.alsoAxes.contains(name) ||
					intended == "pickaxe" && BBConfig.alsoPickaxes.contains(name) ||
					intended == "knife" && BBConfig.alsoKnives.contains(name))
			{
				correctTool = true;
			}
		}

		if (block.getUnlocalizedName() == Blocks.snow.getUnlocalizedName() ||
				block.getUnlocalizedName() == Blocks.snow_layer.getUnlocalizedName())
		{
			intended = null;
		}

		if (intended == null || intended == "shovel" ||
				player.capabilities.isCreativeMode || shouldBeNull(block))
		{
			correctTool = true;
		}

		if (shouldBePickaxe(block))
		{
			if (held != null)
			{
				if (held.getItem() instanceof ItemTool)
				{
					ItemTool tool = (ItemTool)held.getItem();
					if (!tool.getToolClasses(held).contains("pickaxe"))
					{
						correctTool = false;
					}
				}
				else
				{
					correctTool = false;
				}
			}
			else
			{
				correctTool = false;
			}
		}

		if (!correctTool)
		{
			e.setCanceled(true);
			if (usedFace)
			{
				// What do you think the player's 'durability' is?
				player.attackEntityFrom(new DamageSourceFace(block), 4);
				ChatUtil.sendChatToPlayer(player, ChatUtil.ORANGE +
						"[Better Beginnings]" + (player.getEntityWorld().difficultySetting == EnumDifficulty.PEACEFUL ?
						" Ooof! " : " Ouch! ") + "What did you think punching that " +
						ChatUtil.ORANGE + "would do?"); // Redo color because...glitches
			}
			else
			{
				ChatUtil.sendChatToPlayer(player, ChatUtil.ORANGE + "[Better Beginnings] Wrong tool!");
			}
		}
	}

	private boolean shouldBeNull(Block block)
	{
		String blockName = Block.blockRegistry.getNameForObject(block);

		if (BBConfig.alwaysBreakable.contains(blockName))
		{
			return true;
		}

		List<Block> should = new ArrayList<Block>();

		should.add(Blocks.pumpkin);
		should.add(Blocks.lit_pumpkin);
		should.add(Blocks.melon_block);
		should.add(Blocks.ice);
		should.add(Blocks.snow);
		should.add(Blocks.snow_layer);

		for (String s : BBConfig.alwaysBreakable)
		{
			Block always = GameData.getBlockRegistry().getObject(s);
			should.add(always);
		}

		return should.contains(block);
	}

	private boolean shouldBePickaxe(Block block)
	{
		List<Block> should = new ArrayList<Block>();

		should.add(Blocks.coal_block);
		should.add(Blocks.redstone_block);
		should.add(Blocks.stained_hardened_clay);
		should.add(Blocks.hardened_clay);
		should.add(Blocks.quartz_block);
		should.add(Blocks.brick_block);
		should.add(Blocks.packed_ice);
		should.add(Blocks.furnace);
		should.add(Blocks.lit_furnace);

		should.add(RegisterBlocks.kiln);
		should.add(RegisterBlocks.kilnLit);
		should.add(RegisterBlocks.brickOven);
		should.add(RegisterBlocks.brickOvenLit);
		should.add(RegisterBlocks.smelter);
		should.add(RegisterBlocks.smelterLit);
		should.add(RegisterBlocks.obsidianKiln);
		should.add(RegisterBlocks.obsidianKilnLit);
		should.add(RegisterBlocks.netherBrickOven);
		should.add(RegisterBlocks.netherBrickOvenLit);
		should.add(RegisterBlocks.enderSmelter);
		should.add(RegisterBlocks.enderSmelterLit);
		should.add(RegisterBlocks.infusionRepairStation);

		return should.contains(block);
	}

	@SubscribeEvent
	public void onBlockDrops(BlockEvent.HarvestDropsEvent e)
	{
		Block block = e.block;
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
						e.drops.add(new ItemStack(block, 1, e.blockMetadata));
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
		// To fix duplication glitch #27
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
			if (!BBConfig.moreBonesPeacefulOnly || e.entity.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
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






















// BUFFER
