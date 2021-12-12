package furgl.customizations.common.customizations.triggers;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.Selectables;

public class Trigger extends ConfigContextHolder {

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
					trigger.type = Selectables.getTypeByID(obj.get("Type").getAsString());
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