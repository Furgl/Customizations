package furgl.customizations.common.customizations.selectables;

import java.util.List;
import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.context.holders.Subject;
import net.minecraft.item.ItemStack;

public abstract class SelectableAction extends Selectable {

	public SelectableAction(@Nullable Class subjectClass, String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
		Selectables.ALL_ACTIONS.add(this);
		if (subjectClass != null)
			this.placeholderContextHolders.add(new Subject(subjectClass));
	}
	
	/**Activate this action and parses for placeholders if necessary*/
	public abstract void activate(Context[] configContexts, Context[] eventContexts);
	
}