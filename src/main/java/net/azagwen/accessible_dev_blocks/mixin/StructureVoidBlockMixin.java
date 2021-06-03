package net.azagwen.accessible_dev_blocks.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.azagwen.accessible_dev_blocks.StructureVoidToggleVisible;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbAutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.StructureVoidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StructureVoidBlock.class)
public class StructureVoidBlockMixin {

    /**
     * @author allows dynamic change of the outline shape.
     */
    @Overwrite
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return StructureVoidToggleVisible.shape();
    }
}
