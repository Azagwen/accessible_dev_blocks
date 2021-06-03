package net.azagwen.accessible_dev_blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemConvertible;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class StructureVoidParticle extends SpriteBillboardParticle {
    private StructureVoidParticle(ClientWorld world, double x, double y, double z, ItemConvertible itemConvertible) {
        super(world, x, y, z);
        this.setSprite(MinecraftClient.getInstance().getItemRenderer().getModels().getSprite(itemConvertible));
        this.gravityStrength = 0.0F;
        this.maxAge = 1;
        this.collidesWithWorld = false;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.TERRAIN_SHEET;
    }

    public float getSize(float tickDelta) {
        return 0.5F;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        public Factory() {
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new StructureVoidParticle(clientWorld, d, e, f, Blocks.STRUCTURE_VOID.asItem());
        }
    }
}
