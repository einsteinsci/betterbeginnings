package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by einsteinsci on 8/10/2014.
 */
public class ItemClothArmor extends ItemArmor
{
	public ItemClothArmor(ArmorMaterial material, int armorIndex, String name)
	{
		super(material, 0, armorIndex);

		setUnlocalizedName(name);
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
