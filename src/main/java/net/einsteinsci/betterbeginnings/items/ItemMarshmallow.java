package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemFood;

public class ItemMarshmallow extends ItemFood implements IBBName
{
	public ItemMarshmallow()
	{
		super(1, 2.0f, false);
		setUnlocalizedName("marshmallow");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "marshmallow";
	}
}
