package net.azagwen.accessible_dev_blocks.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class AdbDoubleOption extends AdbOption {
    protected final float step;
    protected final double min;
    protected double max;
    private final Function<AdbGameOptions, Double> getter;
    private final BiConsumer<AdbGameOptions, Double> setter;
    private final BiFunction<AdbGameOptions, AdbDoubleOption, Text> displayStringGetter;

    public AdbDoubleOption(String key, double min, double max, float step, Function<AdbGameOptions, Double> getter, BiConsumer<AdbGameOptions, Double> setter, BiFunction<AdbGameOptions, AdbDoubleOption, Text> displayStringGetter) {
        super(key);
        this.min = min;
        this.max = max;
        this.step = step;
        this.getter = getter;
        this.setter = setter;
        this.displayStringGetter = displayStringGetter;
    }

    public AbstractButtonWidget createButton(AdbGameOptions options, int x, int y, int width) {
        return new AdbDoubleOptionSliderWidget(options, x, y, width, 20, this);
    }

    public double getRatio(double value) {
        return MathHelper.clamp((this.adjust(value) - this.min) / (this.max - this.min), 0.0D, 1.0D);
    }

    public double getValue(double ratio) {
        return this.adjust(MathHelper.lerp(MathHelper.clamp(ratio, 0.0D, 1.0D), this.min, this.max));
    }

    private double adjust(double value) {
        if (this.step > 0.0F) {
            value = (double)(this.step * (float)Math.round(value / (double)this.step));
        }

        return MathHelper.clamp(value, this.min, this.max);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public void setMax(float max) {
        this.max = (double)max;
    }

    public void set(AdbGameOptions options, double value) {
        this.setter.accept(options, value);
    }

    public double get(AdbGameOptions options) {
        return (Double)this.getter.apply(options);
    }

    public Text getDisplayString(AdbGameOptions options) {
        return (Text)this.displayStringGetter.apply(options, this);
    }
}