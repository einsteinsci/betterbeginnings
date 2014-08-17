package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.ItemSword;

//Replaces Vanilla Wood Sword
public class NoobWoodSword extends ItemSword
{
	public NoobWoodSword(ToolMaterial material)
	{
		super(material);
		setUnlocalizedName("noobWoodSword");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		//setMaxDamage(60);
		//setCreativeTab(CreativeTabs.tabCombat);
	}

	//@Override
	//public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase holder)
	//{
	//    stack.damageItem(1, holder);
	//    return true;
	//}
	//
	//@Override
	//public boolean onBlockDestroyed(ItemStack stack, World world, Block block,
	//		int xPos, int yPos, int zPos, EntityLivingBase holder)
	//{
	//    if ((double)block.getBlockHardness(world, xPos, yPos, zPos) != 0.0D)
	//    {
	//        stack.damageItem(2, holder);
	//    }
	//
	//    return true;
	//}
	//
	//@Override
	//@SideOnly(Side.CLIENT)
	//public boolean isFull3D()
	//{
	//    return true;
	//}
	//
	//@Override
	//public EnumAction getItemUseAction(ItemStack stack)
	//{
	//    return EnumAction.block;
	//}
	//
	//@Override
	//public int getMaxItemUseDuration(ItemStack stack)
	//{
	//	return 72000;
	//}
	//
	//@Override
	//public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	//{
	//    player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
	//    return stack;
	//}
	//
	//@Override
	////Super-awesome enchantability
	//public int getItemEnchantability()
	//{
	//	return 35;
	//}
}
