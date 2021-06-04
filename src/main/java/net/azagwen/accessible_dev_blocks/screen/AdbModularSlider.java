package net.azagwen.accessible_dev_blocks.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class AdbModularSlider extends AdbOptionSliderWidget {
    private TranslatableText sliderMessage;

    public AdbModularSlider(AdbGameOptions options, int x, int y, int width, int value, TranslatableText message) {
        super(options, x, y, width, 20, value);
        this.sliderMessage = message;
        this.updateMessage();
    }

    protected void updateMessage() {
        this.setMessage((sliderMessage).append(": ").append(new LiteralText(String.valueOf(this.value))));
    }

    protected void applyValue() {
        this.options.write();
    }
}