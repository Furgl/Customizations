package furgl.customizations.customizations.context;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Context {

	private String id;
	protected ArrayList<Variable> variables = Lists.newArrayList();

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
			obj.addProperty(variable.name, (String) variable.toString.apply(variable.getValue.get()));
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

	@Override
	public String toString() {
		String ret = this.getId();
		if (!this.variables.isEmpty()) {
			ret += " {";
			for (int i=0; i<this.variables.size(); ++i) {
				Variable variable = this.variables.get(i);
				if (i > 0)
					ret += ",";
				ret += variable.name+":"+variable.toString.apply(variable.getValue.get());
			}
			ret += "}";
		}
		return ret;
	}

	/**Test if this other context matches this context*/
	public abstract boolean test(Context eventContext);

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

}