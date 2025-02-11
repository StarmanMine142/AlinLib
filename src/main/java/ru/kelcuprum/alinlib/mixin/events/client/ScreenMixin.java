package ru.kelcuprum.alinlib.mixin.events.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.alinlib.api.events.client.ScreenEvents;

@Mixin(value = Screen.class)
public class ScreenMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        ScreenEvents.SCREEN_RENDER.invoker().onScreenRender((Screen) (Object) this, guiGraphics, mouseX, mouseY, partialTick);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ScreenEvents.KEY_PRESS.invoker().onKeyPressed((Screen) (Object) this, keyCode, scanCode, modifiers, cir);
    }
}
