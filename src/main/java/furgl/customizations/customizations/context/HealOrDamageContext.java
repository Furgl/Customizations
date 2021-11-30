package furgl.customizations.customizations.context;

public class HealOrDamageContext extends Context {

	public float heal;
	public float damage;
	
	public HealOrDamageContext(float heal, float damage) {
		this();
		this.heal = heal;
		this.damage = damage;
	}
	
	public HealOrDamageContext() {
		super();
		this.variables.add(new Context.Variable("Heal", 
				() -> this.heal, 
				value -> this.heal = (float) value,
						value -> String.valueOf(value), 
						value -> Float.valueOf((String) value)));
		this.variables.add(new Context.Variable("Damage", 
				() -> this.damage, 
				value -> this.damage = (float) value,
						value -> String.valueOf(value), 
						value -> Float.valueOf((String) value)));
	}
	
	@Override
	public boolean test(Context eventContext) {
		return eventContext instanceof HealOrDamageContext && 
				((HealOrDamageContext)eventContext).heal == this.heal &&
				((HealOrDamageContext)eventContext).damage == this.damage;
	}	
	
}