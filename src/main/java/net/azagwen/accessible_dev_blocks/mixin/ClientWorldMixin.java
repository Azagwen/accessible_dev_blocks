package net.azagwen.accessible_dev_blocks.mixin;

import net.azagwen.accessible_dev_blocks.ADBParticleTypes;
import net.azagwen.accessible_dev_blocks.ADBUtils;
import net.azagwen.accessible_dev_blocks.StructureVoidToggleVisible;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
    private final ClientWorld self = (ClientWorld) (Object) this;
    @Final @Shadow private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "doRandomBlockDisplayTicks(III)V",
            at = @At(value = "HEAD"))
    public void doRandomBlockDisplayTicks(int xCenter, int yCenter, int zCenter, CallbackInfo cbi) {
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
        boolean isStructureVoidVisible = StructureVoidToggleVisible.VISIBILITY.equals(StructureVoidToggleVisible.STRUCTURE_VOID_VISIBILITY.VISIBLE_PARTICLES);

        for(int j = 0; j < 667; ++j) {
            this.randomBlockDisplayTick(xCenter, yCenter, zCenter, 16, (isHoldingStructureVoid && isStructureVoidVisible), mutable);
            this.randomBlockDisplayTick(xCenter, yCenter, zCenter, 32, (isHoldingStructureVoid && isStructureVoidVisible), mutable);
        }
    }

    public void randomBlockDisplayTick(int xCenter, int yCenter, int zCenter, int radius, boolean spawnStructureVoidParticles, BlockPos.Mutable pos) {
        int x = xCenter + self.random.nextInt(radius) - self.random.nextInt(radius);
        int y = yCenter + self.random.nextInt(radius) - self.random.nextInt(radius);
        int z = zCenter + self.random.nextInt(radius) - self.random.nextInt(radius);
        pos.set(x, y, z);
        BlockState blockState = self.getBlockState(pos);

        if (spawnStructureVoidParticles && blockState.isOf(Blocks.STRUCTURE_VOID)) {
            self.addParticle(ADBParticleTypes.STRUCTURE_VOID, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }
}
