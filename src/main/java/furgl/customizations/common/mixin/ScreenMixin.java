package furgl.customizations.common.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import furgl.customizations.client.config.ConfigHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(Screen.class)
public class ScreenMixin {

	@Inject(method="renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"),
			locals = LocalCapture.CAPTURE_FAILSOFT)
	private void renderTooltipResize(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci, int i, int j, int l, int m) {
		if (ConfigHelper.isCustomizationConfigOpen() && m < 0) {
			float scale = (float)((Screen)(Object)this).height / (float)(j + 12);
			matrices.scale(scale, scale, 1);
			matrices.translate((1f-scale)*(l + i + 3), -m+6, 0); // x translate isn't perfect, but it's close enough
		}
	}

}