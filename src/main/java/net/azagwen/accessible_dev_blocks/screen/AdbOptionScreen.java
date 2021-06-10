package net.azagwen.accessible_dev_blocks.screen;

import net.azagwen.accessible_dev_blocks.AdbMain;
import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.azagwen.accessible_dev_blocks.option.AdbOption;
import net.azagwen.accessible_dev_blocks.utils.AdbUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class AdbOptionScreen extends AdbScreen {
    private final Screen parent;
    private final AdbGameOptions settings;
    private ColoredButtonWidget openColorPickerButton;
    private ButtonWidget structVoidRenderModeButton;
    private StructureVoidRenderMode renderMode;
    protected int mainBlockOffsetY = 16;

    public AdbOptionScreen(Screen parent, AdbGameOptions settings) {
        super(new AdbOptionTranslatableText("title"));
        this.parent = parent;
        this.settings = settings;
        this.setTexture(AdbMain.CONFIG_BG_LOCATION);
    }

    @Override
    public void tick() {
        this.openColorPickerButton.setColor(AdbUtils.getColorFromSettings(this.settings));
    }

    protected int getVerticalSpacing(int row) {
        return this.height / 6 + (24 * row) -6;
    }

    protected void init() {
        this.renderMode = this.settings.structVoidRenderMode;

        //Middle
        this.openColorPickerButton = this.addButton(new ColoredButtonWidget(this.width / 2 + 64, this.getVerticalSpacing(3) - mainBlockOffsetY , 56, 20, new AdbOptionTranslatableText("openColorPicker"), AdbUtils.getColorFromSettings(this.settings), (button) -> {
            this.client.openScreen(new AdbOptionColorScreen(this, this.settings));
        }));
        this.structVoidRenderModeButton = this.addButton(new ButtonWidget(this.width / 2 - 120, this.getVerticalSpacing(3) - mainBlockOffsetY , 180, 20, this.getRenderModeButtonText(this.renderMode), (button) -> {
            this.renderMode = this.renderMode.next();
            this.settings.structVoidRenderMode = this.renderMode;
            this.settings.write();
            this.structVoidRenderModeButton.setMessage(this.getRenderModeButtonText(this.renderMode));
        }));
        this.addButton(AdbOption.STRUCT_VOID_RENDER_DIAMETER.createButton(this.settings, this.width / 2 - 120, this.getVerticalSpacing(4) - mainBlockOffsetY, 240));
        this.addButton(AdbOption.STRUCT_VOID_FADE_BORDERS.createButton(this.settings, this.width / 2 - 120, this.getVerticalSpacing(5) - mainBlockOffsetY, 240));

        //Bottom
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.getVerticalSpacing(7), 150, 20, new AdbOptionTranslatableText("reset"), (button) -> {
            this.settings.reset();
            this.settings.write();
            this.client.openScreen(this);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.getVerticalSpacing(7), 150, 20, new AdbOptionTranslatableText("applyAndClose"), (button) -> {
            this.client.openScreen(this.parent);
        }));
    }

    @Override
    public void removed() {
        this.settings.write();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.getVerticalSpacing(0), 16777215);
        drawCenteredText(matrices, this.textRenderer, new AdbOptionTranslatableText("title.structVoidRender"), this.width / 2, this.getVerticalSpacing(1) + 12, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private Text getRenderModeButtonText(StructureVoidRenderMode renderMode) {
        return (new AdbOptionTranslatableText("renderMode")).append(": ").append(renderMode.getTranslatableName());
    }
}