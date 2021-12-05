package furgl.customizations.common.customizations.context.event;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.mixin.WorldAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class EntityContext extends EventContext {

	@Nullable
	public UUID uuid;
	public int id = -1;

	public EntityContext(Entity entity) {
		this(entity == null ? null : entity.getUuid(), entity == null ? 0 : entity.getId());
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
	
	public abstract String getPlaceholderBase();
	
	@Override
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put(getPlaceholderBase()+"_entity_name", eventContexts -> getEntity().map(entity -> entity.getEntityName()).orElse("No entity found"));
		map.put(getPlaceholderBase()+"_entity_pos_x", eventContexts -> getEntity().map(entity -> String.valueOf(entity.getPos().x)).orElse("No entity found"));
		map.put(getPlaceholderBase()+"_entity_pos_y", eventContexts -> getEntity().map(entity -> String.valueOf(entity.getPos().y)).orElse("No entity found"));
		map.put(getPlaceholderBase()+"_entity_pos_z", eventContexts -> getEntity().map(entity -> String.valueOf(entity.getPos().z)).orElse("No entity found"));
		return map;
	}
	
	public Optional<Entity> getEntity() {
		Entity entity = null;
		if (Customizations.server != null) {
			for (World world : Customizations.server.getWorlds()) {
				if (entity == null && uuid != null) 
					entity = ((WorldAccessor)world).getEntityLookup().get(uuid);
				if (entity == null && id != -1) 
					entity = world.getEntityById(id);
			}
		}
		return entity == null ? Optional.empty() : Optional.of(entity);
	}
	
	public static class EntityCauseContext extends EntityContext {
		public EntityCauseContext(Entity entity) {
			super(entity);
		}
		public EntityCauseContext() {
			super();
		}
		@Override
		public String getPlaceholderBase() {
			return "cause";
		}
	}
	public static class EntityTargetContext extends EntityContext {
		public EntityTargetContext(Entity entity) {
			super(entity);
		}
		public EntityTargetContext() {
			super();
		}
		@Override
		public String getPlaceholderBase() {
			return "target";
		}
	}

}