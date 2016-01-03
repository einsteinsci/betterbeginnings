package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerSimpleWorkbench;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSimpleWorkbench extends GuiContainer
{
	private static final ResourceLocation craftingTableGuiTextures =
			new ResourceLocation(ModMain.MODID + ":textures/gui/container/simpleWorkbench.png");
	private ContainerSimpleWorkbench container;

	public GuiSimpleWorkbench(InventoryPlayer invPlayer, World world, BlockPos pos)
	{
		super(new ContainerSimpleWorkbench(invPlayer, world, pos));

		container = (ContainerSimpleWorkbench)inventorySlots;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		// I'm guessing the really big number at the end is the z layer.
		fontRendererObj.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);

		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		if (container.needsBigBoyBench())
		{
			if (mouseX >= k + 87 && mouseX <= k + 115 && mouseY >= l + 32 && mouseY <= l + 53)
			{
				List<String> lines = new ArrayList<>();
				lines.add(ChatUtil.RED + I18n.format("container.crafting.needsBigBoyBench"));

				drawHoveringText(lines, mouseX - k - 96, mouseY - l - 8);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		if (container.needsBigBoyBench())
		{
			drawTexturedModalRect(k + 87, l + 32, xSize, 0, 28, 21);
		}
	}
}
