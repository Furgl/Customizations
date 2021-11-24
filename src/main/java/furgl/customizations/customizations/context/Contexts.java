package furgl.customizations.customizations.context;

import java.util.HashSet;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.Blocks;

public class Contexts {

	public static final HashSet<Context> ALL_CONTEXTS = Sets.newHashSet();
	
	public static final BlockContext BLOCK = new BlockContext(Blocks.AIR);
	public static final ConsoleMessageContext CONSOLE_MESSAGE = new ConsoleMessageContext("");
	public static final RandomContext RANDOM = new RandomContext();
	public static final ServerCommandContext SERVER_COMMAND = new ServerCommandContext("");
	
	@Nullable
	public static Context get(String id) {
		for (Context context : ALL_CONTEXTS)
			if (context.getId().equals(id))
				return context;
		return null;
	}
	
}