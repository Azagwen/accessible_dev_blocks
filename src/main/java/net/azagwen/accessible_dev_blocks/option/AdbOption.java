package net.azagwen.accessible_dev_blocks.option;

import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.azagwen.accessible_dev_blocks.screen.AdbBooleanOption;
import net.azagwen.accessible_dev_blocks.screen.AdbDoubleOption;
import net.azagwen.accessible_dev_blocks.screen.AdbOptionTranslatableText;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.*;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AdbOption {
    public static final AdbDoubleOption STRUCT_VOID_RENDER_DIAMETER = new AdbDoubleOption("structVoidRenderDiam", 8.0F, 128.0F, 1.0F, (adbGameOptions) ->
    {
        return adbGameOptions.structVoidRenderDiameter;
    }, (adbGameOptions, diameter) -> adbGameOptions.structVoidRenderDiameter = diameter, (adbGameOptions, option) ->
    {
        double currentValue = option.get(adbGameOptions);
        if (currentValue == 16.0F) {
            return option.getGenericLabel(new AdbOptionTranslatableText("structVoidRenderDiam.default"));
        } else if (currentValue > 32 && currentValue <= 64){
            return option.getGenericLabel(new LiteralText("§e" + (int) currentValue));
        } else if (currentValue > 64 && currentValue <= 96){
            return option.getGenericLabel(new LiteralText("§6" + (int) currentValue));
        } else if (currentValue > 96){
            return option.getGenericLabel(new LiteralText("§c" + (int) currentValue));
        } else {
            return option.getGenericLabel(new LiteralText("§a" + (int) currentValue));
        }
    });
    public static final AdbBooleanOption STRUCT_VOID_FADE_BORDERS = new AdbBooleanOption("structVoidFadeBorders", (gameOptions) -> {
        return gameOptions.structVoidFadeBorders;
    }, (gameOptions, fadeBorders) -> {
        gameOptions.structVoidFadeBorders = fadeBorders;
    });
    public static final AdbDoubleOption STRUCT_VOID_BOX_COLOR_RED = new AdbDoubleOption("structVoidBoxColorRed", 0.0F, 255.0F, 1.0F, (adbGameOptions) ->
    {
        return adbGameOptions.structVoidBoxColorRed;
    }, (adbGameOptions, value) -> adbGameOptions.structVoidBoxColorRed = value, (adbGameOptions, option) ->
    {
        double currentValue = option.get(adbGameOptions);
        return option.getGenericLabel(new LiteralText("§c" + (int)currentValue));
    });
    public static final AdbDoubleOption STRUCT_VOID_BOX_COLOR_GREEN = new AdbDoubleOption("structVoidBoxColorGreen", 0.0F, 255.0F, 1.0F, (adbGameOptions) ->
    {
        return adbGameOptions.structVoidBoxColorGreen;
    }, (adbGameOptions, value) -> adbGameOptions.structVoidBoxColorGreen = value, (adbGameOptions, option) ->
    {
        double currentValue = option.get(adbGameOptions);
        return option.getGenericLabel(new LiteralText("§a" + (int)currentValue));
    });
    public static final AdbDoubleOption STRUCT_VOID_BOX_COLOR_BLUE = new AdbDoubleOption("structVoidBoxColorBlue", 0.0F, 255.0F, 1.0F, (adbGameOptions) ->
    {
        return adbGameOptions.structVoidBoxColorBlue;
    }, (adbGameOptions, value) -> adbGameOptions.structVoidBoxColorBlue = value, (adbGameOptions, option) ->
    {
        double currentValue = option.get(adbGameOptions);
        return option.getGenericLabel(new LiteralText("§9" + (int)currentValue));
    });

    private final Text key;
    private Optional<List<OrderedText>> tooltip = Optional.empty();

    public AdbOption(String key) {
        this.key = new AdbOptionTranslatableText(key);
    }

    public abstract AbstractButtonWidget createButton(AdbGameOptions options, int x, int y, int width);

    protected Text getDisplayPrefix() {
        return this.key;
    }

    public void setTooltip(List<OrderedText> tooltip) {
        this.tooltip = Optional.of(tooltip);
    }

    public Optional<List<OrderedText>> getTooltip() {
        return this.tooltip;
    }

    protected Text getPixelLabel(int pixel) {
        return new AdbOptionTranslatableText("pixel_value", this.getDisplayPrefix(), pixel);
    }

    protected Text getPercentLabel(double proportion) {
        return new AdbOptionTranslatableText("percent_value", this.getDisplayPrefix(), (int)(proportion * 100.0D));
    }

    protected Text getPercentAdditionLabel(int percentage) {
        return new AdbOptionTranslatableText("percent_add_value", this.getDisplayPrefix(), percentage);
    }

    protected Text getGenericLabel(Text value) {
        return new AdbOptionTranslatableText("generic_value", this.getDisplayPrefix(), value);
    }

    protected Text getGenericLabel(int value) {
        return this.getGenericLabel(new LiteralText(Integer.toString(value)));
    }
}
