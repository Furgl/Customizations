package furgl.customizations.customizations.context;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;

public abstract class EntityContext extends Context {

	@Nullable
	public UUID uuid;
	public int id = -1;

	public EntityContext(Entity entity) {
		this(entity.getUuid(), entity.getId());
	}

	public EntityContext(UUID uuid, int id) {
		this();
		this.uuid = uuid;
		this.id  = id;
	}

	public EntityContext() {
		super();
		this.variables.add(new Context.Variable("UUID", 
				() -> this.uuid, 
				value -> this.uuid = (UUID) value,
				value -> value.toString(), 
				value -> {
					try {
						return UUID.fromString((String) value);
					} 
					catch(Exception e) {
						return null;
					}
				}));
		this.variables.add(new Context.Variable("ID", 
				() -> this.id, 
				value -> this.id = (int) id,
				value -> value.toString(), 
				value -> Integer.valueOf((String) value)));
	}

	@Override
	public boolean test(Context eventContext) {
		return this.uuid == null || (eventContext instanceof EntityContext && ((EntityContext)eventContext).uuid.equals(this.uuid));
	}
	
	public static class EntityCauseContext extends EntityContext {
		public EntityCauseContext(Entity entity) {
			super(entity);
		}
		public EntityCauseContext() {
			super();
		}
	}
	public static class EntityTargetContext extends EntityContext {
		public EntityTargetContext(Entity entity) {
			super(entity);
		}
		public EntityTargetContext() {
			super();
		}
	}

}