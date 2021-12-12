package furgl.customizations.common.customizations.selectables;

import java.util.List;
import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.CustomizationManager;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.holders.Cause;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.context.holders.Other;
import furgl.customizations.common.customizations.context.holders.Target;
import net.minecraft.item.ItemStack;

public class SelectableTrigger extends Selectable {

	public SelectableTrigger(@Nullable Class causeClass, @Nullable Class targetClass, Object[] otherClasses, String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
		Selectables.ALL_TRIGGERS.add(this);
		if (causeClass != null)
			this.placeholderContextHolders.add(new Cause(causeClass));
		if (targetClass != null)
			this.placeholderContextHolders.add(new Target(targetClass));
		if (otherClasses != null && otherClasses.length > 0)
			this.placeholderContextHolders.add(new Other(otherClasses));
	}

	/**Trigger with this cause, target, and any extra Contexts*/
	public void trigger(Object cause, Object target, Context... extras) {
		CustomizationManager.trigger(this, new Cause(cause), new Target(target), new Other((Object[]) extras));
	}

}