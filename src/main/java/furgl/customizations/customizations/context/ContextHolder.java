package furgl.customizations.customizations.context;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.config.selectors.Selectables;

/**This has context that can be set/read/tested*/
public abstract class ContextHolder {
	
	public String name;
	@Nullable
	protected SelectableType type;
	private ArrayList<Context> contexts = Lists.newArrayList();

	public ContextHolder(String name) {
		this.name = name;
	}

	/**Test if this should be activated in this context*/
	public boolean test(Context... eventContext) {
		for (Context contextIn : eventContext) {
			Context context = this.getContext(contextIn);
			if (context != null && !context.test(contextIn))
				return false;
		}
		return true;
	}

	public void addContext(Context context) {
		this.contexts.add(context);
	}

	public boolean hasContext(Context contextIn) {
		for (Context context : contexts)
			if (context.equals(contextIn))
				return true;
		return false;
	}

	public <T extends Context> T getOrAddContext(T contextIn) {
		T context = getContext(contextIn);
		if (context != null)
			return context;
		else {
			context = (T) contextIn.newInstance();
			this.addContext(context);
			return context;
		}
	}

	public ArrayList<Context> getContext() {
		return this.contexts;
	}

	@Nullable
	public <T extends Context> T getContext(T contextIn) {
		for (Context context : contexts)
			if (context.equals(contextIn))
				return (T) context;
		return null;
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

	public SelectableType getType() {
		if (this.type == null)
			this.type = Selectables.BLANK;
		return this.type;
	}

	public void setType(SelectableType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.name+" {Type:"+this.type.getId()+(this.getContext().isEmpty() ? "" : ",")+this.getContext()+"}";
	}

}