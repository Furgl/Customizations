package furgl.customizations.customizations.context;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.Customizations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class PlayerContext extends Context {

	@Nullable
	private UUID uuid;
	@Nullable
	private String name;

	public PlayerContext(PlayerEntity player) {
		this(player.getUuid());
	}

	public PlayerContext(UUID uuid) {
		this();
		this.uuid = uuid;
	}

	public PlayerContext(String name) {
		this();
		this.name = name;
	}

	public PlayerContext() {
		super();
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

	@Override
	public boolean test(Context eventContext) {
		if (this.name == null && this.uuid == null)
			return true;
		if (eventContext instanceof PlayerContext) {
			if (this.name != null && ((PlayerContext)eventContext).name.equals(this.name))
				return true;
			else if (this.uuid != null && ((PlayerContext)eventContext).uuid.equals(this.uuid))
				return true;
		}
		return false;
	}

	public PlayerEntity getPlayer(@Nullable World world) {
		if (world == null) {
			if (Customizations.server != null) {
				if (this.uuid != null)
					return Customizations.server.getPlayerManager().getPlayer(this.uuid);
				if (this.name != null)
					return Customizations.server.getPlayerManager().getPlayer(this.name);
			}
		}
		else {
			if (this.uuid != null)
				return world.getPlayerByUuid(uuid);
			if (this.name != null)
				for (PlayerEntity player : world.getPlayers())
					if (player.getName().getString().equals(this.name))
						return player;
		}
		return null;
	}	

	public static class PlayerCauseContext extends PlayerContext {
		public PlayerCauseContext(PlayerEntity player) {
			super(player);
		}
		public PlayerCauseContext() {
			super();
		}
	}
	public static class PlayerTargetContext extends PlayerContext {
		public PlayerTargetContext(PlayerEntity player) {
			super(player);
		}
		public PlayerTargetContext() {
			super();
		}
	}

}