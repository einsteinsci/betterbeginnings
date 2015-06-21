package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.client.RenderItemGhost;
import net.einsteinsci.betterbeginnings.inventory.ContainerSmelter;
import net.einsteinsci.betterbeginnings.tileentity.TileEntitySmelter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiSmelter extends GuiContainer
{
	private static final ResourceLocation smelterGuiTextures = new ResourceLocation(ModMain.MODID +
																						 ":textures/gui/container/smelter.png");
	//RenderItemPartialTransparency partialTransItemRenderer = new RenderItemPartialTransparency();
	RenderItemGhost ghoster = new RenderItemGhost();
	private TileEntitySmelter tileSmelter;

	public GuiSmelter(InventoryPlayer invPlayer, TileEntitySmelter tile)
	{
		super(new ContainerSmelter(invPlayer, tile));
		tileSmelter = tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string = tileSmelter.hasCustomName() ? tileSmelter.getName() : I18n.format(tileSmelter.getName());
		fontRendererObj.drawString(string, xSize / 2 - fontRendererObj.getStringWidth(string), 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(smelterGuiTextures);
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

		if (tileSmelter.getStackInSlot(TileEntitySmelter.GRAVEL) == null)
		{
			drawItemStack(new ItemStack(Blocks.gravel), k + 66, l + 17, "");
		}
	}

	private void drawItemStack(ItemStack stack, int xPos, int yPos, String note)
	{
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
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

		ghoster.renderItemIntoGUI(stack, xPos, yPos);
		ghoster.renderItemOverlayIntoGUI(font, stack, xPos, yPos, note);
		//partialTransItemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(), stack, xPos, yPos, note);
		zLevel = 0.0F;
		//partialTransItemRenderer.zLevel = 0.0F;
		ghoster.zLevel = 0.0f;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}
}
