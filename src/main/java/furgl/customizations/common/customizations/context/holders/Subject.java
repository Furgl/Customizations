package furgl.customizations.common.customizations.context.holders;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.common.customizations.context.Context.Type;
import net.minecraft.server.network.ServerPlayerEntity;

public class Subject<S> extends EventContextHolder {

	@Nullable
	public ServerPlayerEntity player;
	
	public Subject(S subject) {
		super(Type.SUBJECT, subject);
		if (subject instanceof ServerPlayerEntity)
			this.player = (ServerPlayerEntity) subject;
	}

}