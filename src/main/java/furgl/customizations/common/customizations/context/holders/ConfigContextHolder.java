package furgl.customizations.common.customizations.context.holders;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.selectables.Selectable;
import furgl.customizations.common.customizations.selectables.Selectables;

public abstract class ConfigContextHolder extends ContextHolder {
	
	public String name;
	@Nullable
	protected Selectable type;

	public ConfigContextHolder(String name) {
		this.name = name;
	}

	/**Test if this should be activated in this context*/
	public boolean test(Context... eventContexts) {
		for (Context customizationContext : this.getContext())
			if (!customizationContext.test(this.getContext().toArray(new Context[0]), eventContexts))
				return false;
		return true;
	}
	
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Name", this.name);
		// type
		if (this.getType() != null)
			obj.addProperty("Type", this.type.getId());
		// context
		for (Context context : this.getContext())
			obj.add(context.getId(), context.writeToConfig());
		return obj;
	}

	public Selectable getType() {
		if (this.type == null)
			this.type = Selectables.BLANK;
		return this.type;
	}

	public void setType(Selectable type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.name+" {Type:"+this.type.getId()+(this.getContext().isEmpty() ? "" : ",")+this.getContext()+"}";
	}

}