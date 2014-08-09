package net.einsteinsci.noobcraft.event;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceFace extends DamageSource
{
	public Block faceHurt;
	
	public DamageSourceFace(Block block)
	{
		super("face");
		faceHurt = block;
		setDamageBypassesArmor();
		setDifficultyScaled();
	}
	
	@Override
	public IChatComponent func_151519_b(EntityLivingBase mob)
	{
		return new ChatComponentTranslation("damage.face", mob.func_145748_c_(), faceHurt.getLocalizedName());
	}
}
