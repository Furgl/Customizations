package furgl.customizations.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import furgl.customizations.client.config.ConfigHelper;
import me.shedaniel.clothconfig2.gui.entries.TextFieldListEntry;

@Mixin(TextFieldListEntry.class)
public abstract class TextFieldListEntryMixin {

	/**Expand text field width*/
	@ModifyConstant(method="render", constant=@Constant(intValue=148))
	public int renderFix(int prevValue) {
		if (ConfigHelper.isCustomizationConfigOpen())
			return ConfigHelper.FIELD_WIDTH - 2;
		else
			return prevValue;
	}
	
}
