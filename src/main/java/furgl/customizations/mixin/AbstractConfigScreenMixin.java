package furgl.customizations.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import furgl.customizations.impl.IAbstractConfigScreen;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;

@Mixin(AbstractConfigScreen.class)
public class AbstractConfigScreenMixin implements IAbstractConfigScreen {

	@Shadow @Final
	private List<Tooltip> tooltips;
	
	@Override
	public void setTooltip(Tooltip tooltip) {
		this.tooltips.clear();
		this.tooltips.add(tooltip);
	}

	
}