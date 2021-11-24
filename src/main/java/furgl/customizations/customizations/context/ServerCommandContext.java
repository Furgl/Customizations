package furgl.customizations.customizations.context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ServerCommandContext extends Context {

	public String command = "";
	
	public ServerCommandContext(String command) {
		this();
		this.command = command;
	}
	
	public ServerCommandContext() {
		super("Server_Command");
	}
	
	@Override
	public boolean test(Context eventContext) {
		return this.command.isEmpty() || (eventContext instanceof ServerCommandContext && ((ServerCommandContext)eventContext).command.equals(this.command));
	}	

	@Override
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Command", this.command);
		return obj;
	}

	@Override
	public ServerCommandContext readFromConfig(JsonElement element) {
		ServerCommandContext context = (ServerCommandContext) this.newInstance();
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Command"))
				context.command = obj.get("Command").getAsString();
		}
		return context;
	}
	
	@Override
	public String toString() {
		return this.getId()+" {command:"+command+"}";
	}
	
}