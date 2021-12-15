package furgl.customizations.common.customizations.context;

import java.util.Random;

public class RandomContext extends Context {

	private static final Random RAND = new Random();
	public float chance = 100f;

	public RandomContext(float chance) {
		this();
		this.chance = chance;
	}

	public RandomContext() {
		super();
		this.variables.add(new Context.Variable("Chance", 
				() -> this.chance, 
				chance -> this.chance = (float) chance,
				chance -> String.valueOf(chance), 
				str -> Float.valueOf((String) str)));
	}

	@Override
	public boolean test(Context[] configContexts, Context... eventContexts) {
		return RAND.nextFloat()*100f < this.chance;
	}	

}