package furgl.customizations.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import furgl.customizations.customizations.CustomizationManager;
import furgl.customizations.customizations.context.BlockContext;
import furgl.customizations.customizations.context.PlayerContext;
import furgl.customizations.customizations.triggers.TriggerTypes;
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
			CustomizationManager.trigger(TriggerTypes.BREAK_BLOCK, new PlayerContext(player), new BlockContext(blockState.getBlock()));
	}
	
}