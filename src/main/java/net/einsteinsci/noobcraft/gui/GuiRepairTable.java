package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.event.ChatUtil;
import net.einsteinsci.noobcraft.inventory.ContainerRepairTable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRepairTable extends GuiContainer
{
	private ContainerRepairTable container;
	
	private RenderItemPartialTransparency partialTransItemRenderer = new RenderItemPartialTransparency();
	
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(ModMain.MODID +
		":textures/gui/container/repairTable.png");
	
	private EntityPlayer player;
	
	public GuiRepairTable(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		super(new ContainerRepairTable(invPlayer, world, x, y, z));
		
		container = (ContainerRepairTable)inventorySlots;
		
		player = invPlayer.player;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		buttonList.clear();
		buttonList.add(new GuiButton(0, (width - xSize) / 2 + 6, (height - ySize) / 2 + 55, 64, 20, I18n
			.format("container.repairTable.repair")));
		GuiButton repairButton = (GuiButton)buttonList.get(0);
		if (repairButton != null)
		{
			repairButton.enabled = false;
		}
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		// I'm guessing the really big number at the end is the z layer.
		// fontRendererObj.drawString(I18n.format("container.repairTable", new Object[0]), 8, 6, 4210752);
		ItemStack stack = container.circleSlots[0].getStack();
		if ((container.requiredLevels != 0 || container.takenLevels != 0) && stack != null)
		{
			if (stack.isItemDamaged())
			{
				fontRendererObj.drawString(ChatUtil.BLACK + I18n.format("container.repairTable.minLevel") + ": ", 8, 6,
					4210752);
				fontRendererObj.drawString("" + container.requiredLevels + I18n.format("container.repairTable.level"),
					16, 16, 4210752);
				fontRendererObj.drawString(ChatUtil.BLACK + I18n.format("container.repairTable.levelCost") + ": ", 8,
					28, 4210752);
				fontRendererObj.drawString("" + container.takenLevels + I18n.format("container.repairTable.level"), 16,
					38, 4210752);
			}
		}
		
		if (stack != null)
		{
			if (!stack.isItemDamaged())
			{
				fontRendererObj.drawString(I18n.format("container.repairTable.notNeeded.0"), 8, 6, 4210752);
				fontRendererObj.drawString(I18n.format("container.repairTable.notNeeded.1"), 8, 16, 4210752);
			}
		}
		
		if (container.canRepair(player))
		{
			GuiButton button = (GuiButton)buttonList.get(0);
			if (button != null)
			{
				button.enabled = true;
			}
		}
		else
		{
			GuiButton button = (GuiButton)buttonList.get(0);
			if (button != null)
			{
				button.enabled = false;
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
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 0 && button.enabled)
		{
			container.repair(player);
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
		
		for (int i = 0; i < container.requiredItems.size(); ++i)
		{
			if (container.requiredItems.get(i) == null)
			{
				break;
			}
			
			Slot slot = container.circleSlots[i + 1];
			ItemStack needed = container.requiredItems.get(i);
			
			if (needed != null && !slot.getHasStack())
			{
				drawItemStack(needed, (width - xSize) / 2 + slot.xDisplayPosition, (height - ySize) / 2 +
					slot.yDisplayPosition, "" + needed.stackSize);
			}
		}
	}
}
