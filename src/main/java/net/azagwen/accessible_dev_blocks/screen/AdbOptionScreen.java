package net.azagwen.accessible_dev_blocks.screen;

import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.azagwen.accessible_dev_blocks.option.AdbOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class AdbOptionScreen extends Screen {
    private final Screen parent;
    private final AdbGameOptions settings;
    private ButtonWidget structVoidRenderModeButton;
    private StructureVoidRenderMode renderMode;
    private int mainBlockOffsetY = 16;

    public AdbOptionScreen(Screen parent, AdbGameOptions settings) {
        super(new AdbOptionTranslatableText("title"));
        this.parent = parent;
        this.settings = settings;
    }

    private int getVerticalSpacing(int row) {
        return this.height / 6 + (24 * row) -6;
    }

    protected void init() {
        this.settings.read();
        this.renderMode = this.settings.structVoidRenderMode;

        //Left side
        this.structVoidRenderModeButton = this.addButton(new ButtonWidget(this.width / 2 - 204, this.getVerticalSpacing(3) - mainBlockOffsetY , 200, 20, this.getRenderModeButtonText(this.renderMode), (button) -> {
            this.renderMode = this.renderMode.next();
            this.settings.structVoidRenderMode = this.renderMode;
            this.settings.write();
            this.structVoidRenderModeButton.setMessage(this.getRenderModeButtonText(this.renderMode));
        }));
        this.addButton(AdbOption.STRUCT_VOID_RENDER_DIAMETER.createButton(this.settings, this.width / 2 - 204, this.getVerticalSpacing(4) - mainBlockOffsetY, 200));
        this.addButton(AdbOption.STRUCT_VOID_FADE_BORDERS.createButton(this.settings, this.width / 2 - 204, this.getVerticalSpacing(5) - mainBlockOffsetY, 200));

        //Right side
        this.addButton(AdbOption.STRUCT_VOID_BOX_COLOR_RED.createButton(this.settings, this.width / 2 + 4, this.getVerticalSpacing(3) - mainBlockOffsetY, 200));
        this.addButton(AdbOption.STRUCT_VOID_BOX_COLOR_GREEN.createButton(this.settings, this.width / 2 + 4, this.getVerticalSpacing(4) - mainBlockOffsetY, 200));
        this.addButton(AdbOption.STRUCT_VOID_BOX_COLOR_BLUE.createButton(this.settings, this.width / 2 + 4, this.getVerticalSpacing(5) - mainBlockOffsetY, 200));

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
        drawCenteredText(matrices, this.textRenderer, new AdbOptionTranslatableText("title.structVoidRender"), this.width / 2 - 102, this.getVerticalSpacing(2) - mainBlockOffsetY, 16777215);
        drawCenteredText(matrices, this.textRenderer, new AdbOptionTranslatableText("title.structVoidBoxColor"), this.width / 2 + 102, this.getVerticalSpacing(2) - mainBlockOffsetY, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private Text getRenderModeButtonText(StructureVoidRenderMode renderMode) {
        return (new AdbOptionTranslatableText("renderMode")).append(": ").append(renderMode.getTranslatableName());
    }
}