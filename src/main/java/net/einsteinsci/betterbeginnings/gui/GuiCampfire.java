package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerCampfire;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCampfire extends GuiContainer
{

	private static final ResourceLocation RESOURCES =
			new ResourceLocation(ModMain.MODID + ":textures/gui/container/campfire.png");

	private TileEntityCampfire tileCampfire;

	public GuiCampfire(InventoryPlayer invPlayer, TileEntityCampfire tile)
	{
		super(new ContainerCampfire(invPlayer, tile));
		tileCampfire = tile;
		xSize = 176;
		ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 4, 4210752);

		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		if (tileCampfire.stackFuel() == null)
		{
			if (mouseX >= k + 58 && mouseX <= k + 74 && mouseY >= l + 57 && mouseY <= l + 73)
			{
				List<String> lines = new ArrayList<>();
				lines.add(ChatUtil.LIGHT_GRAY + I18n.format("container.campfire.woodFuelsOnly"));

				drawHoveringText(lines, mouseX - k - 72, mouseY - l - 8);
			}
		}
	}

	public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(RESOURCES);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i;

		i = tileCampfire.getBurnTimeRemainingScaled(22);
		drawTexturedModalRect(k + 58, l + 32 + 22 - i, 176, 22 - i, 14, i);

		i = tileCampfire.getDecayTimeRemainingScaled(16);
		// x y u v w h
		int x = k + 77;
		int y = l + 57 + 16 - i;
		int u = 176;
		int v = 55 - i;
		int w = 3;
		int h = i;
		drawTexturedModalRect(x, y, u, v, w, h);

		i = tileCampfire.getCookProgressScaled(23);
		drawTexturedModalRect(k + 82, l + 34, 177, 22, i, 16);
	}
}
