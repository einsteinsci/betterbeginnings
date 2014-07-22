package net.einsteinsci.noobcraft.event;

import net.einsteinsci.noobcraft.config.NoobcraftConfig;
import net.einsteinsci.noobcraft.items.ItemKnife;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class NoobcraftEventHandler
{
	public static void sendChatToPlayer(EntityPlayer player, String message)
	{
		player.addChatMessage(new ChatComponentText(message));
	}
	
	@SubscribeEvent
	public void login(PlayerEvent.PlayerLoggedInEvent e)
	{
		if (NoobcraftConfig.greetUser)
		{
			sendChatToPlayer(e.player, "HELLO!");
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e)
	{
		
	}
	
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent e)
	{
		Block block = e.block;
		EntityPlayer player = e.getPlayer();
		ItemStack heldItemStack = player.getHeldItem();
		
		handleWrongTool(e, block, player, heldItemStack);
	}
	
	public void handleWrongTool(BlockEvent.BreakEvent e, Block block,
		EntityPlayer player, ItemStack heldItemStack) {
		Item heldItem = null;
		
		String requiredToolClass = block.getHarvestTool(e.blockMetadata);
		String toolUsed = "face";
		
		//Blocks that should be "pickaxe" but are actually null
		if (block == Blocks.coal_block ||
			block == Blocks.redstone_block ||
			block == Blocks.stained_hardened_clay ||
			block == Blocks.hardened_clay ||
			block == Blocks.quartz_block ||
			block == Blocks.brick_block ||
			block == Blocks.ice || 			//not sure if this one is null already
			block == Blocks.packed_ice)
		{
			requiredToolClass = "pickaxe";
		}
		
		//Blocks that should be breakable regardless of tool
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
			
			//if (toolClass.equalsIgnoreCase("null"))
			if (requiredToolClass == null || requiredToolClass == "shovel")
			{
				//Nobody cares. It's a shovel.
				wrongTool = false;
			}
			
			if (wrongTool)
			{
				e.setCanceled(true);
				sendChatToPlayer(player, "[NoobCraft] Wrong tool!");
				
				if (toolUsed.equalsIgnoreCase("face"))
				{
					//What do you think the player's 'durability' is?
					player.attackEntityFrom(DamageSource.cactus, 2);
					sendChatToPlayer(player, "[NoobCraft] Ouch! What do you think punching that would do?");
				}
			}
		}
	}
}






















//BUFFER
