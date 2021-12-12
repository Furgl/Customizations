package furgl.customizations.common.mixin.triggers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	
	@Shadow
	public ServerPlayerEntity player;

	@Inject(method="onDisconnected(Lnet/minecraft/text/Text;)V", at=@At(value="HEAD"))
	public void disconnect(Text reason, CallbackInfo ci) {
		//CustomizationManager.trigger(Selectables.TRIGGER_PLAYER_LOGOUT, ContextHelper.getCauseContext(player), new DisconnectReasonContext(reason.getString()));
	}
	
}