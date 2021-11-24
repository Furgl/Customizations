package furgl.customizations.customizations.context;

import com.google.gson.JsonElement;

public abstract class Context {

	private String id;

	public Context(String id) {
		this.id = id;
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

	public abstract JsonElement writeToConfig();

	public abstract Context readFromConfig(JsonElement obj);

	/**Test if this other context matches this context*/
	public abstract boolean test(Context eventContext);
	
	@Override
	public String toString() {
		return this.getId()+" {"+"}";
	}

}