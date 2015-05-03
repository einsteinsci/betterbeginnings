package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;

public class ItemPan extends Item implements IBBName
{
	public ItemPan()
	{
		super();
		setUnlocalizedName("pan");
		setMaxDamage(250);
		setMaxStackSize(1);
		//setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "pan";
	}
}
