package com.lothrazar.shiftclickvillagertrading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = ModMain.MODID, value = Dist.CLIENT)
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
    Minecraft mc = Minecraft.getInstance();
    if (!mc.hasShiftDown()) {
      return;
    }
    if (mc.player == null || mc.gameMode == null) {
      return;
    }
    MerchantMenu menu = screen.getMenu();
    if (!menu.getCarried().isEmpty()) {
      return;
    }
    Slot hoveredSlot = findSlot(screen, event.getMouseX(), event.getMouseY());
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
    mc.gameMode.handleContainerInput(containerId, slotIndex, 0, ContainerInput.PICKUP, mc.player);
    // Place cursor item into the left input slot
    mc.gameMode.handleContainerInput(containerId, MERCHANT_LEFT_INPUT_SLOT, 0, ContainerInput.PICKUP, mc.player);
    // If cursor still holds items (left slot was full or had a partial merge), return them to the player slot
    if (!menu.getCarried().isEmpty()) {
      mc.gameMode.handleContainerInput(containerId, slotIndex, 0, ContainerInput.PICKUP, mc.player);
    }
  }

  private static Slot findSlot(AbstractContainerScreen<?> screen, double mouseX, double mouseY) {
    int leftPos = screen.leftPos;
    int topPos = screen.topPos;
    for (Slot slot : screen.getMenu().slots) {
      double relX = mouseX - leftPos;
      double relY = mouseY - topPos;
      if (relX >= slot.x - 1 && relX < slot.x + 17 && relY >= slot.y - 1 && relY < slot.y + 17) {
        return slot;
      }
    }
    return null;
  }
}
