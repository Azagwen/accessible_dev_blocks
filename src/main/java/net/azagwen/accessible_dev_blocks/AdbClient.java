package net.azagwen.accessible_dev_blocks;

import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_NAMESPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;

@Environment(EnvType.CLIENT)
public class AdbClient implements ClientModInitializer {
    public static final Logger LOGGER  = LogManager.getLogger();
    public static KeyBinding structureVoidCycleKey;
    public final String keyBindingCategory ="key.categories." + ADB_NAMESPACE;
    public static AdbGameOptions settings;

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderLayer.getTranslucent());
        structureVoidCycleKey = new KeyBinding("key.structure_void.toggle", GLFW_KEY_F4, keyBindingCategory);
        KeyBindingHelper.registerKeyBinding(structureVoidCycleKey);

        MinecraftClient client = MinecraftClient.getInstance();
        settings = new AdbGameOptions(client, client.runDirectory);

        if (!settings.optionsFile.exists()) {
            LOGGER.info("No Options file found, creating one.");
            settings.write();
        }
        LOGGER.info("Accessible Developer blocks client initialised !");
    }
}
