package furgl.customizations.common;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class CommonProxy {

	public CommonProxy() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> Customizations.server = server); 
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> Customizations.server = null);
	}

}