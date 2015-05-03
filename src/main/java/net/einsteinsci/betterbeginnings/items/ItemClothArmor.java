package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemArmor;

public class ItemClothArmor extends ItemArmor implements IBBName
{
	public ItemClothArmor(ArmorMaterial material, int armorIndex, String name)
	{
		super(material, 0, armorIndex);

		setUnlocalizedName(name);
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "clothArmor_" + armorType;
	}
}
