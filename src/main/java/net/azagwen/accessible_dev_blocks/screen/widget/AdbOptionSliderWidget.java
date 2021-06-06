package net.azagwen.accessible_dev_blocks.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.awt.*;

@Environment(EnvType.CLIENT)
public abstract class AdbOptionSliderWidget extends SliderWidget {
    protected final AdbGameOptions options;
    public Identifier WIDGETS_LOCATION;
    private Color bgColor;

    protected AdbOptionSliderWidget(AdbGameOptions options, int x, int y, int width, int height, double value, Color color) {
        super(x, y, width, height, LiteralText.EMPTY, value);
        this.options = options;
        this.bgColor = color;
        this.WIDGETS_LOCATION = new Identifier("textures/gui/widgets.png");
    }

    @Override
    protected void renderBg(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(WIDGETS_LOCATION);
        float red = ((float) this.bgColor.getRed()) / 255;
        float green = ((float) this.bgColor.getGreen()) / 255;
        float blue = ((float) this.bgColor.getBlue()) / 255;
        RenderSystem.color4f(red, green, blue, 1.0F);
        int yOffset = (this.active ? (this.isHovered() ? 2 : 1) : 0) * 20;
        this.drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)), this.y, 0, 46 + yOffset, 4, 20);
        this.drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)) + 4, this.y, 196, 46 + yOffset, 4, 20);
    }

    public void setColor(Color color) {
        this.bgColor = color;
    }

    public void setTexture(Identifier textureLocation) {
        this.WIDGETS_LOCATION = textureLocation;
    }
}
