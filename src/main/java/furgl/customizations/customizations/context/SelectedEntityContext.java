package furgl.customizations.customizations.context;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.Customizations;
import furgl.customizations.config.selectors.Selectable;
import furgl.customizations.config.selectors.Selectables;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SelectedEntityContext extends Context {

	public Selectable positionType = Selectables.BLANK;
	public Selectable type = Selectables.BLANK;
	public String playerName = "";
	@Nullable
	public UUID uuid;
	@Nullable
	public EntityType entityType;
	public float radius;
	public float x;
	public float y;
	public float z;
	@Nullable
	public RegistryKey<World> dimension;
	public int numberOfEntities = 1;
	
	public SelectedEntityContext() {
		super();
		this.variables.add(new Context.Variable("Type", 
				() -> this.type, 
				value -> this.type = (Selectable) value,
						value -> ((Selectable)value).getId(), 
						value -> Selectables.getTypeByID((String) value)));
		this.variables.add(new Context.Variable("PositionType", 
				() -> this.positionType, 
				value -> this.positionType = (Selectable) value,
						value -> ((Selectable)value).getId(), 
						value -> Selectables.getTypeByID((String) value)));
		this.variables.add(new Context.Variable("UUID", 
				() -> this.uuid, 
				value -> this.uuid = (UUID) value,
				value -> value == null ? "" : value.toString(), 
				value -> {
					try {
						return UUID.fromString((String) value);
					} 
					catch(Exception e) {
						return null;
					}
				}));
		this.variables.add(new Context.Variable("PlayerName", 
				() -> this.playerName, 
				value -> this.playerName = (String) value,
				value -> value, 
				value -> value));
		this.variables.add(new Context.Variable("EntityType", 
				() -> this.entityType, 
				value -> this.entityType = (EntityType) value,
				value -> this.entityType == null ? "" : EntityType.getId(this.entityType).toString(), 
				value -> {
					try {
						return ((String)value).isEmpty() ? null : Registry.ENTITY_TYPE.get(new Identifier((String)value));
					}
					catch (Exception e) {
						return null;
					}
				}));
		this.variables.add(new Context.Variable("Radius", 
				() -> this.radius, 
				value -> this.radius = (float) value,
				value -> String.valueOf(this.radius), 
				value -> Float.valueOf((String) value)));
		this.variables.add(new Context.Variable("X", 
				() -> this.x, 
				value -> this.x = (float) value,
				value -> String.valueOf(this.x), 
				value -> Float.valueOf((String) value)));
		this.variables.add(new Context.Variable("Y", 
				() -> this.y, 
				value -> this.y = (float) value,
				value -> String.valueOf(this.y), 
				value -> Float.valueOf((String) value)));
		this.variables.add(new Context.Variable("Z", 
				() -> this.z, 
				value -> this.z = (float) value,
				value -> String.valueOf(this.z), 
				value -> Float.valueOf((String) value)));
		this.variables.add(new Context.Variable("Dimension", 
				() -> this.dimension, 
				value -> this.dimension = (RegistryKey<World>) value,
				value -> this.dimension == null ? "" : this.dimension.getValue().toString(), 
				value -> {
					try {
						for (RegistryKey<World> key : Customizations.server.getWorldRegistryKeys())
			            	if (key.getValue().toString().equals(value))
			            		return key;
						return null;
					}
					catch (Exception e) {
						return null;
					}
				}));
		this.variables.add(new Context.Variable("NumberOfEntities", 
				() -> this.numberOfEntities, 
				value -> this.numberOfEntities = (int) value,
				value -> String.valueOf(this.numberOfEntities), 
				value -> Integer.valueOf((String) value)));
	}
	
	@Override
	public boolean test(Context eventContext) { // TODO
		return this.type == Selectables.ANY || (eventContext instanceof SelectedEntityContext && ((SelectedEntityContext)eventContext).type.equals(this.type));
	}	
	
}