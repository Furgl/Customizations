package furgl.customizations.common.mixin.triggers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.customizations.CustomizationManager;
import furgl.customizations.common.customizations.context.ContextHelper;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Inject(method="onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at=@At(value="RETURN"))
	public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		CustomizationManager.trigger(Selectables.TRIGGER_PLAYER_LOGIN, ContextHelper.getCauseContext(player));
	}
	
}