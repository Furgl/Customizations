package furgl.customizations.common.customizations.context.event;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.context.Context;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerContext extends EventContext {

	@Nullable
	private UUID uuid;
	@Nullable
	private String name;

	public PlayerContext(Type type, PlayerEntity player) {
		this(type);
		this.uuid = player == null ? null : player.getUuid();
		this.name = player == null ? null : player.getEntityName();
	}
	
	public PlayerContext() {
		this(null);
	}

	public PlayerContext(Type type) {
		super(type);
		this.variables.add(new Context.Variable("UUID", 
				() -> this.uuid, 
				value -> this.uuid = (UUID) value,
				value -> value.toString(), 
				value -> {
					try {
						return UUID.fromString((String) value);
					} 
					catch(Exception e) {
						return null;
					}
				}));
		this.variables.add(new Context.Variable("Name", 
				() -> this.name, 
				value -> this.name = (String) value,
				value -> value, 
				value -> value));
	}

	@Nullable
	public ServerPlayerEntity getPlayer() {
		ServerPlayerEntity player = null;
		if (Customizations.server != null) {
			if (this.uuid != null)
				player = Customizations.server.getPlayerManager().getPlayer(this.uuid);
			if (player == null && this.name != null)
				player = Customizations.server.getPlayerManager().getPlayer(this.name);
		}
		return player;
	}

}