package net.azagwen.accessible_dev_blocks.cloth_config;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.math.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;

@Environment(EnvType.CLIENT)
public class AdbConfigTest {

    public AdbConfigTest() {

    }

    public enum structureVoidRenderMode {
        PARTICLE,
        BOX_OUTLINE
    }

    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslatableText("config.adb.title"));
        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/oak_planks.png"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("config.adb.category.main"));

        ModifierKeyCode toggleKey = ModifierKeyCode.of(InputUtil.Type.KEYSYM.createFromCode(GLFW_KEY_F4), Modifier.of(false, false, false));

        category.addEntry(entryBuilder.startSelector(new TranslatableText("config.adb.option.struct_void_render"), new LiteralText[] {new LiteralText("§aParticles"), new LiteralText("§eBox Outlines")}, structureVoidRenderMode.PARTICLE)
                .setDefaultValue(() -> structureVoidRenderMode.PARTICLE)
                .build()
        );
        category.addEntry(entryBuilder.startColorField(new TranslatableText("config.adb.option.struct_void_wireframe_color"), Color.ofRGB(64, 255, 230))
                .build());
        category.addEntry(entryBuilder.startModifierKeyCodeField(new TranslatableText("config.adb.option.struct_void_toggle_key"), toggleKey)
                .setDefaultValue(toggleKey)
                .build());

        return builder;
    }
}
