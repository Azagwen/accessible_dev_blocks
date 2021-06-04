package net.azagwen.accessible_dev_blocks;

import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import static net.azagwen.accessible_dev_blocks.AdbClient.structureVoidCycleKey;

public class StructureVoidToggleVisible {
    private static AdbGameOptions settings = AdbClient.settings;

    @Environment(EnvType.CLIENT)
    public static void toggle() {
        if (structureVoidCycleKey.isPressed()) {
            settings.structVoidVisibility = !settings.structVoidVisibility;
            settings.write();

            if (settings.structVoidVisibility) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void is now §aVisible."), true);
            } else {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void is now §cHidden."), true);
            }
        }
    }

    public static VoxelShape shape() {
        return settings.structVoidVisibility ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }
}
