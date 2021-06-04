package net.azagwen.accessible_dev_blocks.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.text.OrderedText;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AdbDoubleOptionSliderWidget extends AdbOptionSliderWidget implements OrderableTooltip {
    private final AdbDoubleOption option;

    public AdbDoubleOptionSliderWidget(AdbGameOptions gameOptions, int x, int y, int width, int height, AdbDoubleOption option) {
        super(gameOptions, x, y, width, height, ((float)option.getRatio(option.get(gameOptions))));
        this.option = option;
        this.updateMessage();
    }

    protected void applyValue() {
        this.option.set(this.options, this.option.getValue(this.value));
    }

    protected void updateMessage() {
        this.setMessage(this.option.getDisplayString(this.options));
    }

    public Optional<List<OrderedText>> getOrderedTooltip() {
        return this.option.getTooltip();
    }
}
