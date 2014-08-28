package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

// "That's disgusting!"
public class ItemCharredMeat extends ItemFood
{
	public ItemCharredMeat()
	{
		super(4, 8.0f, true);
		setUnlocalizedName("charredMeat");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		ItemStack result = super.onEaten(stack, world, player);

		RegisterAchievements.achievementGet(player, "charredMeat");

		return result;
	}
}
