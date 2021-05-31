package net.azagwen.accessible_dev_blocks.mixin;

import net.azagwen.accessible_dev_blocks.ADBUtils;
import net.azagwen.accessible_dev_blocks.ColorRGB;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;

@Mixin(StructureBlockBlockEntityRenderer.class)
public class StructureBlockBlockEntityRendererMixin {

    /**
     * @author Makes the structure void render bigger, removes the air drawing a blue box (considered pointless).
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    private void method_3585(StructureBlockBlockEntity structureBlockBlockEntity, VertexConsumer vertexConsumer, BlockPos blockPos, boolean bl, MatrixStack matrixStack) {
        BlockView blockView = structureBlockBlockEntity.getWorld();
        BlockPos structureBlockEntityPos = structureBlockBlockEntity.getPos();
        BlockPos offsetBlockEntityPos = structureBlockEntityPos.add(blockPos);
        Iterator blockPosIterator = BlockPos.iterate(offsetBlockEntityPos, offsetBlockEntityPos.add(structureBlockBlockEntity.getSize()).add(-1, -1, -1)).iterator();

        while(true) {
            BlockPos currentPos;
            boolean isBlockAir;
            boolean isBlockVoid;
            do {
                if (!blockPosIterator.hasNext()) {
                    return;
                }

                currentPos = (BlockPos)blockPosIterator.next();
                BlockState blockState = blockView.getBlockState(currentPos);
                isBlockAir = blockState.isAir();
                isBlockVoid = blockState.isOf(Blocks.STRUCTURE_VOID);
            } while(!isBlockAir && !isBlockVoid);

            BlockPos subPos = ADBUtils.createSubstractionBlockPos(currentPos, structureBlockEntityPos);
            Box voidBox = ADBUtils.makeCenteredInflatableBox(subPos, 15, 0);
            ColorRGB voidColor = new ColorRGB(64, 255, 223);
            if (isBlockVoid) {
                ADBUtils.drawBox(matrixStack, vertexConsumer, voidBox, voidColor, 0.25F);
            }
        }
    }
}
