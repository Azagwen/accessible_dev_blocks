package net.azagwen.accessible_dev_blocks.screen.widget;

import net.azagwen.accessible_dev_blocks.option.AdbOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AdbOptionButtonWidget extends ButtonWidget implements OrderableTooltip {
    private final AdbOption option;

    public AdbOptionButtonWidget(int x, int y, int width, int height, AdbOption option, Text text, PressAction pressAction) {
        super(x, y, width, height, text, pressAction);
        this.option = option;
    }

    public AdbOption getOption() {
        return this.option;
    }

    public Optional<List<OrderedText>> getOrderedTooltip() {
        return this.option.getTooltip();
    }
}