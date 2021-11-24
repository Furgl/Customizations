package furgl.customizations.customizations.context;

import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RandomContext extends Context {

	private static final Random RAND = new Random();
	public float chance = 100f;
	
	public RandomContext(float chance) {
		this();
		this.chance = chance;
	}
	
	public RandomContext() {
		super("Random");
	}
	
	@Override
	public boolean test(Context eventContext) {
		return eventContext instanceof RandomContext && (RAND.nextFloat()*100f < this.chance);
	}	

	@Override
	public JsonElement writeToConfig() {
		JsonObject obj = new JsonObject();
		obj.addProperty("Chance", this.chance);
		return obj;
	}

	@Override
	public RandomContext readFromConfig(JsonElement element) {
		RandomContext context = (RandomContext) this.newInstance();
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.has("Chance"))
				context.chance = obj.get("Chance").getAsFloat();
		}
		return context;
	}
	
	@Override
	public String toString() {
		return this.getId()+" {chance:"+chance+"%}";
	}
	
}