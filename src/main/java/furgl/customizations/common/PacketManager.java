package furgl.customizations.common;

import java.util.UUID;

import furgl.customizations.client.config.Config;
import furgl.customizations.client.config.WaitingScreen;
import furgl.customizations.client.particle.DebugParticle;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.mixin.WorldAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class PacketManager {

	// server -> client
	public static final Identifier SEND_CONFIG_TO_CLIENT = new Identifier(Customizations.MODID, "send_config_to_client");
	public static final Identifier DEBUG_VISUALS = new Identifier(Customizations.MODID, "debug_visuals");

	/**Server -> Client packets*/
	@Environment(EnvType.CLIENT)
	public static void initClientPackets() {
		// read config from server
		ClientPlayNetworking.registerGlobalReceiver(SEND_CONFIG_TO_CLIENT, (client, handler, buf, responseSender) -> {
			boolean hasPermission = buf.readBoolean();
			String serverConfig = buf.readString();

			client.execute(() -> {
				// has permission - read config from server and open config screen
				if (hasPermission) {
					// read values from server config
					FileConfig.readFromString(serverConfig);
					// open config
					if (client.currentScreen instanceof WaitingScreen)
						client.openScreen(Config.createConfigScreen(((WaitingScreen)client.currentScreen).parent, true));
				}
				// no permission - show error
				else if (client.currentScreen instanceof WaitingScreen) 
					((WaitingScreen)client.currentScreen).setText(new TranslatableText("config."+Customizations.MODID+".multiplayerWaitingScreen.error").formatted(Formatting.RED));
			});
		});
		// customization activated - display visuals on client
		ClientPlayNetworking.registerGlobalReceiver(DEBUG_VISUALS, (client, handler, buf, responseSender) -> {
			int typeIndex = buf.readInt();
			Context.Type type = typeIndex >= 0 && typeIndex < Context.Type.values().length ? Context.Type.values()[typeIndex] : null;
			ParticleType particle = ParticleManager.getParticle(type);
			UUID uuid = null;
			try {
				uuid = UUID.fromString(buf.readString());
			}
			catch (Exception e) {}
			Entity entity = uuid == null ? null : ((WorldAccessor)client.world).getEntityLookup().get(uuid);
			boolean isBlock = buf.readBoolean();
			boolean hasPos = buf.readBoolean();
			Vec3d tmpPos = null;
			if (hasPos) 
				tmpPos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			final Vec3d pos = tmpPos;
			client.execute(() -> {
				if (particle != null) {
					// outline entity
					if (entity != null) {
						if (entity instanceof LivingEntity)
							((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, true, false));
						client.particleManager.addParticle(new DebugParticle(ParticleManager.getParticle(type), client.world, entity));
					}
					// highlight block
					else if (isBlock && pos != null) {
						client.particleManager.addParticle(new DebugParticle(ParticleManager.getParticle(type), client.world, pos.x+0.5d, pos.y+1d, pos.z+0.5d));
					}
					// label location
					else if (pos != null) {
						client.particleManager.addParticle(new DebugParticle(ParticleManager.getParticle(type), client.world, pos.x+0.5d, pos.y+0.5d, pos.z+0.5d));
					}
				}
			});
		});
	}

	// client -> server
	public static final Identifier CONFIG_WAITING_SCREEN = new Identifier(Customizations.MODID, "config_waiting_screen");
	public static final Identifier SEND_CONFIG_TO_SERVER = new Identifier(Customizations.MODID, "send_config_to_server");

	/**Client -> Server packets*/
	public static void initServerPackets() {
		// client opened config screen in multiplayer
		ServerPlayNetworking.registerGlobalReceiver(CONFIG_WAITING_SCREEN, (server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
				// if player is opped, send them server config
				if (canPlayerEditServerConfig(server, player)) {
					PacketByteBuf buf2 = PacketByteBufs.create();
					buf2.writeBoolean(true); // is opped
					buf2.writeString(FileConfig.writeToString()); // send config text
					ServerPlayNetworking.send(player, PacketManager.SEND_CONFIG_TO_CLIENT, buf2);
				}
				// if player is not opped, show them error
				else {
					PacketByteBuf buf2 = PacketByteBufs.create();
					buf2.writeBoolean(false); // is opped
					buf2.writeString(""); // blank config 
					ServerPlayNetworking.send(player, PacketManager.SEND_CONFIG_TO_CLIENT, buf2);
				}
			});
		});
		// client opened config screen in multiplayer and edited it - update server's config
		ServerPlayNetworking.registerGlobalReceiver(SEND_CONFIG_TO_SERVER, (server, player, handler, buf, responseSender) -> {
			String config = buf.readString();

			server.execute(() -> {
				if (canPlayerEditServerConfig(server, player)) {
					FileConfig.writeToFile(config);
					FileConfig.readFromString(config);
					Customizations.LOGGER.info(player.getName()+" edited the server's config");
				}
			});
		});
	}

	/**Can this player edit the server's config in multiplayer*/
	public static boolean canPlayerEditServerConfig(MinecraftServer server, PlayerEntity player) {
		return server.getPlayerManager().isOperator(player.getGameProfile());
	}

}