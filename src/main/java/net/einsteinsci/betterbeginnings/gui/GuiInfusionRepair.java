package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerInfusionRepair;
import net.einsteinsci.betterbeginnings.register.InfusionRepairUtil;
import net.einsteinsci.betterbeginnings.renderer.RenderItemPartialTransparency;
import net.einsteinsci.betterbeginnings.util.ChatUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GuiInfusionRepair extends GuiContainer
{

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(ModMain.MODID
			                                                                                      + ":textures/gui/container/infusionRepairStation.png");
	private RenderItemPartialTransparency partialTransItemRenderer = new RenderItemPartialTransparency();
	private ContainerInfusionRepair container;
	private EntityPlayer player;

	public GuiInfusionRepair(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		super(new ContainerInfusionRepair(invPlayer, world, x, y, z));

		container = (ContainerInfusionRepair)inventorySlots;
		player = invPlayer.player;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int takenLevels = InfusionRepairUtil.getTakenLevels(container.inputs);
		String takenLevelsStr = "";

		int neededLevels = InfusionRepairUtil.getNeededLevels(container.inputs);
		String neededLevelsStr = "";

		ItemStack repaired = container.inputs.getStackInSlot(0);
		if (repaired != null)
		{
			if (repaired.isItemStackDamageable() &&
					(player.experienceLevel >= InfusionRepairUtil.getNeededLevels(container.inputs)
							|| player.capabilities.isCreativeMode))
			{
				takenLevelsStr = "Taken: " + takenLevels + " L";
				neededLevelsStr = "Needed: " + neededLevels + " L";
			}
			else if (repaired.isItemStackDamageable())
			{
				takenLevelsStr = ChatUtil.RED + "Taken: " + takenLevels + " L";
				neededLevelsStr = ChatUtil.RED + "Needed: " + neededLevels + " L";
			}
			else
			{
				takenLevelsStr = "Taken: ";
				neededLevelsStr = "Needed: ";
			}
		}
		else
		{
			takenLevelsStr = "Taken: ";
			neededLevelsStr = "Needed: ";
		}

		fontRendererObj.drawString(neededLevelsStr, 90, 5, 4210752);
		fontRendererObj.drawString(takenLevelsStr, 90, 67, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		ArrayList<ItemStack> requiredItems = InfusionRepairUtil.getRequiredStacks(container.inputs);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		for (int i = 0; i < requiredItems.size(); ++i)
		{
			if (requiredItems.get(i) == null)
			{
				break;
			}

			Slot slot = container.circleSlots[i + 1];
			ItemStack needed = requiredItems.get(i);

			if (needed != null && !slot.getHasStack())
			{
				drawItemStack(needed, (width - xSize) / 2 + slot.xDisplayPosition, (height - ySize) / 2 +
						slot.yDisplayPosition, "" + needed.stackSize);
			}
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
