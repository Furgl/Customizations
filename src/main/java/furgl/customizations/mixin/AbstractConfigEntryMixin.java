package furgl.customizations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import furgl.customizations.config.Config;
import furgl.customizations.config.ConfigHelper;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

@Mixin(AbstractConfigEntry.class)
public class AbstractConfigEntryMixin {

	/**Custom colors for field types*/
	@Inject(method = "getDisplayedFieldName", at = @At(value = "HEAD"), cancellable = true)
	public void getDisplayedFieldNameChange(CallbackInfoReturnable<Text> ci) {
		if (MinecraftClient.getInstance().currentScreen == Config.currentScreen)
			ci.setReturnValue(ConfigHelper.getDisplayedFieldName((AbstractConfigEntry)(Object)this));
	}
	
}