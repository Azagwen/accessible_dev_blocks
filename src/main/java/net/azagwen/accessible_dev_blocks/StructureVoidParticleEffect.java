package net.azagwen.accessible_dev_blocks;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class StructureVoidParticleEffect implements ParticleEffect {
    public static final StructureVoidParticleEffect RED = new StructureVoidParticleEffect(1.0F, 0.0F, 0.0F, 1.0F);

    public static final Codec<StructureVoidParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.FLOAT.fieldOf("r").forGetter((dustParticleEffect) -> {
            return dustParticleEffect.red;
        }), Codec.FLOAT.fieldOf("g").forGetter((dustParticleEffect) -> {
            return dustParticleEffect.green;
        }), Codec.FLOAT.fieldOf("b").forGetter((dustParticleEffect) -> {
            return dustParticleEffect.blue;
        }), Codec.FLOAT.fieldOf("scale").forGetter((dustParticleEffect) -> {
            return dustParticleEffect.scale;
        })).apply(instance, StructureVoidParticleEffect::new);
    });

    public static final ParticleEffect.Factory<StructureVoidParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<StructureVoidParticleEffect>() {
        public StructureVoidParticleEffect read(ParticleType<StructureVoidParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float f = (float)stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float)stringReader.readDouble();
            stringReader.expect(' ');
            float h = (float)stringReader.readDouble();
            stringReader.expect(' ');
            float i = (float)stringReader.readDouble();
            return new StructureVoidParticleEffect(f, g, h, i);
        }

        public StructureVoidParticleEffect read(ParticleType<StructureVoidParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new StructureVoidParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };
    private final float red;
    private final float green;
    private final float blue;
    private final float scale;

    public StructureVoidParticleEffect(float red, float green, float blue, float scale) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.scale = MathHelper.clamp(scale, 0.01F, 4.0F);
    }

    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.scale);
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), this.red, this.green, this.blue, this.scale);
    }

    public ParticleType<StructureVoidParticleEffect> getType() {
        return AdbParticleTypes.STRUCTURE_VOID;
    }

    @Environment(EnvType.CLIENT)
    public float getRed() {
        return this.red;
    }

    @Environment(EnvType.CLIENT)
    public float getGreen() {
        return this.green;
    }

    @Environment(EnvType.CLIENT)
    public float getBlue() {
        return this.blue;
    }

    @Environment(EnvType.CLIENT)
    public float getScale() {
        return this.scale;
    }
}
