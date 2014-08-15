package net.einsteinsci.noobcraft.event;

import java.util.Iterator;
import java.util.Random;

import net.einsteinsci.noobcraft.config.NoobcraftConfig;
import net.einsteinsci.noobcraft.items.ItemKnife;
import net.einsteinsci.noobcraft.register.RegisterItems;
import net.einsteinsci.noobcraft.register.achievement.RegisterAchievements;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class NoobcraftEventHandler
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e)
	{
		if (NoobcraftConfig.greetUser)
		{
			ChatUtil.sendChatToPlayer(e.player, ChatUtil.LIME + "NoobCraft loaded successfully.");
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e)
	{
		
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
		
		if (item == RegisterItems.noobWoodSword)
		{
			e.toolTip.add(ChatUtil.BLUE + "+0 Attack Damage");
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e)
	{
		Block block = e.block;
		EntityPlayer player = e.getPlayer();
		ItemStack heldItemStack = player.getHeldItem();
		
		handleWrongTool(e, block, player, heldItemStack);
	}
	
	@SubscribeEvent
	public void onBlockDrops(BlockEvent.HarvestDropsEvent e)
	{
		if (e.block == Blocks.vine && !e.isSilkTouching && e.harvester != null)
		{
			if (e.harvester.getHeldItem() != null)
			{
				if (e.harvester.getHeldItem().getItem() instanceof ItemKnife)
				{
					Random rand = e.harvester.worldObj.rand;
					if (rand.nextInt(8) == 0)
					{
						e.drops.add(new ItemStack(Blocks.vine));
					}
				}
			}
		}
		
		if (e.block == Blocks.tallgrass && !e.isSilkTouching && e.harvester != null)
		{
			if (e.harvester.getHeldItem() != null)
			{
				if (e.harvester.getHeldItem().getItem() instanceof ItemKnife)
				{
					Random rand = e.harvester.worldObj.rand;
					if (rand.nextInt(8) == 0)
					{
						e.drops.add(new ItemStack(Blocks.tallgrass,1,1));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent e)
	{
		if (e.crafting.getItem() == RegisterItems.flintKnife)
		{
			e.player.addStat(RegisterAchievements.flintKnife, 1);
			ChatUtil.sendChatToPlayer(e.player, "Flint Knife crafted.");
		}
		
		for (int i = 0; i < e.craftMatrix.getSizeInventory(); ++i)
		{
			if (e.craftMatrix.getStackInSlot(i) != null)
			{
				ItemStack current = e.craftMatrix.getStackInSlot(i);
				if (current.getItem() instanceof ItemKnife && e.crafting.getItem() == Items.stick)
				{
					e.player.addStat(RegisterAchievements.makeSticks, 1);
					ChatUtil.sendChatToPlayer(e.player, "Sticks made with knife.");
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityLivingDrops(LivingDropsEvent e)
	{
		Random rand = e.entity.worldObj.rand;
		
		if (NoobcraftConfig.moreBones)
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
			
			if (e.entityLiving instanceof EntitySkeleton)
			{
				int dropCount = rand.nextInt(3 + e.lootingLevel);
				e.entityLiving.dropItem(Items.bone, 1);
				e.entityLiving.dropItem(RegisterItems.boneShard, dropCount);
			}
		}
		
		if (NoobcraftConfig.flamingAnimalsDropCharredMeat)
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
	
	public void handleWrongTool(BlockEvent.BreakEvent e, Block block, EntityPlayer player, ItemStack heldItemStack)
	{
		Item heldItem = null;
		
		String requiredToolClass = block.getHarvestTool(e.blockMetadata);
		String toolUsed = "face";
		
		// Blocks that should be "pickaxe" but are actually null
		if (block == Blocks.coal_block || block == Blocks.redstone_block || block == Blocks.stained_hardened_clay ||
			block == Blocks.hardened_clay || block == Blocks.quartz_block || block == Blocks.brick_block ||
			block == Blocks.ice || 			// not sure if this one is null already
			block == Blocks.packed_ice)
		{
			requiredToolClass = "pickaxe";
		}
		
		// Blocks that should be breakable regardless of tool
		if (block.getUnlocalizedName() == Blocks.snow.getUnlocalizedName() ||
			block.getUnlocalizedName() == Blocks.snow_layer.getUnlocalizedName())
		{
			requiredToolClass = null;
		}
		
		if (!player.capabilities.isCreativeMode)
		{
			boolean wrongTool = false;
			
			if (heldItemStack != null && requiredToolClass != null)
			{
				heldItem = heldItemStack.getItem();
				if (heldItem instanceof ItemAxe)
				{
					if (!requiredToolClass.equalsIgnoreCase("axe"))
					{
						wrongTool = true;
					}
					toolUsed = "axe";
				}
				else if (heldItem instanceof ItemPickaxe)
				{
					if (!requiredToolClass.equalsIgnoreCase("pickaxe"))
					{
						wrongTool = true;
					}
					toolUsed = "pickaxe";
				}
				else if (heldItem instanceof ItemSpade)
				{
					if (!requiredToolClass.equalsIgnoreCase("shovel"))
					{
						wrongTool = true;
					}
					toolUsed = "shovel";
				}
				else if (heldItem instanceof ItemKnife)
				{
					ItemKnife knife = (ItemKnife)heldItem;
					if (!ItemKnife.GetBreakable().contains(block))
					{
						wrongTool = true;
					}
					toolUsed = "knife";
				}
				else if (!(heldItem instanceof ItemTool))
				{
					wrongTool = true;
				}
			}
			else
			{
				wrongTool = true;
			}
			
			// if (toolClass.equalsIgnoreCase("null"))
			if (requiredToolClass == null || requiredToolClass == "shovel")
			{
				// Nobody cares. It's a shovel.
				wrongTool = false;
			}
			
			if (wrongTool)
			{
				e.setCanceled(true);
				if (toolUsed.equalsIgnoreCase("face"))
				{
					// What do you think the player's 'durability' is?
					player.attackEntityFrom(DamageSource.cactus, 2);
					ChatUtil.sendChatToPlayer(player, ChatUtil.ORANGE +
						"[NoobCraft] Ouch! What did you think punching that would do?");
				}
				else
				{
					ChatUtil.sendChatToPlayer(player, ChatUtil.ORANGE + "[NoobCraft] Wrong tool!");
				}
			}
		}
	}
}






















// BUFFER
