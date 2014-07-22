package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.item.ItemPickaxe;

public class ItemBonePickaxe extends ItemPickaxe
{
	public ItemBonePickaxe()
	{
		super(ToolMaterial.WOOD);
		
		setUnlocalizedName("bonePickaxe");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
}
