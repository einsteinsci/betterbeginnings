package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.ItemPickaxe;

public class ItemBonePickaxe extends ItemPickaxe
{
	public ItemBonePickaxe()
	{
		super(ToolMaterial.WOOD);
		
		setUnlocalizedName("bonePickaxe");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
