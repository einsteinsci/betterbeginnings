package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCharredMeat extends ItemFood implements IBBName
{
	public static final int META_MEAT = 0;
	public static final int META_CHICKEN = 1;
	public static final int META_MUTTON = 2;
	public static final int META_RABBIT = 3;
	public static final int META_FISH = 4;
	public static final int META_UNKNOWN = 5;

	public ItemCharredMeat()
	{
		super(5, 2.0f, true);
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if (stack == null)
		{
			return null;
		}

		return "item." + getSimpleUnlocalizedName(stack.getMetadata());
	}

	public static String getSimpleUnlocalizedName(int meta)
	{
		switch (meta)
		{
		case META_MEAT:
			return "charredMeat";
		case META_CHICKEN:
			return "charredChicken";
		case META_MUTTON:
			return "charredMutton";
		case META_RABBIT:
			return "charredRabbit";
		case META_FISH:
			return "charredFish";
		default:
			return "charredUnknown";
		}
	}

	public static int getDamageBasedOnMeat(ItemStack meatStack)
	{
		Item meat = meatStack.getItem();
		if (meat == null)
		{
			return 5; // ???
		}

		if (meat == Items.beef || meat == Items.porkchop ||
			meat == Items.cooked_beef || meat == Items.cooked_porkchop)
		{
			return 0;
		}

		if (meat == Items.chicken || meat == Items.cooked_chicken)
		{
			return 1;
		}

		if (meat == Items.mutton || meat == Items.cooked_mutton)
		{
			return 2;
		}

		if (meat == Items.rabbit || meat == Items.cooked_rabbit)
		{
			return 3;
		}

		if (meat == Items.fish || meat == Items.cooked_fish)
		{
			return 4;
		}

		return 0;
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

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));
		subItems.add(new ItemStack(itemIn, 1, 3));
		subItems.add(new ItemStack(itemIn, 1, 4));

		subItems.add(new ItemStack(itemIn, 1, 5));
	}
}
