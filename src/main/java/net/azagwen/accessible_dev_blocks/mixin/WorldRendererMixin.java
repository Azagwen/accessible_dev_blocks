package net.azagwen.accessible_dev_blocks.mixin;

import com.google.common.collect.Lists;
import net.azagwen.accessible_dev_blocks.ColorRGB;
import net.azagwen.accessible_dev_blocks.StructureVoidToggleVisible;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Random;

import static net.azagwen.accessible_dev_blocks.ADBMain.ADB_ID;

@SuppressWarnings("static-access")
@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Mutable @Final @Shadow private final MinecraftClient client;
    @Mutable @Final @Shadow private final BufferBuilderStorage bufferBuilders;
    private final WorldRenderer self = (WorldRenderer) (Object) this;
    private static final VoxelShape shape = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    private static final ColorRGB voidColor = new ColorRGB(64, 255, 230);
    private static final int radius = 32;

    public WorldRendererMixin(MinecraftClient client, BufferBuilderStorage bufferBuilders) {
        this.client = client;
        this.bufferBuilders = bufferBuilders;
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V",
            at = @At(value = "HEAD", args = {"log=true"}))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo cbi) {
        boolean isStructureVoidVisible = StructureVoidToggleVisible.VISIBILITY.equals(StructureVoidToggleVisible.STRUCTURE_VOID_VISIBILITY.VISIBLE_BOX_DRAW);
        boolean isPlayerCreative = this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
        VertexConsumer linesVertexConsumer = immediate.getBuffer(RenderLayer.getLines());
        Profiler profiler = self.world.getProfiler();
        Vec3d cameraVector = camera.getPos();

        self.checkEmpty(matrices);
        profiler.swap("adb_structure_void");

        BlockPos blockPos = this.client.player.getBlockPos();
        ArrayList<BlockPos> posList = Lists.newArrayList();
        for (int i = -(this.radius / 2); i < (this.radius / 2); i++) {
            for (int j = -(this.radius / 2); j < (this.radius / 2); j++) {
                for (int k = -(this.radius / 2); k < (this.radius / 2); k++) {
                    posList.add(blockPos.add(i, j, k));
                }
            }
        }

        for (BlockPos currentPos : posList) {
            BlockState blockState = self.world.getBlockState(currentPos);
            if (isStructureVoidVisible && blockState.isOf(Blocks.STRUCTURE_VOID) && isPlayerCreative) {
                this.drawBlockOutline(matrices, linesVertexConsumer, this.shape, cameraVector, currentPos, this.voidColor, 0.5F);
            }
        }
    }

    public final void drawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape shape, Vec3d cameraVector, BlockPos blockPos, ColorRGB color, float alpha) {
        self.drawShapeOutline(matrixStack, vertexConsumer, shape,
                (blockPos.getX() - cameraVector.getX()),
                (blockPos.getY() - cameraVector.getY()),
                (blockPos.getZ() - cameraVector.getZ()),
                color.redNormalized,
                color.greenNormalized,
                color.blueNormalized,
                alpha
        );
    }
}
