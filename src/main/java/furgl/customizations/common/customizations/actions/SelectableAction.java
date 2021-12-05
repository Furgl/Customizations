package furgl.customizations.common.customizations.actions;

import java.util.List;
import java.util.function.BiFunction;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.Selectable;
import furgl.customizations.client.selectors.Selectables;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;

public abstract class SelectableAction extends Selectable {

	public SelectableAction(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
		Selectables.ALL_ACTIONS.add(this);
	}
	
	/**Activate this action and parses for placeholders if necessary*/
	public abstract void activate(Context[] customizationContexts, Context[] eventContexts);

}