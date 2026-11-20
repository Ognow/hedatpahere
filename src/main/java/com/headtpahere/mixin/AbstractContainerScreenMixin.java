package com.headtpahere.mixin;

import com.headtpahere.HeadTpaHere;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    // Runs every time a slot is clicked in any chest/menu-style GUI.
    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void headtpahere$onSlotClicked(Slot slot, int slotId, int mouseButton, ClickType type, CallbackInfo ci) {
        if (HeadTpaHere.onSlotClicked(slot, type)) {
            ci.cancel();
        }
    }
}
