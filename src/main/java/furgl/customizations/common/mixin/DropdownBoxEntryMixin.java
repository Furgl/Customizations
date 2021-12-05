package furgl.customizations.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import furgl.customizations.client.config.ConfigHelper;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;

@Mixin(DropdownBoxEntry.class)
public class DropdownBoxEntryMixin {

	/**Expand dropdown box width*/
	@ModifyConstant(method = "render", constant = @Constant(intValue = 150))
	public int renderFix(int in) {
		if (ConfigHelper.isCustomizationConfigOpen())
			return ConfigHelper.FIELD_WIDTH;
		else
			return in;
	}

}