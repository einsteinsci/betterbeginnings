package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.event.ChatUtil;
import net.einsteinsci.noobcraft.register.RegisterBlocks;
import net.einsteinsci.noobcraft.register.achievement.RegisterAchievements;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFireBow extends Item
{
	public ItemFireBow()
	{
		super();
		setUnlocalizedName("fireBow");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		
		setMaxStackSize(1);
		setMaxDamage(8);
		
		setCreativeTab(ModMain.tabNoobCraft);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face,
		float x2, float y2, float z2)
	{
		stack.damageItem(1, player);
		Block block = world.getBlock(x, y, z);
		if (block.getIdFromBlock(block) == RegisterBlocks.campfire.getIdFromBlock(RegisterBlocks.campfire)){
			world.setBlock(x, y, z, RegisterBlocks.campfireLit);
		}
		player.addStat(RegisterAchievements.startFire, 1);
		ChatUtil.sendChatToPlayer(player, "Started Fire.");
		return true;
	}
}


























//
