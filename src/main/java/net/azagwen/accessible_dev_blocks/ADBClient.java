package net.azagwen.accessible_dev_blocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;

import static net.azagwen.accessible_dev_blocks.StructureVoidToggleVisible.structureVoidCycleKey;

@Environment(EnvType.CLIENT)
public class ADBClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderLayer.getTranslucent());
        KeyBindingRegistry.INSTANCE.register(structureVoidCycleKey);
    }
}
