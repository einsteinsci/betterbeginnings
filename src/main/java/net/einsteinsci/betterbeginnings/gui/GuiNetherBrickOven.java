package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerNetherBrickOven;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by einsteinsci on 8/22/2014.
 */
@SideOnly(Side.CLIENT)
public class GuiNetherBrickOven extends GuiContainer
{
	private static final ResourceLocation ovenGuiTextures = new ResourceLocation(ModMain.MODID +
			                                                                             ":textures/gui/container/netherBrickOven.png");
	private TileEntityNetherBrickOven tileBrickOven;

	public GuiNetherBrickOven(InventoryPlayer inventory, TileEntityNetherBrickOven tile)
	{
		super(new ContainerNetherBrickOven(inventory, tile));
		tileBrickOven = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String string =
				tileBrickOven.hasCustomInventoryName() ? tileBrickOven.getInventoryName() : I18n.format(
						tileBrickOven.getInventoryName());
		fontRendererObj.drawString(string, 40, 6, 4210752);

		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		// In fuel zone
		if (mouseX >= k + 16 && mouseX <= k + 33 && mouseY >= l + 9 && mouseY <= l + 58)
		{
			drawFluidTooltip(mouseX - k, mouseY - l);
		}
	}

	private void drawFluidTooltip(int x, int y)
	{
		List<String> lines = new ArrayList<>();

		lines.add("" + tileBrickOven.getFuelLevel() + " mB");
		func_146283_a(lines, x, y); //Draw tooltip
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(ovenGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;

		i1 = tileBrickOven.getCookProgressScaled(24);
		drawTexturedModalRect(k + 103, l + 34, 176, 14, i1 + 1, 16);

		int tankFillPx = tileBrickOven.getFuelLevelScaled(48);
		drawTexturedModalRect(k + 17, l + 57 - tankFillPx, 176, 78 - tankFillPx, 16, tankFillPx);
	}
}
