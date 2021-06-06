package net.azagwen.accessible_dev_blocks.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.azagwen.accessible_dev_blocks.AdbMain;
import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.azagwen.accessible_dev_blocks.option.AdbOption;
import net.azagwen.accessible_dev_blocks.screen.widget.AdbColorDisplayWidget;
import net.azagwen.accessible_dev_blocks.screen.widget.AdbDoubleOptionSliderWidget;
import net.azagwen.accessible_dev_blocks.utils.AdbUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_ID;

public class AdbOptionColorScreen extends AdbScreen {
    private static final Identifier LIGHT_WIDGET_LOCATION = ADB_ID("textures/gui/widgets_light.png");
    private static final Identifier HEX_WARNING_LOCATION = ADB_ID("textures/gui/indicators.png");
    private final Screen parent;
    private final AdbGameOptions settings;
    private AdbColorDisplayWidget colorDisplayWidget;
    private AdbDoubleOptionSliderWidget redColorSliderWidget;
    private AdbDoubleOptionSliderWidget greenColorSliderWidget;
    private AdbDoubleOptionSliderWidget blueColorSliderWidget;
    private TextFieldWidget hexCodeField;
    private boolean isValidHexColor;
    private AdbOptionScreen parentScreen;

    protected AdbOptionColorScreen(Screen parent, AdbGameOptions settings) {
        super(new AdbOptionTranslatableText("title.structVoidBoxColor"));
        this.parent = parent;
        this.settings = settings;
        if (this.parent instanceof AdbOptionScreen) {
            this.parentScreen = (AdbOptionScreen) this.parent;
        } else {
            this.parentScreen = new AdbOptionScreen(this, this.settings);
        }
        this.setTexture(AdbMain.CONFIG_BG_LOCATION);
    }

    @Override
    public void tick() {
        this.hexCodeField.tick();
        Color color = AdbUtils.getBoxColorFromSettings(this.settings);
        this.colorDisplayWidget.setColor(color);
        this.redColorSliderWidget.setColor(color);
        this.greenColorSliderWidget.setColor(color);
        this.blueColorSliderWidget.setColor(color);
        if (this.redColorSliderWidget.isHovered() || this.greenColorSliderWidget.isHovered() || this.blueColorSliderWidget.isHovered()) {
            this.hexCodeField.setText(AdbUtils.getHexFromColor(color));
        }
    }

    @Override
    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.colorDisplayWidget = this.addButton(new AdbColorDisplayWidget(this.width / 2 + 80, this.parentScreen.getVerticalSpacing(2), 20, 68, AdbUtils.getBoxColorFromSettings(settings)));
        this.redColorSliderWidget = (AdbDoubleOptionSliderWidget) this.addButton(AdbOption.STRUCT_VOID_BOX_COLOR_RED.createButton(this.settings, this.width / 2 - 100, this.parentScreen.getVerticalSpacing(2), 170));
        this.greenColorSliderWidget = (AdbDoubleOptionSliderWidget) this.addButton(AdbOption.STRUCT_VOID_BOX_COLOR_GREEN.createButton(this.settings, this.width / 2 - 100, this.parentScreen.getVerticalSpacing(3), 170));
        this.blueColorSliderWidget = (AdbDoubleOptionSliderWidget) this.addButton(AdbOption.STRUCT_VOID_BOX_COLOR_BLUE.createButton(this.settings, this.width / 2 - 100, this.parentScreen.getVerticalSpacing(4), 170));
        this.redColorSliderWidget.setTexture(LIGHT_WIDGET_LOCATION);
        this.greenColorSliderWidget.setTexture(LIGHT_WIDGET_LOCATION);
        this.blueColorSliderWidget.setTexture(LIGHT_WIDGET_LOCATION);
        this.hexCodeField = new TextFieldWidget(this.textRenderer, this.width / 2 - 99, this.parentScreen.getVerticalSpacing(5) + 1, 198, 18, new AdbOptionTranslatableText("textInput.hexCode"));
        this.hexCodeField.setMaxLength(7);
        this.hexCodeField.setChangedListener(this::onHexCodeChanged);
        this.hexCodeField.setText(AdbUtils.getHexFromColor(AdbUtils.getBoxColorFromSettings(this.settings)));
        this.children.add(this.hexCodeField);

        //Bottom
        this.addButton(new ButtonWidget(this.width / 2 - 75, parentScreen.getVerticalSpacing(7), 150, 20, new AdbOptionTranslatableText("applyAndClose"), (button) -> {
            this.client.openScreen(this.parent);
        }));
    }

    @Override
    public void removed() {
        this.settings.write();
        this.client.keyboard.setRepeatEvents(true);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.parentScreen.getVerticalSpacing(0), 16777215);
        this.hexCodeField.render(matrices, mouseX, mouseY, delta);
        this.renderHexFieldWarning(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void renderHexFieldWarning(MatrixStack matrices) {
        this.client.getTextureManager().bindTexture(HEX_WARNING_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        drawTexture(matrices, this.width / 2 + 80, this.parentScreen.getVerticalSpacing(5), (isValidHexColor ? 20 : 0), 0, 20, 20, 64, 64);
    }

    private void onHexCodeChanged(String text) {
        if (this.hexCodeField.getText().length() >= 6) {
            if (this.hexCodeField.getText().length() < 7) {
                text = "#" + text;
            } else {
                text = text;
            }
            try {
                this.isValidHexColor = true;
                this.settings.structVoidColor = text;
                this.settings.write();

                Color color = Color.decode(text);
                this.redColorSliderWidget.setValue(((double) color.getRed()) / 255);
                this.greenColorSliderWidget.setValue(((double) color.getGreen()) / 255);
                this.blueColorSliderWidget.setValue(((double) color.getBlue()) / 255);
                this.redColorSliderWidget.refreshMessage();
                this.greenColorSliderWidget.refreshMessage();
                this.blueColorSliderWidget.refreshMessage();

            } catch (NumberFormatException e) {
                this.isValidHexColor = false;
//                AdbMain.LOGGER.warn("\"" + text + "\"" + "is not a valid Hexadecimal Color.");
            }
        } else {
            this.isValidHexColor = false;
        }
    }
}
