package net.azagwen.accessible_dev_blocks.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.azagwen.accessible_dev_blocks.cloth_config.AdbAutoConfig;
import net.azagwen.accessible_dev_blocks.AdbParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Final @Shadow private final MinecraftClient client = MinecraftClient.getInstance();
    private final ClientWorld self = (ClientWorld) (Object) this;
    private AdbAutoConfig config = AutoConfig.getConfigHolder(AdbAutoConfig.class).getConfig();

    @Inject(method = "doRandomBlockDisplayTicks(III)V",
            at = @At(value = "HEAD"))
    public void doRandomBlockDisplayTicks(int xCenter, int yCenter, int zCenter, CallbackInfo cbi) {
        boolean isStructureVoidVisible = config.struct_void_visibility.equals(AdbAutoConfig.StructureVoidVisibility.VISIBLE);
        boolean isStructureVoidParticle = config.struct_void_render_mode.equals(AdbAutoConfig.StructureVoidRenderMode.PARTICLE);
        boolean isPlayerCreative = this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
        boolean isHoldingStructureVoid = false;

        for (ItemStack itemStack : this.client.player.getItemsHand()) {
            if (itemStack.getItem() == Blocks.STRUCTURE_VOID.asItem()) {
                isHoldingStructureVoid = true;
                break;
            }
        }

        int diameter = config.struct_void_render_diameter;
        BlockPos playerBlockPos = this.client.player.getBlockPos();
        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(playerBlockPos, (diameter / 2), (diameter / 2), (diameter / 2));

        if (isStructureVoidVisible && isStructureVoidParticle && isHoldingStructureVoid && isPlayerCreative) {
            blockPosIterable.forEach(currentPos -> {
                BlockState blockState = self.getBlockState(currentPos);
                if (blockState.isOf(Blocks.STRUCTURE_VOID)) {
                    int offsetX = playerBlockPos.getX() - currentPos.getX();
                    int offsetY = playerBlockPos.getY() - currentPos.getY();
                    int offsetZ = playerBlockPos.getZ() - currentPos.getZ();

                    double sphere = Math.sqrt(Math.pow(Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2)), 2) + Math.pow(offsetZ, 2));
                    float newSphere = -(((float) sphere) - ((float) diameter / 2));
                    float adjustedSphere = (newSphere / ((float) diameter / 2));

                    if (adjustedSphere > 0) {
                        this.spawParticles(currentPos);
                    }
                }
            });
        }
    }

    public void spawParticles(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockState blockState = self.getBlockState(pos);

        if (blockState.isOf(Blocks.STRUCTURE_VOID)) {
            self.addParticle(AdbParticleTypes.STRUCTURE_VOID, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }
}
