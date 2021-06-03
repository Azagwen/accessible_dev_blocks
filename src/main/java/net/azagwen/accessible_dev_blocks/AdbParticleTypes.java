package net.azagwen.accessible_dev_blocks;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

import static net.azagwen.accessible_dev_blocks.AdbMain.ADB_ID;

public class AdbParticleTypes {
    public static final DefaultParticleType STRUCTURE_VOID = register("structure_void", false);

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, ADB_ID(name), new DefaultParticleTypeSubClass(alwaysShow));
    }
}
