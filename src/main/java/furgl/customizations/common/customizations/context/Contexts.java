package furgl.customizations.common.customizations.context;

import java.util.HashSet;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import furgl.customizations.common.customizations.context.event.DisconnectReasonContext;
import furgl.customizations.common.customizations.context.event.EntityContext.EntityCauseContext;
import furgl.customizations.common.customizations.context.event.EntityContext.EntityTargetContext;
import furgl.customizations.common.customizations.context.event.LocationContext.LocationCauseContext;
import furgl.customizations.common.customizations.context.event.LocationContext.LocationTargetContext;
import furgl.customizations.common.customizations.context.event.PlayerContext.PlayerCauseContext;
import furgl.customizations.common.customizations.context.event.PlayerContext.PlayerTargetContext;
import furgl.customizations.common.customizations.context.event.WorldContext.WorldCauseContext;
import furgl.customizations.common.customizations.context.event.WorldContext.WorldTargetContext;
import net.minecraft.block.Blocks;

public class Contexts {

	public static final HashSet<Context> ALL_CONTEXTS = Sets.newHashSet();
	
	// cause
	public static final EntityCauseContext CAUSE_ENTITY = new EntityCauseContext();
	public static final PlayerCauseContext CAUSE_PLAYER = new PlayerCauseContext();
	public static final LocationCauseContext CAUSE_LOCATION = new LocationCauseContext();
	public static final WorldCauseContext CAUSE_WORLD = new WorldCauseContext();
	// target
	public static final EntityTargetContext TARGET_ENTITY = new EntityTargetContext();
	public static final PlayerTargetContext TARGET_PLAYER = new PlayerTargetContext();
	public static final LocationTargetContext TARGET_LOCATION = new LocationTargetContext();
	public static final WorldTargetContext TARGET_WORLD = new WorldTargetContext();
	// other
	public static final BlockContext BLOCK = new BlockContext(Blocks.AIR);
	public static final ConsoleMessageContext CONSOLE_MESSAGE = new ConsoleMessageContext("");
	public static final RandomContext RANDOM = new RandomContext();
	public static final CommandContext COMMAND = new CommandContext("");
	public static final HealOrDamageContext HEAL_OR_DAMAGE_AMOUNT = new HealOrDamageContext(0, 0);
	public static final SelectedEntityContext SELECTED_ENTITY = new SelectedEntityContext();
	public static final DisconnectReasonContext DISCONNECT_REASON = new DisconnectReasonContext();
	
	@Nullable
	public static Context get(String id) {
		for (Context context : ALL_CONTEXTS)
			if (context.getId().equals(id))
				return context;
		return null;
	}

	public static <T extends Context> Optional<T> get(T contextIn, Context[] contexts) {
		for (Context context : contexts)
			if (contextIn.equals(context))
				return Optional.of((T) context);
		return Optional.empty();
	}
	
}