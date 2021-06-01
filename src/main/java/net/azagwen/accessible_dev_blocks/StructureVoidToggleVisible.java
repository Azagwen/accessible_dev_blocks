package net.azagwen.accessible_dev_blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import static net.azagwen.accessible_dev_blocks.ADBClient.structureVoidCycleKey;

public class StructureVoidToggleVisible {
    public static VoxelShape voxelShape = VoxelShapes.empty();
    public static BlockRenderType renderType = BlockRenderType.INVISIBLE; // Unused

    public enum STRUCTURE_VOID_VISIBILITY
    {
        INVISIBLE,
        VISIBLE_PARTICLES,
        VISIBLE_BOX_DRAW;

        public STRUCTURE_VOID_VISIBILITY next()
        {
            //loop back to start if on last enum
            if(ordinal() + 1 == values().length)
            {
                return values()[0];
            }

            return values()[ordinal() + 1];
        }
    }

    //the current mode for the structure void block for the current client
    public static STRUCTURE_VOID_VISIBILITY VISIBILITY = STRUCTURE_VOID_VISIBILITY.INVISIBLE;

    @Environment(EnvType.CLIENT)
    public static void toggle() {
        //Changes the structure void visibility
        if (structureVoidCycleKey.isPressed()) {
            VISIBILITY = VISIBILITY.next();

            switch (VISIBILITY) {
                case VISIBLE_PARTICLES:
                    voxelShape = VoxelShapes.fullCube();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void Visibility set to §aVisible - Particles."), true);
                    break;
                case VISIBLE_BOX_DRAW:
                    voxelShape = VoxelShapes.fullCube();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void Visibility set to §aVisible - Box Draw."), true);
                    break;
                case INVISIBLE:
                    voxelShape = VoxelShapes.empty();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void Visibility set to §cHidden."), true);
                    break;
                default:
                    break;
            }
        }
    }
}
