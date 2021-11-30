package furgl.customizations.customizations.context;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class WorldContext extends Context {

	public Identifier identifier;

	public WorldContext(World world) {
		this();
		this.identifier = world.getRegistryKey().getValue();
	}

	public WorldContext() {
		super();
		this.variables.add(new Context.Variable("World", 
				() -> this.identifier, 
				value -> this.identifier = (Identifier) value,
				value -> value.toString(), 
				value -> new Identifier((String) value)));
	}

	@Override
	public boolean test(Context eventContext) {
		return this.identifier == null || (eventContext instanceof WorldCauseContext && ((WorldCauseContext)eventContext).identifier.equals(this.identifier));
	}

	public RegistryKey<World> getWorldKey() {
		return ContextHelper.getWorldKey(this.identifier);
	}	

	@Nullable
	public World getWorld() {
		return ContextHelper.getWorld(ContextHelper.getWorldKey(this.identifier));
	}	

	public static class WorldCauseContext extends WorldContext {
		public WorldCauseContext(World world) {
			super(world);
		}
		public WorldCauseContext() {
			super();
		}
	}
	public static class WorldTargetContext extends WorldContext {
		public WorldTargetContext(World world) {
			super(world);
		}
		public WorldTargetContext() {
			super();
		}
	}

}