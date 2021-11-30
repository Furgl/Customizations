package furgl.customizations.customizations.conditions;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.config.selectors.Selectables;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;

public class Condition extends ContextHolder {

	public Condition(String name) {
		super(name);
	}
	
	@Override
	public boolean test(Context... eventContext) {
		// conditions only care about their context (not event context)
		for (Context context : this.getContext()) {
			if (!context.test(context))
				return false;
		}
		return true;
	}
	
	public static Condition readFromConfig(JsonElement element) {
		Condition condition = null;
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Name")) {
				String name = obj.get("Name").getAsString();
				condition = new Condition(name);
				// type
				if (obj.has("Type"))
					condition.type = (SelectableType) Selectables.getTypeByID(obj.get("Type").getAsString());
				// context
				for (Entry<String, JsonElement> entry : obj.entrySet())
					if (!entry.getKey().equals("Name") && !entry.getKey().equals("Type")) {
						Context context = Contexts.get(entry.getKey());
						if (context != null)
							condition.addContext(context.readFromConfig(entry.getValue()));
					}
			}
		}
		return condition;
	}
	
}