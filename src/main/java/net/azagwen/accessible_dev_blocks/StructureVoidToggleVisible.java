package net.azagwen.accessible_dev_blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.lwjgl.glfw.GLFW;

import static net.azagwen.accessible_dev_blocks.ADBMain.ADB_ID;
import static net.azagwen.accessible_dev_blocks.ADBMain.ADB_NAMESPACE;

public class StructureVoidToggleVisible {
    public static VoxelShape voxelShape = VoxelShapes.empty();

    public enum STRUCTURE_VOID_VISIBILITY
    {
        INVISIBLE,
        VISIBLE;

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

    public static final FabricKeyBinding structureVoidCycleKey =  FabricKeyBinding.Builder.create(
            ADB_ID("structure_void.toggle"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "key.categories." + ADB_NAMESPACE).build();

    @Environment(EnvType.CLIENT)
    public static void toggle() {
        //Changes the structure void visibility
        if (structureVoidCycleKey.isPressed()) {
            VISIBILITY = VISIBILITY.next();

            switch (VISIBILITY) {
                case VISIBLE:
                    voxelShape = VoxelShapes.fullCube();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void is now §aVisible."), true);
                    break;
                case INVISIBLE:
                    voxelShape = VoxelShapes.empty();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("Structure Void is now §cHidden."), true);
                    break;
                default:
                    break;
            }
        }
    }
}
