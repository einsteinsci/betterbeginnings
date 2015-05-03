package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemFood;

public class ItemMarshmallowCooked extends ItemFood implements IBBName
{
	public ItemMarshmallowCooked()
	{
		super(5, 6.0f, false);
		setUnlocalizedName("marshmallowCooked");
		setCreativeTab(ModMain.tabBetterBeginnings);
		setPotionEffect(1, 15, 0, 0.8f);
	}

	@Override
	public String getName()
	{
		return "marshmallowCooked";
	}
}
