package net.einsteinsci.betterbeginnings.entity.projectile;

import io.netty.buffer.ByteBuf;
import net.einsteinsci.betterbeginnings.items.ItemKnife;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityThrownKnife extends EntityThrowable implements IEntityAdditionalSpawnData
{
	private static final String TAG_THROWN_KNIFE = "ThrownKnife";

	private ItemStack knife;
	private float baseDamage;
	private float force;
	private boolean inTerrain;
	private BlockPos stuckPos = new BlockPos(-1, -1, -1);

	public EntityThrownKnife(World worldIn) 
	{
		super(worldIn);
	}

	public EntityThrownKnife(World world, EntityLivingBase thrower, ItemStack knife) 
	{
		super(world, thrower);
		this.knife = knife;
		this.baseDamage = ((ItemTool)knife.getItem()).getToolMaterial().getDamageVsEntity() + ItemKnife.DAMAGE;
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) 
	{
		switch(mop.typeOfHit)
		{
		case BLOCK:
			if (!inTerrain)
			{
				doBlockHitEffects(worldObj, mop);
			}
			BlockPos pos = mop.getBlockPos();
			IBlockState state = worldObj.getBlockState(pos);
			if(state.getBlock().getCollisionBoundingBox(worldObj, pos, state) != null)
			{
				this.inTerrain = true;
				this.stuckPos = pos;
				this.setVelocity(0.0F, 0.0F, 0.0F);
			}	
			break;
		case ENTITY:
			if(mop.entityHit instanceof EntityLivingBase)
			{
				EntityLivingBase entityLiving = (EntityLivingBase) mop.entityHit;
				if(!worldObj.isRemote && !knife.attemptDamageItem(2, rand))
				{
					entityLiving.attackEntityFrom(DamageSource.causeThrownDamage(mop.entityHit, this.getThrower()), baseDamage * force);
					entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), (int) (100 * force), 2, false, false));
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer playerIn) 
	{
		this.knife.stackSize = 1;
		if(inTerrain && playerIn.inventory.addItemStackToInventory(this.knife))
		{
			this.setDead();
		}
	}

	@Override
	public void onUpdate() 
	{
		if(worldObj.getBlockState(this.stuckPos).getBlock().isAir(worldObj, stuckPos))
		{
			this.inTerrain = false;
		}
		if(!inTerrain)
		{
			super.onUpdate();
		}
	}

	private void doBlockHitEffects(World worldObj, MovingObjectPosition mop) 
	{
		IBlockState state = this.worldObj.getBlockState(mop.getBlockPos());
		for(int p = 0; p < 8; p ++)
		{
			worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, 0.0F, 0.0F, 0.0F, Block.getStateId(state));
		}
		worldObj.playSoundAtEntity(this, state.getBlock().stepSound.getStepSound(), 0.8F, 0.9F);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) 
	{
		super.readEntityFromNBT(tagCompound);
		knife = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag(TAG_THROWN_KNIFE));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) 
	{
		super.writeEntityToNBT(tagCompound);
		if(knife != null)
		{
			NBTTagCompound thrownKnife = knife.writeToNBT(new NBTTagCompound());
			tagCompound.setTag(TAG_THROWN_KNIFE, thrownKnife);
		}
	}

	public ItemStack getKnife() 
	{
		return knife;
	}

	public EntityThrownKnife setForce(float force)
	{
		this.force = force;
		return this;
	}

	@Override
	public void writeSpawnData(ByteBuf additionalData) 
	{
		ByteBufUtils.writeItemStack(additionalData, knife);
		additionalData.writeFloat(baseDamage);
		additionalData.writeFloat(force);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{
		knife = ByteBufUtils.readItemStack(additionalData);
		baseDamage = additionalData.readFloat();
		force = additionalData.readFloat();
	}
}