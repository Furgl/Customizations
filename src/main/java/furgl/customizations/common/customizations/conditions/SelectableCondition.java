package furgl.customizations.common.customizations.conditions;

import java.util.List;
import java.util.function.BiFunction;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.Selectable;
import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;

public class SelectableCondition extends Selectable {

	public SelectableCondition(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
		Selectables.ALL_CONDITIONS.add(this);
	}

}