package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerEnderSmelter;
import net.einsteinsci.betterbeginnings.renderer.RenderItemPartialTransparency;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityEnderSmelter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by einsteinsci on 8/21/2014.
 */
public class GuiEnderSmelter extends GuiContainer
{
	// Same gui texture.
	private static final ResourceLocation kilnGuiTextures = new ResourceLocation(ModMain.MODID +
			                                                                             ":textures/gui/container/smelter.png");
	RenderItemPartialTransparency partialTransItemRenderer = new RenderItemPartialTransparency();
	private TileEntityEnderSmelter tileSmelter;

	public GuiEnderSmelter(InventoryPlayer invPlayer, TileEntityEnderSmelter tile)
	{
		super(new ContainerEnderSmelter(invPlayer, tile));
		tileSmelter = tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string =
				tileSmelter.hasCustomInventoryName() ? tileSmelter.getInventoryName() : I18n.format(
						tileSmelter.getInventoryName(), new Object[0]);
		fontRendererObj.drawString(string, xSize / 2 - fontRendererObj.getStringWidth(string), 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(kilnGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;

		if (tileSmelter.isBurning())
		{
			i1 = tileSmelter.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}

		i1 = tileSmelter.getCookProgressScaled(24);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);

		if (tileSmelter.getStackInSlot(TileEntityEnderSmelter.GRAVEL) == null)
		{
			drawItemStack(new ItemStack(Blocks.gravel), k + 66, l + 17, "");
		}
	}

	private void drawItemStack(ItemStack stack, int xPos, int yPos, String note)
	{
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
		partialTransItemRenderer.zLevel = 200.0F;
		FontRenderer font = null;
		if (stack != null)
		{
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null)
		{
			font = fontRendererObj;
		}

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		partialTransItemRenderer.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), stack, xPos, yPos);
		partialTransItemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(), stack, xPos, yPos, note);
		zLevel = 0.0F;
		partialTransItemRenderer.zLevel = 0.0F;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}
}
