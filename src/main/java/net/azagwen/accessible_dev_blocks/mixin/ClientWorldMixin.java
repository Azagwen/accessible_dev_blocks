package net.azagwen.accessible_dev_blocks.mixin;

import net.azagwen.accessible_dev_blocks.AdbClient;
import net.azagwen.accessible_dev_blocks.StructureVoidParticleEffect;
import net.azagwen.accessible_dev_blocks.option.AdbGameOptions;
import net.azagwen.accessible_dev_blocks.screen.StructureVoidRenderMode;
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
    private AdbGameOptions settings = AdbClient.settings;

    @Inject(method = "doRandomBlockDisplayTicks(III)V",
            at = @At(value = "HEAD"))
    public void doRandomBlockDisplayTicks(int xCenter, int yCenter, int zCenter, CallbackInfo cbi) {
        boolean isStructureVoidVisible = this.settings.structVoidVisibility;
        boolean isStructureVoidParticle = this.settings.structVoidRenderMode.equals(StructureVoidRenderMode.PARTICLE);
        boolean isPlayerCreative = this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
        boolean isHoldingStructureVoid = false;

        for (ItemStack itemStack : this.client.player.getItemsHand()) {
            if (itemStack.getItem() == Blocks.STRUCTURE_VOID.asItem()) {
                isHoldingStructureVoid = true;
                break;
            }
        }

        int diameter = (int) this.settings.structVoidRenderDiameter;
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
                    boolean fadeBorders = this.settings.structVoidFadeBorders;

                    if (adjustedSphere > 0) {
                        this.spawParticles(currentPos, fadeBorders ? Math.max(0, adjustedSphere) : 1.0F);
                    }
                }
            });
        }
    }

    public void spawParticles(BlockPos pos, float size) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockState blockState = self.getBlockState(pos);

        if (blockState.isOf(Blocks.STRUCTURE_VOID)) {
            StructureVoidParticleEffect particle = new StructureVoidParticleEffect(
                    ((float) this.settings.structVoidColorRed) / 255,
                    ((float) this.settings.structVoidColorGreen) / 255,
                    ((float) this.settings.structVoidColorBlue) / 255,
                    size);
            self.addParticle(particle, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }
}
