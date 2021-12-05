package furgl.customizations.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import furgl.customizations.client.ClientProxy;
import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.config.FileConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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
		ServerLifecycleEvents.SERVER_STARTED.register(server -> Customizations.server = server); 
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> Customizations.server = null);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		proxy = new ClientProxy();
		PacketManager.initClientPackets();
	}

}