package furgl.customizations.common.mixin.triggers;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.customizations.CustomizationManager;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	
	@Shadow @Final
	protected ServerPlayerEntity player;

	@Inject(method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void tryBreakBlockListener(BlockPos pos, CallbackInfoReturnable<Boolean> ci, BlockState blockState) {
		if (ci.getReturnValueZ()) 
			CustomizationManager.trigger(Selectables.TRIGGER_BREAK_BLOCK, 
					Selectables.TRIGGER_BREAK_BLOCK.getContext(player, player.world, pos, blockState.getBlock()));
	}

}