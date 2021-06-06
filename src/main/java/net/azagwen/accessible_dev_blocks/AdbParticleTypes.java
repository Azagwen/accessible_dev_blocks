package net.azagwen.accessible_dev_blocks;

import com.mojang.serialization.Codec;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_ID;

public class AdbParticleTypes {
    public static final ParticleType<StructureVoidParticleEffect> STRUCTURE_VOID;

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, ADB_ID(name), new DefaultParticleTypeSubClass(alwaysShow));
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> function) {
        return Registry.register(Registry.PARTICLE_TYPE, ADB_ID(name), new ParticleType<T>(false, factory) {
            public Codec<T> getCodec() {
                return function.apply(this);
            }
        });
    }

    static {
        STRUCTURE_VOID = register("structure_void", StructureVoidParticleEffect.PARAMETERS_FACTORY, (particleType) -> {
            return StructureVoidParticleEffect.CODEC;
        });
    }
}
