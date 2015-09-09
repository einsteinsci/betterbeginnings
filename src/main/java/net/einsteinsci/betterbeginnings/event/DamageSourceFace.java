package net.einsteinsci.betterbeginnings.event;

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
	public IChatComponent getDeathMessage(EntityLivingBase mob)
	{
		return new ChatComponentTranslation("death.face", mob.getCommandSenderName(), faceHurt.getLocalizedName());
	}
}
