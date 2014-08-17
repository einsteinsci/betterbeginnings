package net.einsteinsci.betterbeginnings.renderer;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blockmodels.ModelCampfire;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCampfire extends TileEntitySpecialRenderer
{

	public static final ResourceLocation texture = new ResourceLocation(ModMain.MODID + ":textures/model/Campfire.png");

	private ModelCampfire model;

	public RenderCampfire()
	{
		this.model = new ModelCampfire();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x,
								   double y, double z, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		GL11.glRotatef(180, 0F, 0F, 1F);

		this.bindTexture(texture);

		GL11.glPushMatrix();
		this.model.renderModel(0.0625F);
		GL11.glPopMatrix();


		GL11.glPopMatrix();
	}

}
