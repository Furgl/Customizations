package furgl.customizations.common.customizations.context.event;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import furgl.customizations.common.customizations.context.Context;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class LocationContext extends EventContext {

	public Vec3d location;
	
	public LocationContext(BlockPos location) {
		this();
		this.location = location == null ? Vec3d.ZERO : new Vec3d(location.getX(), location.getY(), location.getZ());
	}
	
	public LocationContext(Vec3d location) {
		this();
		this.location = location == null ? Vec3d.ZERO : location;
	}
	
	public LocationContext() {
		super();
		this.variables.add(new Context.Variable("Location", 
				() -> this.location, 
				value -> this.location = (Vec3d) value,
						value -> value.toString(), 
						value -> fromString((String) value)));
	}
	
	/**(x, y, z) String to Vec3d*/
	@Nullable
	private Vec3d fromString(String value) {
		try {
			value = value.replaceAll("(", "").replaceAll(")", ""); // remove ( and )
			String[] array = value.split(", ");
			return new Vec3d(Double.valueOf(array[0]), Double.valueOf(array[1]), Double.valueOf(array[2]));
		}
		catch (Exception e) {
			return null;
		}
	}
	
	protected abstract String getPlaceholderBase();
	
	@Override
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put(getPlaceholderBase()+"_pos_x", eventContexts -> String.valueOf(this.location.x));
		map.put(getPlaceholderBase()+"_pos_y", eventContexts -> String.valueOf(this.location.y));
		map.put(getPlaceholderBase()+"_pos_z", eventContexts -> String.valueOf(this.location.z));
		return map;
	}
	
	public static class LocationCauseContext extends LocationContext {
		public LocationCauseContext(BlockPos location) {
			super(location);
		}
		
		public LocationCauseContext(Vec3d location) {
			super(location);
		}
		public LocationCauseContext() {
			super();
		}

		@Override
		protected String getPlaceholderBase() {
			return "cause";
		}
	}
	public static class LocationTargetContext extends LocationContext {
		public LocationTargetContext(BlockPos location) {
			super(location);
		}
		
		public LocationTargetContext(Vec3d location) {
			super(location);
		}
		public LocationTargetContext() {
			super();
		}

		@Override
		protected String getPlaceholderBase() {
			return "target";
		}
	}
	
}