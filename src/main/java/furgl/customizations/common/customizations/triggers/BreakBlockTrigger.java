package furgl.customizations.common.customizations.triggers;

import java.util.List;
import java.util.function.BiFunction;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.SelectableTrigger;
import net.minecraft.item.ItemStack;

public class BreakBlockTrigger extends SelectableTrigger {

	public BreakBlockTrigger(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(null, null, null, id, stack, relatedElements);
	}
	
}