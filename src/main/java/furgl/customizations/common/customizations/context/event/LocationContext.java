package furgl.customizations.common.customizations.context.event;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import furgl.customizations.common.customizations.context.Context;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LocationContext extends EventContext {

	public Vec3d location;
	
	public LocationContext(Type type, BlockPos location) {
		this(type);
		this.location = location == null ? Vec3d.ZERO : new Vec3d(location.getX(), location.getY(), location.getZ());
	}
	
	public LocationContext(Type type, Vec3d location) {
		this(type);
		this.location = location == null ? Vec3d.ZERO : location;
	}
	
	public LocationContext() {
		this(null);
	}
	
	public LocationContext(Type type) {
		super(type);
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
	protected Map<String, Function<Context[], String>> createPlaceholders() {
		Map<String, Function<Context[], String>> map = Maps.newLinkedHashMap();
		map.put(addPlaceholderBase("pos_x"), eventContexts -> this.location.x == (int) this.location.x ? String.valueOf((int) this.location.x) : String.valueOf(this.location.x));
		map.put(addPlaceholderBase("pos_y"), eventContexts -> this.location.y == (int) this.location.y ? String.valueOf((int) this.location.y) : String.valueOf(this.location.y));
		map.put(addPlaceholderBase("pos_z"), eventContexts -> this.location.z == (int) this.location.z ? String.valueOf((int) this.location.z) : String.valueOf(this.location.z));
		return map;
	}
	
}