package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
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
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
								  float hitX, float hitY, float hitZ)
	{
		Block tested = world.getBlock(x, y, z);
		String blockName = tested.getUnlocalizedName().substring(5);
		ChatComponentTranslation trans = new ChatComponentTranslation(blockName);
		trans.appendText(" : " + world.getBlockMetadata(x, y, z));
		player.addChatMessage(trans);
		
		/*
		 * if (tested == RegisterBlocks.kiln || tested == RegisterBlocks.kilnLit) { TileEntityKiln kiln =
		 * (TileEntityKiln)world.getTileEntity(x, y, z);
		 * 
		 * ChatUtil.sendChatToPlayer(player, "Stacked: " + kiln.stacked); }
		 */

		return true;
	}
}
