package furgl.customizations.common.customizations.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.Formatting;

public abstract class Context { 

	public enum Type {
		CAUSE(Formatting.YELLOW), 
		TARGET(Formatting.RED), 
		SUBJECT(Formatting.GREEN), 
		OTHER(Formatting.GRAY);
		
		public Formatting formatting;

		private Type(Formatting formatting) {
			this.formatting = formatting;
		}
	}
	
	@Nullable
	public Type type;
	private String id;
	protected ArrayList<Variable> variables = Lists.newArrayList();
	@Nullable
	private Map<String, Function<Context[], String>> placeholders;
	
	public Context(Type type) {
		this();
		this.type = type;
	}
	
	public Context() {
		this.id = this.getClass().getSimpleName().replace("Context", "");
		Contexts.ALL_CONTEXTS.add(this);
	}

	@Override
	public boolean equals(Object obj) {
		return this.getClass() == obj.getClass();
	}

	public Context newInstance() {
		try {
			return this.getClass().getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getId() {
		return id;
	}

	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		for (Variable variable : this.variables)
			obj.addProperty(variable.name, variable.getValue.get() == null ? "" : (String) variable.toString.apply(variable.getValue.get()));
		return obj;
	}

	public Context readFromConfig(JsonElement element) {
		Context context = this.newInstance();
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			for (Variable variable : context.variables)
				if (obj.has(variable.name) && !obj.get(variable.name).isJsonNull()) {
					Object value = variable.getValue.get();
					try {
						value = variable.fromString.apply(obj.get(variable.name).getAsString());
						variable.setValue.accept(value);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
		}
		return context;
	}
	
	/**Test if these event contexts match this context*/
	public abstract boolean test(Context[] configContexts, Context... eventContexts);

	@Override
	public String toString() {
		String ret = this.getId()+(this.type == null ? "" : " "+WordUtils.capitalizeFully(this.type.name()));
		if (!this.variables.isEmpty()) {
			ret += " {";
			for (int i=0; i<this.variables.size(); ++i) {
				Variable variable = this.variables.get(i);
				if (i > 0)
					ret += ",";
				ret += variable.name+":"+(variable.getValue.get() == null ? "" : variable.toString.apply(variable.getValue.get()));
			}
			ret += "}";
		}
		return ret;
	}

	public static class Variable<T> {
		public String name;
		public Supplier<T> getValue;
		public Consumer<T> setValue;
		public Function<T, String> toString;
		public Function<String, T> fromString;

		public Variable(String name, Supplier<T> getValue, Consumer<T> setValue, Function<T, String> toString, Function<String, T> fromString) {
			this.name = name;
			this.getValue = getValue;
			this.setValue = setValue;
			this.toString = toString;
			this.fromString = fromString;
		}
	}

	/**Get map of placeholder (without the %s) to function of eventContexts -> String*/
	public Map<String, Function<Context[], String>> getPlaceholders() {
		if (this.placeholders == null)
			this.placeholders = this.createPlaceholders();
		return this.placeholders;
	}
	
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		return Maps.newLinkedHashMap();
	}
	
	/**Add placeholder base (i.e. "cause") to placeholder*/
	public String addPlaceholderBase(String placeholder) {
		return this.type == null ? placeholder : this.type.name().toLowerCase()+"_"+placeholder;
	}
	
}