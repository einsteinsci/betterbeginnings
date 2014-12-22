package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerNetherBrickOven;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by einsteinsci on 8/22/2014.
 */
@SideOnly(Side.CLIENT)
public class GuiNetherBrickOven extends GuiContainer
{
	private static final ResourceLocation ovenGuiTextures =
			new ResourceLocation(ModMain.MODID + ":textures/gui/container/netherBrickOven.png");

	private TileEntityNetherBrickOven tileBrickOven;

	public GuiNetherBrickOven(InventoryPlayer inventory, TileEntityNetherBrickOven tile)
	{
		super(new ContainerNetherBrickOven(inventory, tile));
		tileBrickOven = tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String string =
				tileBrickOven.hasCustomInventoryName() ? tileBrickOven.getInventoryName() :
						I18n.format(tileBrickOven.getInventoryName());
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
		List<String> lines = new ArrayList<String>();

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
		//drawTexturedModalRect(k + 17, l + 58 - tankFillPx, 176, 31, 16, tankFillPx);
		drawFluid(tileBrickOven.getFuelStack(), k + 17, l + 10, 16, 48, tileBrickOven.fuelTank.getCapacity());
	}

	public void drawFluid(FluidStack fluid, int x, int y, int width, int height, int maxCapacity)
	{
		if (fluid == null || fluid.getFluid() == null)
		{
			return;
		}
		IIcon icon = fluid.getFluid().getIcon(fluid);
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		setGLColorFromInt(fluid.getFluid().getColor(fluid));
		int fullX = width / 16;
		int fullY = height / 16;
		int lastX = width - fullX * 16;
		int lastY = height - fullY * 16;
		int level = fluid.amount * height / maxCapacity;
		int fullLvl = (height - level) / 16;
		int lastLvl = height - level - fullLvl * 16;
		for (int i = 0; i < fullX; i++)
		{
			for (int j = 0; j < fullY; j++)
			{
				if (j >= fullLvl)
				{
					drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
				}
			}
		}
		for (int i = 0; i < fullX; i++)
		{
			drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
		}
		for (int i = 0; i < fullY; i++)
		{
			if (i >= fullLvl)
			{
				drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
			}
		}
		drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
	}

	public static void setGLColorFromInt(int color)
	{
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, 1.0F);
	}

	//The magic is here
	private void drawCutIcon(IIcon icon, int x, int y, int width, int height, int cut)
	{
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y + height, zLevel, icon.getMinU(), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + height, zLevel, icon.getInterpolatedU(width),
		                     icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + cut, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(cut));
		tess.addVertexWithUV(x, y + cut, zLevel, icon.getMinU(), icon.getInterpolatedV(cut));
		tess.draw();
	}
}
