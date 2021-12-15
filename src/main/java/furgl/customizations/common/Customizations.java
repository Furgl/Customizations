package furgl.customizations.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import furgl.customizations.client.ClientProxy;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.customizations.selectables.Selectables;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;

public class Customizations implements ModInitializer, ClientModInitializer {
	
	public static final String MODNAME = "Customizations";
	public static final String MODID = "customizations";

	public static final Logger LOGGER = LogManager.getLogger(MODNAME); 
	@Nullable
	public static MinecraftServer server;
	public static CommonProxy proxy;
	
	static {
		Selectables.ALL_SELECTABLES.getClass(); // initialize selectables right away so they stay in order
	}
	
	@Override
	public void onInitialize() {
		proxy = new CommonProxy();
		PacketManager.initServerPackets();
		FileConfig.init();
		ParticleManager.initServer();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		proxy = new ClientProxy();
		PacketManager.initClientPackets();
		ParticleManager.initClient();
	}

}