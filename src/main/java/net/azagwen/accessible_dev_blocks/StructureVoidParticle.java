package net.azagwen.accessible_dev_blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StructureVoidParticle extends SpriteBillboardParticle {

    private StructureVoidParticle(ClientWorld world, double x, double y, double z, StructureVoidParticleEffect parameters, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.setSprite(spriteProvider);
        this.colorRed = parameters.getRed();
        this.colorGreen = parameters.getGreen();
        this.colorBlue = parameters.getBlue();
        this.scale = parameters.getScale() / 2;
        this.gravityStrength = 0.0F;
        this.maxAge = 1;
        this.collidesWithWorld = false;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<StructureVoidParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(StructureVoidParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new StructureVoidParticle(world, x, y, z, parameters, this.spriteProvider);
        }
    }
}
