package net.azagwen.accessible_dev_blocks.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.awt.*;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_ID;

public class AdbColorDisplayWidget extends AbstractButtonWidget {
    public static final Identifier DROPPER_LOCATION = ADB_ID("textures/gui/dropper_icon.png");
    protected Color color;
    protected int width;
    protected int height;

    public AdbColorDisplayWidget(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, new LiteralText("colorDisplay"));
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color currentColor = new Color(
                this.active ? this.color.getRed() : this.color.getRed() / 4,
                this.active ? this.color.getGreen() : this.color.getGreen() / 4,
                this.active ? this.color.getBlue() : this.color.getBlue() / 4
        );
        Color maxedColor = this.color;
        int invertedRed = -((maxedColor.getRed() - 255));
        int invertedGreen = -((maxedColor.getGreen() - 255));
        int invertedBlue = -((maxedColor.getBlue() - 255));
        int grayscale = (invertedRed + invertedGreen + invertedBlue) / 3;
        int maxedChannel = grayscale < 140 ? 0 : 255;
        maxedColor = new Color(maxedChannel, maxedChannel, maxedChannel);

        int adjustedWidth = (this.width / 2) - 10;
        int adjustedHeight = (this.height / 2) - 10;

        fill(matrices, this.x, this.y, this.x + width, this.y + height, this.active ? maxedColor.hashCode() : Color.BLACK.hashCode());
        fill(matrices, this.x + 1, this.y + 1, this.x + width - 1, this.y + height - 1, currentColor.hashCode());

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getTextureManager().bindTexture(DROPPER_LOCATION);
        RenderSystem.color4f(maxedChannel, maxedChannel, maxedChannel, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        drawTexture(matrices, this.x + adjustedWidth, this.y + adjustedHeight, 0, 0, 20, 20, 64, 128);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return false;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}