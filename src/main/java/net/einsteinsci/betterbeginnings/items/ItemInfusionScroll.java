package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockInfusionRepairStation;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityInfusionRepair;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
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
		setMaxStackSize(1);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
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
					ChatUtil.sendChatToPlayer(player, I18n.format("scroll.notool"));
					return true;
				}

				if (!infusionRepair.isDiffusionMode())
				{
					if (infusionRepair.stackTool().getItemDamage() == 0)
					{
						ChatUtil.sendChatToPlayer(player, I18n.format("scroll.repaired"));
						return true;
					}

					TileEntityInfusionRepair.InfusionIngredient ingredient = infusionRepair.getNextIngredient();
					if (ingredient == null)
					{
						ChatUtil.sendChatToPlayer(player, ChatUtil.RED + I18n.format("scroll.error"));
					}
					else if (ingredient.isXP)
					{
						ChatUtil.sendChatToPlayer(player, I18n.format("scroll.xp", ingredient.count));
					}
					else
					{
						ItemStack ingredientStack = new ItemStack(ingredient.item, ingredient.count, ingredient.damage);
						ChatUtil.sendChatToPlayer(player, I18n.format("scroll.item", ingredient.count,
							ingredientStack.getDisplayName()));
					}
				}
				else
				{
					if (infusionRepair.diffusionReady())
					{
						int healthNeeded = BBConfig.diffusionHealthTaken - (int)infusionRepair.getHealthTaken();
						float heartsNeeded = (float)healthNeeded / 2.0f;
						String heartsNeededStr = "" + heartsNeeded;
						heartsNeededStr = heartsNeededStr.replaceFirst("\\.5", " 1/2");
						heartsNeededStr = heartsNeededStr.replaceFirst("\\.0", "");
						ChatUtil.sendChatToPlayer(player, I18n.format("scroll.diffusionblood", "" + heartsNeededStr));
					}
					else if (infusionRepair.diffusionHasTool())
					{
						ChatUtil.sendChatToPlayer(player, I18n.format("scroll.diffusionbook"));
					}
					else
					{
						ChatUtil.sendChatToPlayer(player, I18n.format("scroll.notool"));
					}
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
