package net.azagwen.accessible_dev_blocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.RenderLayer;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_NAMESPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;

@Environment(EnvType.CLIENT)
public class AdbClient implements ClientModInitializer {
    public static KeyBinding structureVoidCycleKey;
    public final String keyBindingCategory ="key.categories." + ADB_NAMESPACE;

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderLayer.getTranslucent());
        structureVoidCycleKey = new KeyBinding("key.structure_void.toggle", GLFW_KEY_F4, keyBindingCategory);
        KeyBindingHelper.registerKeyBinding(structureVoidCycleKey);
    }
}
