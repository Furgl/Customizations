package furgl.customizations.common.customizations.context;

import java.util.HashSet;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import furgl.customizations.common.customizations.context.Context.Type;
import furgl.customizations.common.customizations.context.event.DisconnectReasonContext;
import furgl.customizations.common.customizations.context.event.EntityContext;
import furgl.customizations.common.customizations.context.event.LocationContext;
import furgl.customizations.common.customizations.context.event.PlayerContext;
import furgl.customizations.common.customizations.context.event.WorldContext;
import net.minecraft.block.Blocks;

public class Contexts {

	public static final HashSet<Context> ALL_CONTEXTS = Sets.newHashSet();
	
	// cause
	public static final EntityContext CAUSE_ENTITY = new EntityContext(Type.CAUSE);
	public static final PlayerContext CAUSE_PLAYER = new PlayerContext(Type.CAUSE);
	public static final LocationContext CAUSE_LOCATION = new LocationContext(Type.CAUSE);
	public static final WorldContext CAUSE_WORLD = new WorldContext(Type.CAUSE);
	// target
	public static final EntityContext TARGET_ENTITY = new EntityContext(Type.TARGET);
	public static final PlayerContext TARGET_PLAYER = new PlayerContext(Type.TARGET);
	public static final LocationContext TARGET_LOCATION = new LocationContext(Type.TARGET);
	public static final WorldContext TARGET_WORLD = new WorldContext(Type.TARGET);
	// subject
	public static final EntityContext SUBJECT_ENTITY = new EntityContext(Type.SUBJECT);
	public static final PlayerContext SUBJECT_PLAYER = new PlayerContext(Type.SUBJECT);
	public static final LocationContext SUBJECT_LOCATION = new LocationContext(Type.SUBJECT);
	public static final WorldContext SUBJECT_WORLD = new WorldContext(Type.SUBJECT);
	// other
	public static final BlockContext BLOCK = new BlockContext(null, Blocks.AIR);
	public static final ConsoleMessageContext CONSOLE_MESSAGE = new ConsoleMessageContext("");
	public static final ChatMessageContext CHAT_MESSAGE = new ChatMessageContext("");
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