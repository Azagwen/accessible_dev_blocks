package net.azagwen.accessible_dev_blocks.mixin;

import net.azagwen.accessible_dev_blocks.ADBParticleTypes;
import net.azagwen.accessible_dev_blocks.StructureVoidParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    private final ParticleManager self = (ParticleManager) (Object) this;

    @Inject(method = "registerDefaultFactories()V",
            at = @At(value = "TAIL"))
    private void registerDefaultFactories(CallbackInfo cbi) {
        self.registerFactory(ADBParticleTypes.STRUCTURE_VOID, new StructureVoidParticle.Factory());
    }
}
