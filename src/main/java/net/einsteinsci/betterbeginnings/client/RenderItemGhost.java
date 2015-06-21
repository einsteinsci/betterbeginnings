package net.einsteinsci.betterbeginnings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IColoredBakedQuad;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;

@Deprecated
public class RenderItemGhost extends RenderItem
{
	public static final float DEF_TRANSPARENCY = 0.25f;

	float transparencyAmount;
	private static final ResourceLocation GHOST_RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");
	TextureManager ghostTextureManager;

	public RenderItemGhost(float transparency)
	{
		super(getBaseTextureManager(), getBaseModelManager());
		transparencyAmount = transparency;

		ghostTextureManager = getBaseTextureManager();
	}

	public RenderItemGhost()
	{
		this(DEF_TRANSPARENCY);
	}

	static TextureManager getBaseTextureManager()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		// gonna need some reflection here
		Object objTexMan = ReflectionHelper.getPrivateValue(RenderItem.class, renderItem, "textureManager");
		return (TextureManager)objTexMan;
	}

	static ModelManager getBaseModelManager()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		return renderItem.getItemModelMesher().getModelManager();
	}

	// region base class methods

	public void renderItemIntoGUI(ItemStack stack, int x, int y)
	{
		TextureManager textureManager = getBaseTextureManager();

		IBakedModel ibakedmodel = getItemModelMesher().getItemModel(stack);
		GlStateManager.pushMatrix();
		textureManager.bindTexture(TextureMap.locationBlocksTexture);
		textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);

		// HELLO
		GlStateManager.color(1.0F, 1.0F, 1.0F, transparencyAmount);

		setupGuiTransform(x, y, ibakedmodel.isGui3d());
		applyTransform(ibakedmodel.getItemCameraTransforms().gui);
		renderItem(stack, ibakedmodel);
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		textureManager.bindTexture(TextureMap.locationBlocksTexture);
		textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
	}

	public void renderItem(ItemStack stack, IBakedModel model)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();

		if (model.isBuiltInRenderer())
		{
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, transparencyAmount);
			GlStateManager.enableRescaleNormal();
			TileEntityItemStackRenderer.instance.renderByItem(stack);
		}
		else
		{
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			this.renderModel(model, -1, stack);

			if (stack.hasEffect())
			{
				this.renderEffect(model);
			}
		}

		GlStateManager.popMatrix();
	}

	private void renderModel(IBakedModel model, int color, ItemStack stack)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.setVertexFormat(DefaultVertexFormats.ITEM);
		EnumFacing[] a_enumfacing = EnumFacing.values();
		int j = a_enumfacing.length;

		for (int k = 0; k < j; ++k)
		{
			EnumFacing enumfacing = a_enumfacing[k];
			this.renderQuads(worldrenderer, model.getFaceQuads(enumfacing), color, stack);
		}

		this.renderQuads(worldrenderer, model.getGeneralQuads(), color, stack);
		tessellator.draw();
	}

	private void renderEffect(IBakedModel model)
	{
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(768, 1);
		ghostTextureManager.bindTexture(GHOST_RES_ITEM_GLINT);
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		// oh so that's how they do that!
		float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(model, -8372020, null);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(model, -8372020, null);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		ghostTextureManager.bindTexture(TextureMap.locationBlocksTexture);
	}

	private void renderQuads(WorldRenderer renderer, List quads, int color, ItemStack stack)
	{
		boolean flag = color == -1 && stack != null;
		BakedQuad bakedquad;
		int j;
		//renderer.setColorRGBA_F(1.0f, 1.0f, 1.0f, transparencyAmount);

		for (Iterator iterator = quads.iterator(); iterator.hasNext(); this.renderQuad(renderer, bakedquad, j))
		{
			bakedquad = (BakedQuad)iterator.next();
			j = color;

			if (flag && bakedquad.hasTintIndex())
			{
				j = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable)
				{
					j = TextureUtil.anaglyphColor(j);
				}

				j |= -16777216;
			}
		}
	}

	private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d)
	{
		GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + this.zLevel);
		GlStateManager.translate(8.0F, 8.0F, 0.0F);
		GlStateManager.scale(1.0F, 1.0F, -1.0F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		if (isGui3d)
		{
			GlStateManager.scale(40.0F, 40.0F, 40.0F);
			GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.enableLighting();
		}
		else
		{
			GlStateManager.scale(64.0F, 64.0F, 64.0F);
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.disableLighting();
		}
	}

	private void renderQuad(WorldRenderer renderer, BakedQuad quad, int color)
	{
		int transColor = color;
		if (color != -1)
		{
			int alpha = (color << 16) & 255;
			transColor = color - alpha;
			alpha = (int)(255.0f * transparencyAmount);
			transColor = transColor + alpha;
		}

		renderer.addVertexData(quad.getVertexData());
		if (quad instanceof IColoredBakedQuad)
		{
			ForgeHooksClient.putQuadColor(renderer, quad, transColor);
		}
		else
		{
			renderer.putColor4(transColor);
		}

		putQuadNormal(renderer, quad);
	}

	private void putQuadNormal(WorldRenderer renderer, BakedQuad quad)
	{
		Vec3i vec3i = quad.getFace().getDirectionVec();
		renderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
	}

	// endregion
}
