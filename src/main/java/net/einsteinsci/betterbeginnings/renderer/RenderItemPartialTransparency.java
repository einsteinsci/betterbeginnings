package net.einsteinsci.betterbeginnings.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItemPartialTransparency extends RenderItem
{
	public static final float OPACITY = 0.4F;
	protected RenderBlocks renderBlocksRi2 = new RenderBlockPartialTransparency();

	@Override
	public void renderItemIntoGUI(FontRenderer renderer, TextureManager textures, ItemStack stack, int xPos, int yPos,
								  boolean renderEffect)
	{
		int k = stack.getItemDamage();
		Object object = stack.getIconIndex();
		int l;
		float f;
		float f3;
		float f4;

		if (stack.getItemSpriteNumber() == 0 &&
				RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType()))
		{
			textures.bindTexture(TextureMap.locationBlocksTexture);
			Block block = Block.getBlockFromItem(stack.getItem());
			GL11.glEnable(GL11.GL_ALPHA_TEST);

			//if (block.getRenderBlockPass() != 0)
			//{
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
			//}
			//else
			//{
			//	GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
			//	GL11.glEnable(GL11.GL_BLEND);
			//}

			GL11.glPushMatrix();
			GL11.glTranslatef(xPos - 2, yPos + 3, -3.0F + zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			l = stack.getItem().getColorFromItemStack(stack, 0);
			f3 = (l >> 16 & 255) / 255.0F;
			f4 = (l >> 8 & 255) / 255.0F;
			f = (l & 255) / 255.0F;

			if (renderWithColor)
			{
				GL11.glColor4f(f3, f4, f, OPACITY);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			renderBlocksRi2.useInventoryTint = renderWithColor;
			renderBlocksRi2.renderBlockAsItem(block, k, 1.0f);
			renderBlocksRi2.useInventoryTint = true;

			if (block.getRenderBlockPass() == 0)
			{
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			}

			GL11.glPopMatrix();
		}
		else if (stack.getItem().requiresMultipleRenderPasses())
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			textures.bindTexture(TextureMap.locationItemsTexture);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(0, 0, 0, 0);
			GL11.glColorMask(false, false, false, true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, OPACITY);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(-1);
			// tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.1f);
			tessellator.addVertex(xPos - 2, yPos + 18, zLevel);
			tessellator.addVertex(xPos + 18, yPos + 18, zLevel);
			tessellator.addVertex(xPos + 18, yPos - 2, zLevel);
			tessellator.addVertex(xPos - 2, yPos - 2, zLevel);
			tessellator.draw();
			GL11.glColorMask(true, true, true, true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);

			Item item = stack.getItem();
			for (l = 0; l < item.getRenderPasses(k); ++l)
			{
				OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
				textures.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture
											 : TextureMap.locationItemsTexture);
				IIcon iicon = item.getIcon(stack, l);
				int i1 = stack.getItem().getColorFromItemStack(stack, l);
				f = (i1 >> 16 & 255) / 255.0F;
				float f1 = (i1 >> 8 & 255) / 255.0F;
				float f2 = (i1 & 255) / 255.0F;

				if (renderWithColor)
				{
					GL11.glColor4f(f, f1, f2, OPACITY);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}

				GL11.glDisable(GL11.GL_LIGHTING);    // Forge: Make sure that render states are reset, as renderEffect can
				GL11.glEnable(GL11.GL_ALPHA_TEST);  // derp them up.

				renderIcon(xPos, yPos, iicon, 16, 16);

				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_LIGHTING);

				if (renderEffect && stack.hasEffect(l))
				{
					renderEffect(textures, xPos, yPos);
				}
			}

			GL11.glEnable(GL11.GL_LIGHTING);
		}
		else
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			ResourceLocation resourcelocation = textures.getResourceLocation(stack.getItemSpriteNumber());
			textures.bindTexture(resourcelocation);

			if (object == null)
			{
				object =
						((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation))
								.getAtlasSprite("missingno");
			}

			l = stack.getItem().getColorFromItemStack(stack, 0);
			f3 = (l >> 16 & 255) / 255.0F;
			f4 = (l >> 8 & 255) / 255.0F;
			f = (l & 255) / 255.0F;

			if (renderWithColor)
			{
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(f3, f4, f, OPACITY);
			}

			GL11.glDisable(GL11.GL_LIGHTING); // Forge: Make sure that render states are reset, a renderEffect can derp
			// them up.
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);

			renderIcon(xPos, yPos, (IIcon)object, 16, 16);

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);

			if (renderEffect && stack.hasEffect(0))
			{
				renderEffect(textures, xPos, yPos);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}

