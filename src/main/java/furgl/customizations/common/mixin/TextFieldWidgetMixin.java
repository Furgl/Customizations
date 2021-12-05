package furgl.customizations.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {

	/**Clear text and select text field when right-clicking*/
	@Inject(method = "mouseClicked", at = @At(value = "RETURN"), cancellable = true)
	public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
		TextFieldWidget text = (TextFieldWidget)(Object)this;
		boolean mouseOver = mouseX >= (double)text.x && mouseX < (double)(text.x + text.getWidth()) && mouseY >= (double)text.y && mouseY < (double)(text.y + text.getHeight());
		if (mouseOver && text.isVisible() && text.isFocused() && button == 1) {
			text.setText("");
			ci.setReturnValue(true);
		}
	}

}