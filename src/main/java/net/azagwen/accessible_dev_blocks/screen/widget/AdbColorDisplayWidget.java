package net.azagwen.accessible_dev_blocks.screen.widget;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

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
        fillGradient(matrices, this.x, this.y, this.x + width, this.y + height, 0xffffffff, 0xffffffff);
        fillGradient(matrices, this.x + 1, this.y + 1, this.x + width - 1, this.y + height - 1, color, color);
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return false;
    }

    public void setColor(int color) {
        this.color = color;
    }
}