/*package net.einsteinsci.betterbeginnings.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderItemPartialTransparency extends RenderItem
{
	protected RenderBlocks renderBlocksRi2 = new RenderBlocks();
	
	public static final float OPACITY = 0.4F;
	
	@Override
	public void renderItemIntoGUI(FontRenderer renderer, TextureManager textures, ItemStack stack, int xPos, int yPos,
		boolean renderEffect)
	{
		int k = stack.getItemDamage();
		Object object = stack.getIconIndex();
		int l;
		float f;
		float f3;
		float f4;
		
		if (stack.getItemSpriteNumber() == 0 &&
			RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType()))
		{
			textures.bindTexture(TextureMap.locationBlocksTexture);
			Block block = Block.getBlockFromItem(stack.getItem());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			if (block.getRenderBlockPass() != 0)
			{
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			}
			else
			{
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
				GL11.glDisable(GL11.GL_BLEND);
			}
			
			GL11.glPushMatrix();
			GL11.glTranslatef(xPos - 2, yPos + 3, -3.0F + zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			l = stack.getItem().getColorFromItemStack(stack, 0);
			f3 = (l >> 16 & 255) / 255.0F;
			f4 = (l >> 8 & 255) / 255.0F;
			f = (l & 255) / 255.0F;
			
			if (renderWithColor)
			{
				GL11.glColor4f(f3, f4, f, OPACITY);
			}
			
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			renderBlocksRi2.useInventoryTint = renderWithColor;
			renderBlocksRi2.renderBlockAsItem(block, k, 1.0F);
			renderBlocksRi2.useInventoryTint = true;
			
			if (block.getRenderBlockPass() == 0)
			{
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			}
			
			GL11.glPopMatrix();
		}
		else if (stack.getItem().requiresMultipleRenderPasses())
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			textures.bindTexture(TextureMap.locationItemsTexture);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(0, 0, 0, 0);
			GL11.glColorMask(false, false, false, true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, OPACITY);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(-1);
			tessellator.addVertex(xPos - 2, yPos + 18, zLevel);
			tessellator.addVertex(xPos + 18, yPos + 18, zLevel);
			tessellator.addVertex(xPos + 18, yPos - 2, zLevel);
			tessellator.addVertex(xPos - 2, yPos - 2, zLevel);
			tessellator.draw();
			GL11.glColorMask(true, true, true, true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			Item item = stack.getItem();
			for (l = 0; l < item.getRenderPasses(k); ++l)
			{
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				textures.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture
					: TextureMap.locationItemsTexture);
				IIcon iicon = item.getIcon(stack, l);
				int i1 = stack.getItem().getColorFromItemStack(stack, l);
				f = (i1 >> 16 & 255) / 255.0F;
				float f1 = (i1 >> 8 & 255) / 255.0F;
				float f2 = (i1 & 255) / 255.0F;
				
				if (renderWithColor)
				{
					GL11.glColor4f(f, f1, f2, OPACITY);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}
				
				GL11.glDisable(GL11.GL_LIGHTING); // Forge: Make sure that render states are reset, ad renderEffect can
				// derp them up.
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				
				renderIcon(xPos, yPos, iicon, 16, 16);
				
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_LIGHTING);
				
				if (renderEffect && stack.hasEffect(l))
				{
					renderEffect(textures, xPos, yPos);
				}
			}
			
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		else
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			ResourceLocation resourcelocation = textures.getResourceLocation(stack.getItemSpriteNumber());
			textures.bindTexture(resourcelocation);
			
			if (object == null)
			{
				object =
					((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation))
					.getAtlasSprite("missingno");
			}
			
			l = stack.getItem().getColorFromItemStack(stack, 0);
			f3 = (l >> 16 & 255) / 255.0F;
			f4 = (l >> 8 & 255) / 255.0F;
			f = (l & 255) / 255.0F;
			
			if (renderWithColor)
			{
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(f3, f4, f, OPACITY);
			}
			
			GL11.glDisable(GL11.GL_LIGHTING); // Forge: Make sure that render states are reset, a renderEffect can derp
			// them up.
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			
			renderIcon(xPos, yPos, (IIcon)object, 16, 16);
			
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			
			if (renderEffect && stack.hasEffect(0))
			{
				renderEffect(textures, xPos, yPos);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
*/
