package net.azagwen.accessible_dev_blocks.screen.widget;

import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;

@Environment(EnvType.CLIENT)
public abstract class AdbOptionSliderWidget extends SliderWidget {
    protected final AdbGameOptions options;

    protected AdbOptionSliderWidget(AdbGameOptions options, int x, int y, int width, int height, double value) {
        super(x, y, width, height, LiteralText.EMPTY, value);
        this.options = options;
    }
}
