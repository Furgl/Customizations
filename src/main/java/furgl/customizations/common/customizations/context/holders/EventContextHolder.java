package furgl.customizations.common.customizations.context.holders;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.common.customizations.context.BlockContext;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Context.Type;
import furgl.customizations.common.customizations.context.event.EntityContext;
import furgl.customizations.common.customizations.context.event.LocationContext;
import furgl.customizations.common.customizations.context.event.PlayerContext;
import furgl.customizations.common.customizations.context.event.WorldContext;
import furgl.customizations.common.impl.BlockAndLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EventContextHolder<O> extends ContextHolder {

	@Nullable
	public Type type;
	public List<String> placeholders = Lists.newArrayList();
	@Nullable
	private String name;

	public EventContextHolder(@Nullable Type type, O... objs) {
		this.type = type;
		if (type != null)
			for (O obj : objs)
				this.addContext(getContext(type, obj));

		this.placeholders.addAll(this.getContext().stream()
				.flatMap(ctx -> ctx.getPlaceholders().keySet().stream())
				.collect(Collectors.toList()));
	}

	public String getName(boolean formatted) {
		if (formatted)
			return this.type.formatting+""+Formatting.BOLD+
					WordUtils.capitalizeFully(this.type.name())+(this.name != null ? " ("+this.name+")" : "")+": ";
		else
			return this.name == null ? "" : this.name;
	}

	// GET CONTEXTS

	/**Get contexts for this object (can be null or class for getting placeholders)*/
	public List<Context> getContext(Type type, @Nullable Object obj) {
		if (obj instanceof Context)
			return Lists.newArrayList((Context) obj);
		else if (obj instanceof ServerPlayerEntity || obj == ServerPlayerEntity.class) {
			this.name = "Player";
			return getPlayerContext(type, (ServerPlayerEntity) (obj instanceof Class ? null : obj));
		}
		else if (obj instanceof Entity || obj == Entity.class) {
			this.name = "Entity";
			return getEntityContext(type, (Entity) (obj instanceof Class ? null : obj));
		}
		else if (obj instanceof BlockAndLocation || obj == BlockAndLocation.class) {
			this.name = "Block";
			return getBlockAndLocationContext(type, (BlockAndLocation) (obj instanceof Class ? null : obj));
		}
		else
			return Lists.newArrayList();
	}

	private List<Context> getPlayerContext(Type type, @Nullable PlayerEntity player) {
		List<Context> list = Lists.newArrayList(new PlayerContext(type, player));
		list.addAll(getEntityContext(type, (Entity)player));
		return list;
	}

	private List<Context> getEntityContext(Type type, @Nullable Entity entity) {
		List<Context> list = Lists.newArrayList(new EntityContext(type, entity));
		list.addAll(getLocationContext(type, entity == null ? null : entity.world, entity == null ? null : entity.getPos()));
		return list;
	}

	private List<Context> getBlockAndLocationContext(Type type, @Nullable BlockAndLocation bal) {
		List<Context> list = Lists.newArrayList(new BlockContext(type, bal == null ? null : bal.state.getBlock()));
		list.addAll(getLocationContext(type, bal == null ? null : bal.world, bal == null ? null : bal.pos));
		return list;
	}

	private List<Context> getLocationContext(Type type, @Nullable World world, @Nullable BlockPos pos) {
		return getLocationContext(type, world, pos == null ? null : new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
	}

	private List<Context> getLocationContext(Type type, @Nullable World world, @Nullable Vec3d pos) {
		return Lists.newArrayList(new WorldContext(type, world), new LocationContext(type, pos));
	}

}