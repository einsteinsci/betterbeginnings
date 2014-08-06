package net.einsteinsci.noobcraft.inventory;


/*
 * public class ContainerKilnStacked extends Container { private TileEntityKiln topKiln; private TileEntityKiln
 * bottomKiln; // public int stacked; private int lastCookTimeTop; private int lastCookTimeBottom; private int
 * lastBurnTime; private int lastItemBurnTime;
 * 
 * public static final int INPUTTOP = 0; public static final int INPUTBOTTOM = 3; public static final int FUEL = 1;
 * public static final int OUTPUTTOP = 2; public static final int OUTPUTBOTTOM = 4;
 * 
 * public ContainerKilnStacked(InventoryPlayer playerInv, TileEntityKiln kilnTop, TileEntityKiln kilnBottom) { if
 * (kilnTop.stacked == -1) { topKiln = kilnTop; bottomKiln = kilnBottom; } else if (kilnTop.stacked == 0) { topKiln =
 * kilnBottom; bottomKiln = kilnTop; }
 * 
 * // stacked = kiln.stacked; addSlotToContainer(new Slot(topKiln, INPUTTOP, 56, 29)); addSlotToContainer(new
 * Slot(bottomKiln, INPUTTOP, 56, 48)); addSlotToContainer(new Slot(bottomKiln, FUEL, 32, 48)); // fuel belongs to
 * bottom kiln addSlotToContainer(new SlotFurnace(playerInv.player, topKiln, OUTPUTTOP, 116, 29));
 * addSlotToContainer(new SlotFurnace(playerInv.player, bottomKiln, OUTPUTTOP, 116, 48));
 * 
 * int i; for (i = 0; i < 3; ++i) { for (int j = 0; j < 9; ++j) { addSlotToContainer(new Slot(playerInv, j + i * 9 + 9,
 * 8 + j * 18, 84 + i * 18)); } }
 * 
 * for (i = 0; i < 9; ++i) { addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142)); } }
 * 
 * @Override public void addCraftingToCrafters(ICrafting craft) { super.addCraftingToCrafters(craft);
 * 
 * craft.sendProgressBarUpdate(this, 0, topKiln.kilnCookTime); craft.sendProgressBarUpdate(this, 1,
 * bottomKiln.kilnBurnTime); craft.sendProgressBarUpdate(this, 2, bottomKiln.currentBurnTime);
 * craft.sendProgressBarUpdate(this, 3, bottomKiln.kilnCookTime); }
 * 
 * @Override public void detectAndSendChanges() { super.detectAndSendChanges();
 * 
 * for (int i = 0; i < crafters.size(); ++i) { ICrafting craft = (ICrafting)crafters.get(i);
 * 
 * if (lastCookTimeTop != topKiln.kilnCookTime) { craft.sendProgressBarUpdate(this, 0, topKiln.kilnCookTime); } if
 * (lastBurnTime != bottomKiln.kilnBurnTime) { craft.sendProgressBarUpdate(this, 1, bottomKiln.kilnBurnTime); } if
 * (lastItemBurnTime != bottomKiln.currentBurnTime) { craft.sendProgressBarUpdate(this, 2, bottomKiln.currentBurnTime);
 * } if (lastCookTimeBottom != bottomKiln.kilnCookTime) { craft.sendProgressBarUpdate(this, 3, bottomKiln.kilnCookTime);
 * } }
 * 
 * lastBurnTime = bottomKiln.kilnBurnTime; lastCookTimeTop = topKiln.kilnCookTime; lastItemBurnTime =
 * bottomKiln.currentBurnTime; lastCookTimeBottom = bottomKiln.kilnCookTime; }
 * 
 * @SideOnly(Side.CLIENT)
 * 
 * @Override public void updateProgressBar(int par1, int par2) { if (par1 == 0) { topKiln.kilnCookTime = par2; } if
 * (par1 == 1) { bottomKiln.kilnBurnTime = par2; } if (par1 == 2) { bottomKiln.currentBurnTime = par2; } if (par1 == 3)
 * { bottomKiln.kilnCookTime = par2; } }
 * 
 * @Override public boolean canInteractWith(EntityPlayer player) { return topKiln.isUseableByPlayer(player) ||
 * bottomKiln.isUseableByPlayer(player); }
 * 
 * @Override public ItemStack transferStackInSlot(EntityPlayer player, int par2) { ItemStack itemstack = null; Slot slot
 * = (Slot)inventorySlots.get(par2);
 * 
 * if (slot != null && slot.getHasStack()) { ItemStack itemstack1 = slot.getStack(); itemstack = itemstack1.copy();
 * 
 * if (par2 == 2) { if (!mergeItemStack(itemstack1, 3, 39, true)) { return null; } slot.onSlotChange(itemstack1,
 * itemstack); } else if (par2 != 1 && par2 != 0) { if (KilnRecipes.smelting().getSmeltingResult(itemstack1) != null) {
 * if (!mergeItemStack(itemstack1, 0, 1, false)) { return null; } } else if (TileEntityKiln.isItemFuel(itemstack1)) { if
 * (!mergeItemStack(itemstack1, 1, 2, false)) { return null; } } else if (par2 >= 3 && par2 < 30) { if
 * (!mergeItemStack(itemstack1, 30, 39, false)) { return null; } } else if (par2 >= 30 && par2 < 39 &&
 * !mergeItemStack(itemstack1, 3, 30, false)) { return null; } } else if (!mergeItemStack(itemstack1, 3, 39, false)) {
 * return null; } if (itemstack1.stackSize == 0) { slot.putStack((ItemStack)null); } else { slot.onSlotChanged(); } if
 * (itemstack1.stackSize == itemstack.stackSize) { return null; } slot.onPickupFromSlot(player, itemstack1); } return
 * itemstack; }
 * 
 * @Override public List getInventory() { ArrayList arraylist = new ArrayList();
 * 
 * for (int i = 0; i < inventorySlots.size(); ++i) { arraylist.add(((Slot)inventorySlots.get(i)).getStack()); }
 * 
 * return arraylist; }
 * 
 * /*
 * 
 * @Override public ItemStack transferStackInSlot(EntityPlayer player, int intoSlot) { ItemStack stack = null; Slot slot
 * = (Slot)this.inventorySlots.get(intoSlot);
 * 
 * if (slot != null && slot.getHasStack()) { ItemStack itemStack1 = slot.getStack(); stack = itemStack1.copy();
 * 
 * if (intoSlot == 2) { if (!this.mergeItemStack(itemStack1, 3, 39, true)) { return null; }
 * slot.onSlotChange(itemStack1, stack); } else if (intoSlot != 1 && intoSlot != 0) { if
 * (FurnaceRecipes.smelting().getSmeltingResult(itemStack1) != null) { if (!this.mergeItemStack(itemStack1, 0, 1,
 * false)) { return null; } } else if (TileEntityKiln.isItemFuel(itemStack1)) { if (!this.mergeItemStack(itemStack1, 1,
 * 2, false)) { return null; } } else if (intoSlot >= 3 && intoSlot < 30) { if (!this.mergeItemStack(itemStack1, 30, 39,
 * false)) { return null; } } else if (intoSlot >= 30 && intoSlot < 39 && !this.mergeItemStack(itemStack1, 3, 30,
 * false)) { return null; } else if (!this.mergeItemStack(itemStack1, 3, 39, false)) { return null; }
 * 
 * if (itemStack1.stackSize == 0) { slot.putStack(null); } else { slot.onSlotChanged(); }
 * 
 * if (itemStack1.stackSize == stack.stackSize) { return null; }
 * 
 * slot.onPickupFromSlot(player, itemStack1); }
 * 
 * return stack; }
 * 
 * return null; }
 * 
 * }
 */
