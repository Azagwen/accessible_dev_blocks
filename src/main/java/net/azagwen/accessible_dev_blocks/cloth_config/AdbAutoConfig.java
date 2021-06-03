package net.azagwen.accessible_dev_blocks.cloth_config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.*;
import me.shedaniel.autoconfig.annotation.ConfigEntry.*;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;
import net.azagwen.accessible_dev_blocks.AdbMain;

@Config(name = AdbMain.ADB_NAMESPACE)
@Background(value = AdbMain.ADB_NAMESPACE + ":textures/gui/config_background.png")
public class AdbAutoConfig implements ConfigData {

    public enum StructureVoidRenderMode {
        PARTICLE,
        BOX_OUTLINE;
    }

    public enum StructureVoidVisibility
    {
        INVISIBLE,
        VISIBLE;

        public StructureVoidVisibility next()
        {
            //loop back to start if on last enum
            if(ordinal() + 1 == values().length)
            {
                return values()[0];
            }
            return values()[ordinal() + 1];
        }
    }

    @Excluded
    public StructureVoidVisibility struct_void_visibility = StructureVoidVisibility.INVISIBLE;

    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    public StructureVoidRenderMode struct_void_render_mode = StructureVoidRenderMode.PARTICLE;

    @BoundedDiscrete(min = 8, max = 128)
    public int struct_void_render_diameter = 16;

    @CollapsibleObject(startExpanded = true)
    public StructVoidBoxOptions struct_void_box_options = new StructVoidBoxOptions();

    public static class StructVoidBoxOptions {
        public boolean fade_borders = true;

        @CollapsibleObject(startExpanded = true)
        public StructVoidBoxColor struct_void_box_color = new StructVoidBoxColor();
    }

    public static class StructVoidBoxColor {
        @BoundedDiscrete(min = 0, max = 255)
        public int color_red = 64;

        @BoundedDiscrete(min = 0, max = 255)
        public int color_green = 255;

        @BoundedDiscrete(min = 0, max = 255)
        public int color_blue = 230;
    }
}
