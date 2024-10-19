package net.karen.mccourse.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class AlexandriteParticles extends TextureSheetParticle {
    protected AlexandriteParticles(ClientLevel pLevel, double pX, double pY, double pZ,
                                   SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        // Position of particle
        this.friction = 0.8f;
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;

        // Size of particle
        this.quadSize *= 0.75f; // 75% of size
        this.lifetime = 20; // 20 ticks
        this.setSpriteFromAge(spriteSet);

        // RGB colors of particle
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    // Return of particle
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        // Created constructor to set spriteSet
        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        // Created method to create custom particles
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed) {
            return new AlexandriteParticles(level, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }

    }

}
