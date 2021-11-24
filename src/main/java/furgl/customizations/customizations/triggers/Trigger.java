package furgl.customizations.customizations.triggers;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;

public class Trigger extends ContextHolder {

	public Trigger(String name) {
		super(name);
	}
	
	public static Trigger readFromConfig(JsonElement element) {
		Trigger trigger = null;
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Name")) {
				String name = obj.get("Name").getAsString();
				trigger = new Trigger(name);
				// type
				if (obj.has("Type"))
					trigger.type = TriggerTypes.getTypeByID(obj.get("Type").getAsString());
				// context
				for (Entry<String, JsonElement> entry : obj.entrySet())
					if (!entry.getKey().equals("Name") && !entry.getKey().equals("Type")) {
						Context context = Contexts.get(entry.getKey());
						if (context != null)
							trigger.addContext(context.readFromConfig(entry.getValue()));
					}
			}
		}
		return trigger;
	}

}