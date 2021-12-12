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

public class WorldContext extends EventContext {

	public Identifier identifier;

	public WorldContext(Type type, World world) {
		this(type);
		this.identifier = world == null ? null : world.getRegistryKey().getValue();
	}
	
	public WorldContext() {
		this(null);
	}

	public WorldContext(Type type) {
		super(type);
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
		
	@Override
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put(addPlaceholderBase("world_name"), eventContexts -> this.identifier.toString());
		map.put(addPlaceholderBase("world_time"), eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getTime())).orElse("0"));
		map.put(addPlaceholderBase("world_spawn_x"), eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getSpawnX())).orElse("0"));
		map.put(addPlaceholderBase("world_spawn_y"), eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getSpawnY())).orElse("0"));
		map.put(addPlaceholderBase("world_spawn_z"), eventContexts -> this.getWorld().map(world -> String.valueOf(world.getLevelProperties().getSpawnZ())).orElse("0"));
		return map;
	}

}