package furgl.customizations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import furgl.customizations.config.FileConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class Customizations implements ModInitializer {

	public static final String MODNAME = "Customizations";
	public static final String MODID = "customizations";

	public static final Logger LOGGER = LogManager.getLogger(MODNAME); 
	@Nullable
	public static MinecraftServer server;
	
	@Override
	public void onInitialize() {
		FileConfig.init();
		ServerLifecycleEvents.SERVER_STARTED.register(server -> Customizations.server = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> Customizations.server = null);
	}

}