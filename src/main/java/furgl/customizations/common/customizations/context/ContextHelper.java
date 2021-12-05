package furgl.customizations.common.customizations.context;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import eu.pb4.placeholders.PlaceholderAPI;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.context.event.EntityContext.EntityCauseContext;
import furgl.customizations.common.customizations.context.event.EntityContext.EntityTargetContext;
import furgl.customizations.common.customizations.context.event.LocationContext.LocationCauseContext;
import furgl.customizations.common.customizations.context.event.LocationContext.LocationTargetContext;
import furgl.customizations.common.customizations.context.event.PlayerContext.PlayerCauseContext;
import furgl.customizations.common.customizations.context.event.PlayerContext.PlayerTargetContext;
import furgl.customizations.common.customizations.context.event.WorldContext.WorldCauseContext;
import furgl.customizations.common.customizations.context.event.WorldContext.WorldTargetContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ContextHelper {
	
	public static final String PLACEHOLDER_CHAR = "%";

	/**Parse placeholders in this string with this context*/
	public static String parse(String str, Context... eventContexts) {
		ServerPlayerEntity player = Contexts.get(Contexts.CAUSE_PLAYER, eventContexts).map(ctx -> ctx.getPlayer()).orElse(Contexts.get(Contexts.TARGET_PLAYER, eventContexts).map(ctx -> ctx.getPlayer()).orElse(null));
		return parse(str, player, eventContexts);
	}
	
	public static String parse(String str, @Nullable ServerPlayerEntity player, Context... eventContexts) {
		if (Customizations.server != null) {
			// replace our placeholders
			for (Context ctx : eventContexts)
				for (Entry<String, Function<Context[], String>> entry : ctx.getPlaceholders().entrySet())
					str = str.replaceAll(PLACEHOLDER_CHAR+entry.getKey()+PLACEHOLDER_CHAR, entry.getValue().apply(eventContexts));
			// replace papi placeholders
			if (player != null)
				return PlaceholderAPI.parseText(Text.of(str), player).getString();
			else
				return PlaceholderAPI.parseText(Text.of(str), Customizations.server).getString();
		}
		else
			return str;
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

	public static List<Context> getCauseContext(@Nullable PlayerEntity player) {
		List<Context> list = Lists.newArrayList(new PlayerCauseContext(player));
		list.addAll(getCauseContext((Entity)player));
		return list;
	}

	private static List<Context> getCauseContext(Entity entity) {
		List<Context> list = Lists.newArrayList(new EntityCauseContext(entity));
		list.addAll(getCauseContext(entity == null ? null : entity.world, entity == null ? null : entity.getPos()));
		return list;
	}

	public static List<Context> getCauseContext(World world, BlockPos pos) {
		return getCauseContext(world, pos == null ? null : new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
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
		list.addAll(getTargetContext(entity == null ? null : entity.world, entity == null ? null : entity.getPos()));
		return list;
	}

	public static List<Context> getTargetContext(World world, BlockPos pos) {
		return getTargetContext(world, pos == null ? null : new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
	}

	public static List<Context> getTargetContext(World world, Vec3d pos) {
		return Lists.newArrayList(new WorldTargetContext(world), new LocationTargetContext(pos));
	}

}