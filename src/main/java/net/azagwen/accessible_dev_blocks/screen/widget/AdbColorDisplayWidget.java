package net.azagwen.accessible_dev_blocks.screen.widget;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.awt.*;

public class AdbColorDisplayWidget extends AbstractButtonWidget {
    protected int color;
    protected int width;
    protected int height;

    public AdbColorDisplayWidget(int x, int y, int width, int height, int color) {
        super(x, y, width, height, new LiteralText("colorDisplay"));
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int black = new Color(0, 0, 0).hashCode();
        Color invertedColor = new Color(color);
        int invertedRed = -((invertedColor.getRed() - 255));
        int invertedGreen = -((invertedColor.getGreen() - 255));
        int invertedBlue = -((invertedColor.getBlue() - 255));
        int grayscale = (invertedRed + invertedGreen + invertedBlue) / 3;
        int maxedChannel = grayscale < 140 ? 0 : 255;
        invertedColor = new Color(maxedChannel, maxedChannel, maxedChannel);

        int adjustedWidth = (this.width / 2) - 5;
        int adjustedHeight = (this.height / 2) - 5;

        fill(matrices, this.x, this.y, this.x + width, this.y + height, invertedColor.hashCode());
        fill(matrices, this.x + 1, this.y + 1, this.x + width - 1, this.y + height - 1, color);

        //TODO: Was too lazy to add a texture for this
        fill(matrices, (this.x + 7) + adjustedWidth, (this.y + 0) + adjustedHeight, (this.x + 7 + 2) + adjustedWidth, (this.y + 0 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 9) + adjustedWidth, (this.y + 1) + adjustedHeight, (this.x + 9 + 1) + adjustedWidth, (this.y + 1 + 2) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 6) + adjustedWidth, (this.y + 1) + adjustedHeight, (this.x + 6 + 1) + adjustedWidth, (this.y + 1 + 3) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 4) + adjustedWidth, (this.y + 1) + adjustedHeight, (this.x + 4 + 1) + adjustedWidth, (this.y + 1 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 5) + adjustedWidth, (this.y + 2) + adjustedHeight, (this.x + 5 + 1) + adjustedWidth, (this.y + 2 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 4) + adjustedWidth, (this.y + 3) + adjustedHeight, (this.x + 4 + 1) + adjustedWidth, (this.y + 3 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 7) + adjustedWidth, (this.y + 3) + adjustedHeight, (this.x + 7 + 2) + adjustedWidth, (this.y + 3 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 3) + adjustedWidth, (this.y + 4) + adjustedHeight, (this.x + 3 + 1) + adjustedWidth, (this.y + 4 + 2) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 7) + adjustedWidth, (this.y + 4) + adjustedHeight, (this.x + 7 + 1) + adjustedWidth, (this.y + 4 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 2) + adjustedWidth, (this.y + 5) + adjustedHeight, (this.x + 3 + 1) + adjustedWidth, (this.y + 5 + 2) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 6) + adjustedWidth, (this.y + 5) + adjustedHeight, (this.x + 6 + 1) + adjustedWidth, (this.y + 5 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 8) + adjustedWidth, (this.y + 5) + adjustedHeight, (this.x + 8 + 1) + adjustedWidth, (this.y + 5 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 4) + adjustedWidth, (this.y + 6) + adjustedHeight, (this.x + 4 + 2) + adjustedWidth, (this.y + 6 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 3) + adjustedWidth, (this.y + 7) + adjustedHeight, (this.x + 3 + 2) + adjustedWidth, (this.y + 7 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 1) + adjustedWidth, (this.y + 7) + adjustedHeight, (this.x + 1 + 1) + adjustedWidth, (this.y + 7 + 2) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 2) + adjustedWidth, (this.y + 8) + adjustedHeight, (this.x + 2 + 1) + adjustedWidth, (this.y + 8 + 1) + adjustedHeight, invertedColor.hashCode());
        fill(matrices, (this.x + 0) + adjustedWidth, (this.y + 9) + adjustedHeight, (this.x + 0 + 1) + adjustedWidth, (this.y + 9 + 1) + adjustedHeight, invertedColor.hashCode());
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return false;
    }

    public void setColor(int color) {
        this.color = color;
    }
}