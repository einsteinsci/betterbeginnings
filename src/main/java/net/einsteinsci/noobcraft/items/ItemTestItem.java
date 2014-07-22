package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class ItemTestItem extends Item 
{
	public ItemTestItem()
	{
		super();
		setUnlocalizedName("testItem");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, 
			int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		Block tested = world.getBlock(x, y, z);
		String blockName = tested.getUnlocalizedName().substring(5);
		ChatComponentTranslation trans = new ChatComponentTranslation(blockName);
		trans.appendText(" : " + world.getBlockMetadata(x, y, z));
		player.addChatMessage(trans);
		
		return true;
	}
}
