package furgl.customizations.common.customizations.context.holders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.PacketManager;
import furgl.customizations.common.config.FileConfig;
import furgl.customizations.common.customizations.context.BlockContext;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.Context.Type;
import furgl.customizations.common.customizations.context.event.EntityContext;
import furgl.customizations.common.customizations.context.event.LocationContext;
import furgl.customizations.common.customizations.context.event.PlayerContext;
import furgl.customizations.common.customizations.context.event.WorldContext;
import furgl.customizations.common.impl.BlockAndLocation;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EventContextHolder<O> extends ContextHolder {

	public Type type;
	public List<String> placeholders = Lists.newArrayList();
	@Nullable
	private String name;
	private O[] objs;

	public EventContextHolder(Type type, O... objs) {
		this.type = type;
		for (O obj : objs)
			initObject(obj);
		this.objs = objs;
	}

	/**Initialize values based on this object (get name, contexts, etc.)*/
	public void initObject(Object obj) {
		// get name / contexts
		if (obj instanceof Context)
			this.addContext((Context) obj);
		else if (obj instanceof ServerPlayerEntity || obj == ServerPlayerEntity.class) {
			this.name = "Player";
			this.addContext(getPlayerContext((ServerPlayerEntity) (obj instanceof Class ? null : obj)));
		}
		else if (obj instanceof Entity || obj == Entity.class) {
			this.name = "Entity";
			this.addContext(getEntityContext((Entity) (obj instanceof Class ? null : obj)));
		}
		else if (obj instanceof BlockAndLocation || obj == BlockAndLocation.class) {
			this.name = "Block";
			this.addContext(getBlockAndLocationContext((BlockAndLocation) (obj instanceof Class ? null : obj)));
		}
		// debug visuals
		if (FileConfig.debugVisuals && Customizations.server != null) {
			PacketByteBuf buf = PacketByteBufs.create();
			// read variables from obj
			UUID uuid = obj instanceof Entity ? ((Entity)obj).getUuid() : null;
			boolean isBlock = this.hasContext(Contexts.BLOCK);
			Vec3d pos = this.hasContext(Contexts.LOCATION, this.type) ? this.getContext(Contexts.LOCATION, this.type).location : null;
			// write variables to buffer
			buf.writeInt(this.type.ordinal());
			buf.writeString(uuid == null ? "" : uuid.toString());
			buf.writeBoolean(isBlock);
			buf.writeBoolean(pos != null);
			if (pos != null) {
				buf.writeDouble(pos.x);
				buf.writeDouble(pos.y);
				buf.writeDouble(pos.z);
			}
			for (ServerPlayerEntity player : Customizations.server.getPlayerManager().getPlayerList())
				ServerPlayNetworking.send(player, PacketManager.DEBUG_VISUALS, buf);
		}
		// get placeholders
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
	
	@Override
	public String toString() {
		return "{"+Arrays.toString(this.objs)+"}";
	}

	// GET CONTEXTS

	private List<Context> getPlayerContext(@Nullable PlayerEntity player) {
		List<Context> list = Lists.newArrayList(new PlayerContext(type, player));
		list.addAll(getEntityContext((Entity)player));
		return list;
	}

	private List<Context> getEntityContext(@Nullable Entity entity) {
		List<Context> list = Lists.newArrayList(new EntityContext(type, entity));
		list.addAll(getLocationContext(entity == null ? null : entity.world, entity == null ? null : entity.getPos()));
		return list;
	}

	private List<Context> getBlockAndLocationContext(@Nullable BlockAndLocation bal) {
		List<Context> list = Lists.newArrayList(new BlockContext(type, bal == null ? null : bal.state.getBlock()));
		list.addAll(getLocationContext(bal == null ? null : bal.world, bal == null ? null : bal.pos));
		return list;
	}

	private List<Context> getLocationContext(@Nullable World world, @Nullable BlockPos pos) {
		return getLocationContext(world, pos == null ? null : new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
	}

	private List<Context> getLocationContext(@Nullable World world, @Nullable Vec3d pos) {
		return Lists.newArrayList(new WorldContext(type, world), new LocationContext(type, pos));
	}

}