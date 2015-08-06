package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockInfusionRepairStation;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityInfusionRepair;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemTestItem extends Item implements IBBName
{
	public ItemTestItem()
	{
		super();
		setUnlocalizedName("testItem");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
								  float hitX, float hitY, float hitZ)
	{
		Block tested = world.getBlockState(pos).getBlock();
		if (tested instanceof BlockInfusionRepairStation)
		{
			BlockInfusionRepairStation birs = (BlockInfusionRepairStation)tested;

			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity != null && tileEntity instanceof TileEntityInfusionRepair)
			{
				TileEntityInfusionRepair infusionRepair = (TileEntityInfusionRepair)tileEntity;

				ChatUtil.sendChatToPlayer(player, "Contains:");
				for (ItemStack st : infusionRepair.stacks)
				{
					if (st != null)
					{
						ChatUtil.sendChatToPlayer(player, st.toString());
					}
				}

				TileEntityInfusionRepair.Ingredient ingredient = infusionRepair.getNextIngredient();
				ChatUtil.sendChatToPlayer(player, "\nNext Item: " + (ingredient != null ? ingredient.toString() : "NULL"));
			}
		}

		return true;
	}

	@Override
	public String getName()
	{
		return "testItem";
	}
}
