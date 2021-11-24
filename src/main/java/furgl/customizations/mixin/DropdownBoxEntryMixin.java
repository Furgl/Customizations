package furgl.customizations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import furgl.customizations.config.Config;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.client.MinecraftClient;

@Mixin(DropdownBoxEntry.class)
public class DropdownBoxEntryMixin {

	/**Expand dropdown box width*/
	@ModifyConstant(method = "render", constant = @Constant(intValue = 150))
	public int renderFix(int in) {
		if (MinecraftClient.getInstance().currentScreen == Config.currentScreen)
			return 200;
		else
			return in;
	}

}