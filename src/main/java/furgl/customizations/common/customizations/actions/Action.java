package furgl.customizations.common.customizations.actions;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;

public class Action extends ContextHolder {

	public Action(String name) {
		super(name);
	}
	
	public static Action readFromConfig(JsonElement element) {
		Action action = null;
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Name")) {
				String name = obj.get("Name").getAsString();
				action = new Action(name);
				// type
				if (obj.has("Type"))
					action.type = Selectables.getTypeByID(obj.get("Type").getAsString());
				// context
				for (Entry<String, JsonElement> entry : obj.entrySet())
					if (!entry.getKey().equals("Name") && !entry.getKey().equals("Type")) {
						Context context = Contexts.get(entry.getKey());
						if (context != null)
							action.addContext(context.readFromConfig(entry.getValue()));
					}
			}
		}
		return action;
	}

	/**Activate this action*/
	public void activate(Context... eventContexts) {
		((SelectableAction) this.type).activate(this.getContext().toArray(new Context[0]), eventContexts);
	}
	
}