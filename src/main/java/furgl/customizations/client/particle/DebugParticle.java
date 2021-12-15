package furgl.customizations.client.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;

public class DebugParticle extends SpriteBillboardParticle {

	public static HashMap<DefaultParticleType, SpriteProvider> spriteProviders = Maps.newHashMap();
	private static HashMap<Entity, List<DebugParticle>> entitiesWithParticles = Maps.newHashMap();

	@Nullable
	private Entity entity;

	public DebugParticle(ParticleType particleType, ClientWorld clientWorld, double x, double y, double z) {
		this(particleType, clientWorld, x, y, z, 0, 0, 0, null);
	}

	public DebugParticle(ParticleType particleType, ClientWorld clientWorld, @Nullable Entity entity) {
		this(particleType, clientWorld, 0, 0, 0, 0, 0, 0, entity);
	}

	public DebugParticle(ParticleType particleType, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Nullable Entity entity) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.entity = entity;
		this.collidesWithWorld = false;
		this.setSprite(spriteProviders.get(particleType));
		this.maxAge = 60;
		this.scale = 0.4f;
		if (entity != null) {
			List<DebugParticle> list = entitiesWithParticles.containsKey(entity) ? entitiesWithParticles.get(entity) : Lists.newArrayList();
			list.add(this);
			entitiesWithParticles.put(entity, list);
		}
		this.updatePosition();
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
	}

	private void updatePosition() {
		if (entity != null) {
			float yOffset = 0.1f;
			if (entitiesWithParticles.containsKey(this.entity)) {
				List<DebugParticle> list = entitiesWithParticles.get(entity);
				list.removeIf(particle -> particle.dead);
				if (list.size() > 1)
					yOffset += list.indexOf(this)*0.4f;
			}

			this.prevPosX = this.x;
			this.prevPosY = this.y;
			this.prevPosZ = this.z;
			this.x = entity.getX();
			this.y = entity.getY()+entity.getHeight()+yOffset;
			this.z = entity.getZ();
		}
	}

	@Override
	public void tick() {
		if (this.age++ >= this.maxAge)
			this.markDead();
		else 
			this.updatePosition();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		public Factory(DefaultParticleType defaultParticleType, SpriteProvider spriteProvider) {
			DebugParticle.spriteProviders.put(defaultParticleType, spriteProvider);
		}

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld world, double x, double y, double z, double g, double h, double i) {
			return new DebugParticle(defaultParticleType, world, x, y, z);
		}
	}

}