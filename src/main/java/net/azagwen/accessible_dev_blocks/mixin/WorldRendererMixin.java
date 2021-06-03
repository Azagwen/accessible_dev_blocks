package net.azagwen.accessible_dev_blocks.mixin;

import com.google.common.collect.Lists;
import me.shedaniel.autoconfig.AutoConfig;
import net.azagwen.accessible_dev_blocks.AdbParticleTypes;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbAutoConfig;
import net.azagwen.accessible_dev_blocks.utils.ColorRGB;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
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

@SuppressWarnings("static-access")
@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Mutable @Final @Shadow private final MinecraftClient client;
    @Mutable @Final @Shadow private final BufferBuilderStorage bufferBuilders;
    private final WorldRenderer self = (WorldRenderer) (Object) this;
    private static final VoxelShape shape = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    private AdbAutoConfig config = AutoConfig.getConfigHolder(AdbAutoConfig.class).getConfig();


    public WorldRendererMixin(MinecraftClient client, BufferBuilderStorage bufferBuilders) {
        this.client = client;
        this.bufferBuilders = bufferBuilders;
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V",
            at = @At(value = "HEAD", args = { "log=true" }))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo cbi) {
        boolean isStructureVoidVisible = config.struct_void_visibility.equals(AdbAutoConfig.StructureVoidVisibility.VISIBLE);
        boolean isStructureVoidBox = config.struct_void_render_mode.equals(AdbAutoConfig.StructureVoidRenderMode.BOX_OUTLINE);
        boolean isPlayerCreative = this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
        boolean shouldRenderBox = isStructureVoidVisible && isStructureVoidBox && isPlayerCreative;

        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
        VertexConsumer linesVertexConsumer = immediate.getBuffer(RenderLayer.getLines());
        Profiler profiler = self.world.getProfiler();
        Vec3d cameraVector = camera.getPos();

        self.checkEmpty(matrices);
        profiler.swap("adb_structure_void");

        switch (config.struct_void_render_mode) {
            case PARTICLE:
                spawnParticles();
            case BOX_OUTLINE:
                renderBoxOutlines(matrices, linesVertexConsumer, cameraVector, shouldRenderBox);
        }
    }

    private void spawnParticles() {
        BlockPos playerBlockPos = this.client.player.getBlockPos();
        boolean isHoldingStructureVoid = false;

        if (this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE) {
            for (ItemStack itemStack : this.client.player.getItemsHand()) {
                if (itemStack.getItem() == Blocks.STRUCTURE_VOID.asItem()) {
                    isHoldingStructureVoid = true;
                    break;
                }
            }
        }

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        boolean isStructureVoidVisible = AutoConfig.getConfigHolder(AdbAutoConfig.class).getConfig().struct_void_visibility.equals(AdbAutoConfig.StructureVoidVisibility.VISIBLE);
        boolean isStructureVoidParticle = AutoConfig.getConfigHolder(AdbAutoConfig.class).getConfig().struct_void_render_mode.equals(AdbAutoConfig.StructureVoidRenderMode.PARTICLE);

        for(int j = 0; j < 667; ++j) {
            this.randomBlockDisplayTick(playerBlockPos, 16, (isHoldingStructureVoid && isStructureVoidVisible && isStructureVoidParticle), mutable);
            this.randomBlockDisplayTick(playerBlockPos, 32, (isHoldingStructureVoid && isStructureVoidVisible && isStructureVoidParticle), mutable);
        }
    }

    private void renderBoxOutlines(MatrixStack matrices, VertexConsumer linesVertexConsumer, Vec3d cameraVector, boolean shouldRender) {
        ColorRGB voidColor = new ColorRGB(
                config.struct_void_box_color.struct_void_box_color_red,
                config.struct_void_box_color.struct_void_box_color_green,
                config.struct_void_box_color.struct_void_box_color_blue
        );
        int diameter = config.struct_void_box_render_diameter;
        BlockPos playerBlockPos = this.client.player.getBlockPos();
        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(playerBlockPos, (diameter / 2), (diameter / 2), (diameter / 2));

        blockPosIterable.forEach(currentPos -> {
            BlockState blockState = self.world.getBlockState(currentPos);
            if (blockState.isOf(Blocks.STRUCTURE_VOID) && shouldRender) {
                int offsetX = playerBlockPos.getX() - currentPos.getX();
                int offsetY = playerBlockPos.getY() - currentPos.getY();
                int offsetZ = playerBlockPos.getZ() - currentPos.getZ();

                double alpha = Math.sqrt(Math.pow(Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2)), 2) + Math.pow(offsetZ, 2));
                float finalAlpha = -(((float) alpha) - ((float) diameter / 2));

                this.drawBlockOutline(matrices, linesVertexConsumer, this.shape, cameraVector, currentPos, voidColor, Math.max(0, (finalAlpha / ((float) diameter / 2))));
            }
        });
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

    public void randomBlockDisplayTick(BlockPos centerPos, int radius, boolean spawnStructureVoidParticles, BlockPos.Mutable pos) {
        int x = centerPos.getX() + self.world.random.nextInt(radius) - self.world.random.nextInt(radius);
        int y = centerPos.getY() + self.world.random.nextInt(radius) - self.world.random.nextInt(radius);
        int z = centerPos.getZ() + self.world.random.nextInt(radius) - self.world.random.nextInt(radius);
        pos.set(x, y, z);
        BlockState blockState = self.world.getBlockState(pos);

        if (spawnStructureVoidParticles && blockState.isOf(Blocks.STRUCTURE_VOID)) {
            self.world.addParticle(AdbParticleTypes.STRUCTURE_VOID, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }
}
