package furgl.customizations.common;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.client.particle.DebugParticle;
import furgl.customizations.common.customizations.context.Context.Type;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleManager {
	
	public static final DefaultParticleType CAUSE = FabricParticleTypes.simple();
	public static final DefaultParticleType TARGET = FabricParticleTypes.simple();
	public static final DefaultParticleType SUBJECT = FabricParticleTypes.simple();

	public static void initServer() {
		// register types
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(Customizations.MODID, "cause"), CAUSE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(Customizations.MODID, "target"), TARGET);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(Customizations.MODID, "subject"), SUBJECT);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		// register textures
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier(Customizations.MODID, "particle/cause"));
            registry.register(new Identifier(Customizations.MODID, "particle/target"));
            registry.register(new Identifier(Customizations.MODID, "particle/subject"));
        }));
		// register behavior
		ParticleFactoryRegistry.getInstance().register(CAUSE, spriteProvider -> new DebugParticle.Factory(CAUSE, spriteProvider));
		ParticleFactoryRegistry.getInstance().register(TARGET, spriteProvider -> new DebugParticle.Factory(TARGET, spriteProvider));
		ParticleFactoryRegistry.getInstance().register(SUBJECT, spriteProvider -> new DebugParticle.Factory(SUBJECT, spriteProvider));
	}

	@Nullable
	public static ParticleType getParticle(Type type) {
		if (type == Type.CAUSE)
			return CAUSE;
		else if (type == Type.TARGET)
			return TARGET;
		else if (type == Type.SUBJECT)
			return SUBJECT;
		else
			return null;
	}

}