package furgl.customizations.common;

import furgl.customizations.client.config.Config;
import furgl.customizations.client.config.WaitingScreen;
import furgl.customizations.common.config.FileConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class PacketManager {

	// server -> client
	public static final Identifier SEND_CONFIG_TO_CLIENT = new Identifier(Customizations.MODID, "send_config_to_client");

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