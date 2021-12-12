package furgl.customizations.common.customizations.selectables;

import java.util.List;
import java.util.function.BiFunction;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import net.minecraft.item.ItemStack;

public class SelectableCondition extends Selectable {

	public SelectableCondition(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
		Selectables.ALL_CONDITIONS.add(this);
	}

}