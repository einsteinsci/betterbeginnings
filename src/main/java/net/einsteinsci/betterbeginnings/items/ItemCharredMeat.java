package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCharredMeat extends ItemFood implements IBBName
{
	public ItemCharredMeat()
	{
		super(4, 8.0f, true);
		setUnlocalizedName("charredMeat");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		super.onFoodEaten(stack, world, player);

		RegisterAchievements.achievementGet(player, "charredMeat");
	}

	@Override
	public String getName()
	{
		return "charredMeat";
	}
}
