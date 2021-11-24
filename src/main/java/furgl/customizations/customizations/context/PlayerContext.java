package furgl.customizations.customizations.context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerContext extends Context {

	public PlayerContext(ServerPlayerEntity player) {
		super("Player");
	}
	
	@Override
	public boolean test(Context eventContext) {
		return true; // TODO
	}	

	@Override
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		//obj.addProperty("block", Registry.BLOCK.getId(this.block).toString());
		return obj;
	}

	@Override
	public PlayerContext readFromConfig(JsonElement element) {
		PlayerContext context = (PlayerContext) this.newInstance();
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
//			if (obj.has("block")) {
//				Block block = Registry.BLOCK.get(new Identifier(obj.get("block").getAsString()));
//				context.block = block;
//			}
		}
		return context;
	}
	
}