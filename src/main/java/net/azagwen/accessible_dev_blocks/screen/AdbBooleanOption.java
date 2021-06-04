package net.azagwen.accessible_dev_blocks.screen;

import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class AdbBooleanOption extends AdbOption {
    private final Predicate<AdbGameOptions> getter;
    private final BiConsumer<AdbGameOptions, Boolean> setter;
    @Nullable
    private final Text name;

    public AdbBooleanOption(String key, Predicate<AdbGameOptions> getter, BiConsumer<AdbGameOptions, Boolean> setter) {
        this(key, (Text)null, getter, setter);
    }

    public AdbBooleanOption(String string, @Nullable Text text, Predicate<AdbGameOptions> predicate, BiConsumer<AdbGameOptions, Boolean> biConsumer) {
        super(string);
        this.getter = predicate;
        this.setter = biConsumer;
        this.name = text;
    }

    public void set(AdbGameOptions options, String value) {
        this.set(options, "true".equals(value));
    }

    public void toggle(AdbGameOptions options) {
        this.set(options, !this.get(options));
        options.write();
    }

    private void set(AdbGameOptions options, boolean value) {
        this.setter.accept(options, value);
    }

    public boolean get(AdbGameOptions options) {
        return this.getter.test(options);
    }

    public AbstractButtonWidget createButton(AdbGameOptions options, int x, int y, int width) {
        if (this.name != null) {
            this.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(this.name, 200));
        }

        return new AdbOptionButtonWidget(x, y, width, 20, this, this.getDisplayString(options), (button) -> {
            this.toggle(options);
            button.setMessage(this.getDisplayString(options));
        });
    }

    public Text getDisplayString(AdbGameOptions options) {
        return ScreenTexts.composeToggleText(this.getDisplayPrefix(), this.get(options));
    }
}
