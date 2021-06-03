package net.azagwen.accessible_dev_blocks.mixin;

import me.shedaniel.autoconfig.AutoConfig;
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
        boolean isStructureVoidBoxOutline = config.struct_void_render_mode.equals(AdbAutoConfig.StructureVoidRenderMode.BOX_OUTLINE);
        boolean isPlayerCreative = this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
        ColorRGB voidColor = new ColorRGB(
                config.struct_void_box_options.struct_void_box_color.color_red,
                config.struct_void_box_options.struct_void_box_color.color_green,
                config.struct_void_box_options.struct_void_box_color.color_blue
        );

        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
        VertexConsumer linesVertexConsumer = immediate.getBuffer(RenderLayer.getLines());
        Profiler profiler = self.world.getProfiler();
        Vec3d cameraVector = camera.getPos();

        self.checkEmpty(matrices);
        profiler.swap("adb_structure_void");

        int diameter = config.struct_void_render_diameter;
        BlockPos playerBlockPos = this.client.player.getBlockPos();
        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(playerBlockPos, (diameter / 2), (diameter / 2), (diameter / 2));

        if (isStructureVoidVisible && isStructureVoidBoxOutline && isPlayerCreative) {
            blockPosIterable.forEach(currentPos -> {
                BlockState blockState = self.world.getBlockState(currentPos);
                if (blockState.isOf(Blocks.STRUCTURE_VOID)) {
                    int offsetX = playerBlockPos.getX() - currentPos.getX();
                    int offsetY = playerBlockPos.getY() - currentPos.getY();
                    int offsetZ = playerBlockPos.getZ() - currentPos.getZ();

                    double alpha = Math.sqrt(Math.pow(Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2)), 2) + Math.pow(offsetZ, 2));
                    float newAlpha = -(((float) alpha) - ((float) diameter / 2));
                    float adjustedAlpha = (newAlpha / ((float) diameter / 2));
                    boolean fadeBorders = config.struct_void_box_options.fade_borders;

                    if (adjustedAlpha > 0) {
                        this.drawBlockOutline(matrices, linesVertexConsumer, this.shape, cameraVector, currentPos, voidColor, fadeBorders ? Math.max(0, adjustedAlpha) : 1.0F);
                    }
                }
            });
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
