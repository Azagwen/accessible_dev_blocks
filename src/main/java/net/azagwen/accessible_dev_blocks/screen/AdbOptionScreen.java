package net.azagwen.accessible_dev_blocks.screen;

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
        //left row
        this.renderMode = this.settings.structVoidRenderMode;
        this.structVoidRenderModeButton = this.addButton(new ButtonWidget(this.width / 2 - 155, this.getVerticalSpacing(1), 310, 20, this.getRenderModeButtonText(this.renderMode), (button) -> {
            this.renderMode = this.renderMode.next();
            this.structVoidRenderModeButton.setMessage(this.getRenderModeButtonText(this.renderMode));
        }));
        this.addButton(AdbOption.STRUCT_VOID_RENDER_DIAMETER.createButton(this.settings, this.width / 2 - 155, this.getVerticalSpacing(2), 310));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.getVerticalSpacing(3), 150, 20, new AdbOptionTranslatableText("video"), (button) -> {
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 168, 100, 20, new AdbOptionTranslatableText("back"), (button) -> {
            this.client.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 55, this.height / 6 + 168, 100, 20, new AdbOptionTranslatableText("applyAndClose"), (button) -> {
            this.settings.structVoidRenderMode = this.renderMode;
            this.settings.write();
            this.client.openScreen(this.parent);
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private Text getRenderModeButtonText(StructureVoidRenderMode renderMode) {
        return (new AdbOptionTranslatableText("render_mode")).append(": ").append(renderMode.getTranslatableName());
    }
}