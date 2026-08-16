package com.lothrazar.shiftclickvillagertrading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

  // Merchant menu slot layout:
  // 0 = left input (first ingredient) <- we target this
  // 1 = right input (second ingredient)
  // 2 = result/output (vanilla shift-click already works here)
  // 3+ = player inventory and hotbar
  private static final int MERCHANT_LEFT_INPUT_SLOT = 0;
  private static final int MERCHANT_PLAYER_SLOTS_START = 3;

  @SubscribeEvent
  public static void onScreenMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
    if (!(event.getScreen() instanceof MerchantScreen screen)) {
      return;
    }
    if (event.getButton() != 0) {
      return;
    }
    if (!Screen.hasShiftDown()) {
      return;
    }
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null || mc.gameMode == null) {
      return;
    }
    MerchantMenu menu = screen.getMenu();
    if (!menu.getCarried().isEmpty()) {
      return;
    }
    Slot hoveredSlot = screen.getSlotUnderMouse();
    if (hoveredSlot == null) {
      return;
    }
    int slotIndex = hoveredSlot.index;
    if (slotIndex < MERCHANT_PLAYER_SLOTS_START) {
      return;
    }
    if (!hoveredSlot.hasItem()) {
      return;
    }
    event.setCanceled(true);
    int containerId = menu.containerId;
    // Pick up the stack from the player slot into cursor
    mc.gameMode.handleInventoryMouseClick(containerId, slotIndex, 0, ClickType.PICKUP, mc.player);
    // Place cursor item into the left input slot
    mc.gameMode.handleInventoryMouseClick(containerId, MERCHANT_LEFT_INPUT_SLOT, 0, ClickType.PICKUP, mc.player);
    // If cursor still holds items (left slot was full or had a partial merge), return them to the player slot
    if (!menu.getCarried().isEmpty()) {
      mc.gameMode.handleInventoryMouseClick(containerId, slotIndex, 0, ClickType.PICKUP, mc.player);
    }
  }
}
