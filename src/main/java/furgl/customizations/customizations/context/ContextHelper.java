package furgl.customizations.customizations.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import furgl.customizations.Customizations;
import furgl.customizations.config.selectors.Selectable;
import furgl.customizations.config.selectors.Selectables;
import furgl.customizations.customizations.context.EntityContext.EntityCauseContext;
import furgl.customizations.customizations.context.EntityContext.EntityTargetContext;
import furgl.customizations.customizations.context.LocationContext.LocationCauseContext;
import furgl.customizations.customizations.context.LocationContext.LocationTargetContext;
import furgl.customizations.customizations.context.PlayerContext.PlayerCauseContext;
import furgl.customizations.customizations.context.PlayerContext.PlayerTargetContext;
import furgl.customizations.customizations.context.WorldContext.WorldCauseContext;
import furgl.customizations.customizations.context.WorldContext.WorldTargetContext;
import furgl.customizations.mixin.WorldAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ContextHelper {

	/**Get entity based on these parameters*/
	public static Set<Entity> getEntity(@Nullable World worldIn, @Nullable UUID uuid, int id, @Nullable String playerName) {
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

		if (entity == null)
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
	private static Set<Entity> getNearestEntities(@Nullable EntityType entityType, Location location, float radius, int numberOfEntities, Class<? extends Entity> baseClass) {
		if (location != null && location.world != null && location.pos != null) {
			TypeFilter filter = entityType == null ? PASSTHROUGH_FILTER : entityType;
			Box box = Box.of(location.pos, radius, radius, radius);
			return (Set<Entity>) location.world.getEntitiesByType(filter, box, entity -> baseClass.isAssignableFrom(entity.getClass())).stream()
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

	/**Get entity from these contexts*/
	public static Set<Entity> getEntity(Class<? extends Entity> baseClass, Context... contexts) {
		Selectable selectable = Contexts.get(Contexts.SELECTED_ENTITY, contexts).map(context -> context.type).orElse(Selectables.NONE);
		World world = Contexts.get(Contexts.CAUSE_WORLD, contexts).map(context -> context.getWorld()).orElse(null);
		if (selectable == Selectables.ENTITY_CAUSE) 
			return Contexts.get(Contexts.CAUSE_ENTITY, contexts).map(context -> getEntity(world, context.uuid, context.id, null)).orElse(Sets.newHashSet());
		else if (selectable == Selectables.ENTITY_TARGET)
			return Contexts.get(Contexts.TARGET_ENTITY, contexts).map(context -> getEntity(world, context.uuid, context.id, null)).orElse(Sets.newHashSet());
		else if (selectable == Selectables.ENTITY_SPECIFIC)
			return Contexts.get(Contexts.SELECTED_ENTITY, contexts).map(context -> getEntity(world, context.uuid, -1, context.playerName)).orElse(Sets.newHashSet());
		else if (selectable == Selectables.ENTITY_NEAREST)
			return Contexts.get(Contexts.SELECTED_ENTITY, contexts).map(context -> getNearestEntities(context.entityType, getLocation(context.positionType, contexts), context.radius, context.numberOfEntities, baseClass)).orElse(Sets.newHashSet());
		return Sets.newHashSet();
	}

	@Nullable
	private static Location getLocation(Selectable positionType, Context... contexts) {
		Vec3d pos = null;
		World world = null;
		if (positionType == Selectables.POSITION_CAUSE) {
			pos = Contexts.get(Contexts.CAUSE_LOCATION, contexts).map(context -> context.location).orElse(null);
			world = Contexts.get(Contexts.CAUSE_WORLD, contexts).map(context -> context.getWorld()).orElse(null);
		}
		else if (positionType == Selectables.POSITION_TARGET) {
			pos = Contexts.get(Contexts.TARGET_LOCATION, contexts).map(context -> context.location).orElse(null);
			world = Contexts.get(Contexts.TARGET_WORLD, contexts).map(context -> context.getWorld()).orElse(null);
		}
		else if (positionType == Selectables.POSITION_FIXED) {
			pos = Contexts.get(Contexts.SELECTED_ENTITY, contexts).map(context -> new Vec3d(context.x, context.y, context.z)).orElse(null);
			world = Contexts.get(Contexts.SELECTED_ENTITY, contexts).map(context -> getWorld(context.dimension)).orElse(null);
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

	public static RegistryKey<World> getWorldKey(Identifier identifier) {
		if (Customizations.server != null && identifier != null)
			for (World world : Customizations.server.getWorlds())
				if (world.getRegistryKey().getValue().equals(identifier))
					return world.getRegistryKey();
		return World.OVERWORLD;
	}

	@Nullable
	public static World getWorld(RegistryKey<World> worldKey) {
		if (Customizations.server != null)
			return Customizations.server.getWorld(worldKey);
		return null;
	}
	
	// GET CONTEXTS

	public static List<Context> getCauseContext(PlayerEntity player) {
		List<Context> list = Lists.newArrayList(new PlayerCauseContext(player));
		list.addAll(getCauseContext((Entity)player));
		return list;
	}

	private static List<Context> getCauseContext(Entity entity) {
		List<Context> list = Lists.newArrayList(new EntityCauseContext(entity));
		list.addAll(getCauseContext(entity.world, entity.getPos()));
		return list;
	}
	
	public static List<Context> getCauseContext(World world, BlockPos pos) {
		return getCauseContext(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
	}
	
	public static List<Context> getCauseContext(World world, Vec3d pos) {
		return Lists.newArrayList(new WorldCauseContext(world), new LocationCauseContext(pos));
	}

	public static List<Context> getTargetContext(PlayerEntity player) {
		List<Context> list = Lists.newArrayList(new PlayerTargetContext(player));
		list.addAll(getTargetContext((Entity)player));
		return list;
	}

	private static List<Context> getTargetContext(Entity entity) {
		List<Context> list = Lists.newArrayList(new EntityTargetContext(entity));
		list.addAll(getTargetContext(entity.world, entity.getPos()));
		return list;
	}
	
	public static List<Context> getTargetContext(World world, BlockPos pos) {
		return getTargetContext(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
	}
	
	public static List<Context> getTargetContext(World world, Vec3d pos) {
		return Lists.newArrayList(new WorldTargetContext(world), new LocationTargetContext(pos));
	}

}