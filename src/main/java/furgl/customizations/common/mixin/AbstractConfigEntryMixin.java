package furgl.customizations.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import furgl.customizations.client.config.ConfigHelper;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import net.minecraft.text.Text;

@Mixin(AbstractConfigEntry.class)
public class AbstractConfigEntryMixin {

	/**Custom colors for field types*/
	@Inject(method = "getDisplayedFieldName", at = @At(value = "HEAD"), cancellable = true)
	public void getDisplayedFieldNameChange(CallbackInfoReturnable<Text> ci) {
		if (ConfigHelper.isCustomizationConfigOpen())
			ci.setReturnValue(ConfigHelper.getDisplayedFieldName((AbstractConfigEntry)(Object)this));
	}
	
}