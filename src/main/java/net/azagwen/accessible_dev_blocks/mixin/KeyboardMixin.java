package net.azagwen.accessible_dev_blocks.mixin;

import net.azagwen.accessible_dev_blocks.StructureVoidToggleVisible;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void keyPress(long window, int key, int scancode, int i, int j, CallbackInfo ci) {
        StructureVoidToggleVisible.toggle();
    }
}
