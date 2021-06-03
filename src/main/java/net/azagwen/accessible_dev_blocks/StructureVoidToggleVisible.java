package net.azagwen.accessible_dev_blocks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbAutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import static net.azagwen.accessible_dev_blocks.AdbClient.structureVoidCycleKey;

public class StructureVoidToggleVisible {
    private static ConfigHolder<AdbAutoConfig> config = AutoConfig.getConfigHolder(AdbAutoConfig.class);

    @Environment(EnvType.CLIENT)
    public static void toggle() {
        if (structureVoidCycleKey.isPressed()) {
            config.getConfig().struct_void_visibility = config.getConfig().struct_void_visibility.next();
            config.save();

            switch (config.getConfig().struct_void_visibility) {
                case VISIBLE:
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void is now §aVisible."), true);
                    break;
                case INVISIBLE:
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void is now §cHidden."), true);
                    break;
                default:
                    break;
            }
        }
    }

    public static VoxelShape shape() {
        switch (config.getConfig().struct_void_visibility) {
            case VISIBLE:
                return VoxelShapes.fullCube();
            case INVISIBLE:
            default:
                return VoxelShapes.empty();
        }
    }
}
