package furgl.customizations.customizations.context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConsoleMessageContext extends Context {

	public String message = "";
	
	public ConsoleMessageContext(String message) {
		this();
		this.message = message;
	}
	
	public ConsoleMessageContext() {
		super("Console_Message");
	}
	
	@Override
	public boolean test(Context eventContext) {
		return this.message.isEmpty() || (eventContext instanceof ConsoleMessageContext && ((ConsoleMessageContext)eventContext).message.equals(this.message));
	}	

	@Override
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Message", this.message);
		return obj;
	}

	@Override
	public ConsoleMessageContext readFromConfig(JsonElement element) {
		ConsoleMessageContext context = (ConsoleMessageContext) this.newInstance();
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Message"))
				context.message = obj.get("Message").getAsString();
		}
		return context;
	}
	
	@Override
	public String toString() {
		return this.getId()+" {message:"+message+"}";
	}
	
}