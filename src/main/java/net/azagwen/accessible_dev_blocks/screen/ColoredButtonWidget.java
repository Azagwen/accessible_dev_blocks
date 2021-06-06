package net.azagwen.accessible_dev_blocks.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_ID;

public class ColoredButtonWidget extends ButtonWidget {
    public static final Identifier LIGHT_WIDGETS_LOCATION = ADB_ID("textures/gui/widgets_light.png");
    public static final ColoredButtonWidget.TooltipSupplier EMPTY = (button, matrices, mouseX, mouseY) -> {
    };
    private Color backgroundTint;

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, Color color, ColoredButtonWidget.PressAction onPress, ColoredButtonWidget.TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.backgroundTint = color;
    }

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, Color color, ColoredButtonWidget.PressAction onPress) {
        this(x, y, width, height, message, color, onPress, EMPTY);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        minecraftClient.getTextureManager().bindTexture(LIGHT_WIDGETS_LOCATION);
        float red = ((float) this.backgroundTint.getRed()) / 255;
        float green = ((float) this.backgroundTint.getGreen()) / 255;
        float blue = ((float) this.backgroundTint.getBlue()) / 255;
        RenderSystem.color4f(red, green, blue, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int yOffset = this.getYImage(this.isHovered());
        this.drawTexture(matrices, this.x, this.y, 0, 46 + yOffset * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + yOffset * 20, this.width / 2, this.height);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
        int textColor = this.active ? 16777215 : 10526880;
        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    public void setColor(Color color) {
        this.backgroundTint = color;
    }
}
