package furgl.customizations.customizations.context;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class LocationContext extends Context {

	public Vec3d location;
	
	public LocationContext(BlockPos location) {
		this();
		this.location = new Vec3d(location.getX(), location.getY(), location.getZ());
	}
	
	public LocationContext(Vec3d location) {
		this();
		this.location = location;
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
	
	@Override
	public boolean test(Context eventContext) {
		return this.location == null || 
				(eventContext instanceof LocationContext && ((LocationContext)eventContext).location.equals(this.location));
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
	}
	
}