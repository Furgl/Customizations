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
	
	public static final EntityContext ENTITY = new EntityContext();
	public static final PlayerContext PLAYER = new PlayerContext();
	public static final LocationContext LOCATION = new LocationContext();
	public static final WorldContext WORLD = new WorldContext();
	public static final BlockContext BLOCK = new BlockContext(Blocks.AIR);
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
		return get(contextIn, null, contexts);
	}

	public static <T extends Context> Optional<T> get(T contextIn, @Nullable Type type, Context[] contexts) {
		for (Context context : contexts)
			if (contextIn.equals(context) && (type == null || context.type == type))
				return Optional.of((T) context);
		return Optional.empty();
	}
	
}