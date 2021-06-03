package net.azagwen.accessible_dev_blocks.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class AdbUtils {
    public static Box makeCenteredInflatableBox(BlockPos blockPos, int boxSize, float inflateFac) {
        float newBoxSize = (float)(boxSize / 16);
        float minBoxSize = 0.5F - (newBoxSize / 2);
        float maxBoxSize = 0.5F + (newBoxSize / 2);

        double minX = blockPos.getX() + minBoxSize - inflateFac;
        double minY = blockPos.getY() + minBoxSize - inflateFac;
        double minZ = blockPos.getZ() + minBoxSize - inflateFac;
        double maxX = blockPos.getX() + maxBoxSize + inflateFac;
        double maxY = blockPos.getY() + maxBoxSize + inflateFac;
        double maxZ = blockPos.getZ() + maxBoxSize + inflateFac;

        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static BlockPos createSubstractionBlockPos(BlockPos firstPos, BlockPos secondPos) {
        float X = firstPos.getX() - secondPos.getX();
        float Y = firstPos.getY() - secondPos.getY();
        float Z = firstPos.getZ() - secondPos.getZ();

        return new BlockPos(X, Y, Z);
    }

    @Environment(EnvType.CLIENT)
    public static void drawBox(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, ColorRGB color, float alpha) {
        WorldRenderer.drawBox(matrices, vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.redNormalized, color.greenNormalized, color.blueNormalized, alpha, color.redNormalized, color.greenNormalized, color.blueNormalized);
    }
}
