package furgl.customizations.common.customizations.context.event;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public abstract class WorldContext extends EventContext {

	public Identifier identifier;

	public WorldContext(World world) {
		this();
		this.identifier = world == null ? null : world.getRegistryKey().getValue();
	}

	public WorldContext() {
		super();
		this.variables.add(new Context.Variable("World", 
				() -> this.identifier, 
				value -> this.identifier = (Identifier) value,
				value -> value.toString(), 
				value -> new Identifier((String) value)));
	}

	public RegistryKey<World> getWorldKey() {
		return ContextHelper.getWorldKey(this.identifier);
	}	

	@Nullable
	public Optional<World> getWorld() {
		World world = ContextHelper.getWorld(ContextHelper.getWorldKey(this.identifier));
		if (world == null)
			return Optional.empty();
		else
			return Optional.of(world);
	}	
	
	protected abstract String getPlaceholderBase();
	
	@Override
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put(getPlaceholderBase()+"_world_name", eventContexts -> this.identifier.toString());
		map.put(getPlaceholderBase()+"_world_time", eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getTime())).orElse("0"));
		map.put(getPlaceholderBase()+"_world_spawn_x", eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getSpawnX())).orElse("0"));
		map.put(getPlaceholderBase()+"_world_spawn_y", eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getSpawnY())).orElse("0"));
		map.put(getPlaceholderBase()+"_world_spawn_z", eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getSpawnZ())).orElse("0"));
		return map;
	}

	public static class WorldCauseContext extends WorldContext {
		public WorldCauseContext(World world) {
			super(world);
		}
		public WorldCauseContext() {
			super();
		}
		@Override
		protected String getPlaceholderBase() {
			return "cause";
		}
	}
	public static class WorldTargetContext extends WorldContext {
		public WorldTargetContext(World world) {
			super(world);
		}
		public WorldTargetContext() {
			super();
		}
		@Override
		protected String getPlaceholderBase() {
			return "target";
		}
	}

}