package net.azagwen.accessible_dev_blocks.mixin;

import net.azagwen.accessible_dev_blocks.screen.AdbOptionBoxColorScreen;
import net.azagwen.accessible_dev_blocks.screen.AdbOptionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_ID;

@Mixin(Screen.class)
public class ScreenMixin {
    private Screen self = (Screen) (Object) this;

    @Redirect(
            method = "renderBackgroundTexture(I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V",
                    args = {"log=false"}
                    )
    )
    public void redirectBindTexture(TextureManager manager, Identifier id) {
        if (MinecraftClient.getInstance().currentScreen instanceof AdbOptionScreen || MinecraftClient.getInstance().currentScreen instanceof AdbOptionBoxColorScreen) {
            manager.bindTexture(ADB_ID("textures/gui/config_background.png"));
        } else {
            manager.bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        }
    }
}
