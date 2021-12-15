package furgl.customizations.common.customizations.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.selectables.Selectable;
import furgl.customizations.common.customizations.selectables.Selectables;
import furgl.customizations.common.mixin.WorldAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SelectedEntityContext extends Context {

	public Set<Entity> selectedEntities = Sets.newHashSet();

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
	public boolean test(Context[] configContexts, Context... eventContexts) {
		this.selectedEntities = getEntity(configContexts, eventContexts);
		return !this.selectedEntities.isEmpty();
	}	

	/**Get entity from these contexts*/
	public Set<Entity> getEntity(Context[] configContexts, Context... eventContexts) {
		World world = Contexts.get(Contexts.WORLD, Type.CAUSE, eventContexts).map(context -> context.getWorld().orElse(null)).orElse(null);
		if (this.type == Selectables.ENTITY_CAUSE)
			return Contexts.get(Contexts.ENTITY, Type.CAUSE, eventContexts).map(context -> this.getEntity(world, context.uuid, context.id, null)).orElse(Sets.newHashSet());
		else if (this.type == Selectables.ENTITY_TARGET)
			return Contexts.get(Contexts.ENTITY, Type.TARGET, eventContexts).map(context -> this.getEntity(world, context.uuid, context.id, null)).orElse(Sets.newHashSet());
		else if (this.type == Selectables.ENTITY_SPECIFIC)
			return this.getEntity(world, this.uuid, -1, this.playerName);
		else if (this.type == Selectables.ENTITY_NEAREST)
			return this.getNearestEntities(this.entityType, getLocation(this.positionType, eventContexts), this.radius, this.numberOfEntities, null);
		else if (this.type == Selectables.ENTITY_ALL_PLAYERS && Customizations.server != null)
			return Sets.newHashSet(Customizations.server.getPlayerManager().getPlayerList());
		return Sets.newHashSet();
	}

	/**Get entity based on these parameters*/
	public Set<Entity> getEntity(@Nullable World worldIn, @Nullable UUID uuid, int id, @Nullable String playerName) {		
		Entity entity = null;
		// player name
		if (entity == null && playerName != null && !playerName.isEmpty() && Customizations.server != null) 
			entity = Customizations.server.getPlayerManager().getPlayer(playerName);
		// player uuid
		if (entity == null && uuid != null && Customizations.server != null) 
			entity = Customizations.server.getPlayerManager().getPlayer(uuid);
		// if world not specified, check all worlds
		if (entity == null) {
			ArrayList<World> worlds = Lists.newArrayList();
			if (worldIn != null)
				worlds.add(worldIn);
			else if (Customizations.server != null)
				worlds = Lists.newArrayList(Customizations.server.getWorlds());
			// check each world
			for (World world : worlds) {
				if (entity == null && uuid != null) 
					entity = ((WorldAccessor)world).getEntityLookup().get(uuid);
				if (entity == null && id != -1) 
					entity = world.getEntityById(id);
			}
		}

		if (entity == null || // entity not found
				(this.uuid != null && !entity.getUuid().equals(this.uuid)) || // entity's uuid doesn't match
				(this.playerName != null && !this.playerName.isEmpty() && !this.playerName.equalsIgnoreCase(entity.getEntityName()))) // player's name doesn't match
			return Sets.newHashSet();
		else
			return Sets.newHashSet(entity);
	}	

	private static final TypeFilter<Entity, ?> PASSTHROUGH_FILTER = new TypeFilter<Entity, Entity>() {
		public Entity downcast(Entity entity) {
			return entity;
		}

		public Class<? extends Entity> getBaseClass() {
			return Entity.class;
		}
	};

	/**Get entities nearest to this position*/
	private Set<Entity> getNearestEntities(@Nullable EntityType entityType, Location location, float radius, int numberOfEntities, Class<? extends Entity> baseClass) {
		if (location != null && location.world != null && location.pos != null) {
			TypeFilter filter = entityType == null ? PASSTHROUGH_FILTER : entityType;
			Box box = Box.of(location.pos, radius, radius, radius);
			return (Set<Entity>) location.world.getEntitiesByType(filter, box, entity -> baseClass == null || baseClass.isAssignableFrom(entity.getClass())).stream()
					.sorted(new Comparator<Entity>() {
						@Override
						public int compare(Entity e1, Entity e2) {
							return Double.compare(e1.squaredDistanceTo(location.pos), e2.squaredDistanceTo(location.pos));
						}
					})
					.limit(numberOfEntities)
					.collect(Collectors.toSet());
		}
		return Sets.newHashSet();
	}

	@Nullable
	private Location getLocation(Selectable positionType, Context[] eventContexts) {
		Vec3d pos = null;
		World world = null;
		if (positionType == Selectables.POSITION_CAUSE) {
			pos = Contexts.get(Contexts.LOCATION, Type.CAUSE, eventContexts).map(context -> context.location).orElse(null);
			world = Contexts.get(Contexts.WORLD, Type.CAUSE, eventContexts).map(context -> context.getWorld().orElse(null)).orElse(null);
		}
		else if (positionType == Selectables.POSITION_TARGET) {
			pos = Contexts.get(Contexts.LOCATION, Type.CAUSE, eventContexts).map(context -> context.location).orElse(null);
			world = Contexts.get(Contexts.WORLD, Type.CAUSE, eventContexts).map(context -> context.getWorld().orElse(null)).orElse(null);
		}
		else if (positionType == Selectables.POSITION_FIXED) {
			pos = Contexts.get(Contexts.SELECTED_ENTITY, Type.OTHER, eventContexts).map(context -> new Vec3d(context.x, context.y, context.z)).orElse(null);
			world = ContextHelper.getWorld(this.dimension);
		}
		return new Location(world, pos);
	}

	public static class Location {
		@Nullable
		public World world;
		@Nullable
		public Vec3d pos;

		public Location(@Nullable World world, @Nullable Vec3d pos) {
			this.world = world;
			this.pos = pos;
		}
	}

}