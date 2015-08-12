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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemInfusionScroll extends Item implements IBBName
{
	public ItemInfusionScroll()
	{
		super();
		setUnlocalizedName(getName());
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
		float hitX, float hitY, float hitZ)
	{
		Block clickedBlock = world.getBlockState(pos).getBlock();
		if (clickedBlock instanceof BlockInfusionRepairStation)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity != null && tileEntity instanceof TileEntityInfusionRepair)
			{
				TileEntityInfusionRepair infusionRepair = (TileEntityInfusionRepair)tileEntity;

				if (infusionRepair.stackTool() == null)
				{
					ChatUtil.sendChatToPlayer(player, "No tool on table.");
					return true;
				}
				else if (infusionRepair.stackTool().getItemDamage() == 0)
				{
					ChatUtil.sendChatToPlayer(player, "Tool is repaired.");
					return true;
				}

				TileEntityInfusionRepair.Ingredient ingredient = infusionRepair.getNextIngredient();
				if (ingredient == null)
				{
					ChatUtil.sendChatToPlayer(player, ChatUtil.RED + "Next ingredient is NULL.");
				}
				else if (ingredient.isXP)
				{
					ChatUtil.sendChatToPlayer(player, "Infusion requires " + ingredient.count + " levels.");
				}
				else
				{
					ItemStack ingredientStack = new ItemStack(ingredient.item, ingredient.count, ingredient.damage);
					ChatUtil.sendChatToPlayer(player, "Next ingredient: " + ingredient.count + "x " +
						ingredientStack.getDisplayName());
				}
			}
		}

		return true;
	}

	@Override
	public String getName()
	{
		return "infusionScroll";
	}
}
