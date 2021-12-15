package furgl.customizations.common.customizations.actions;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import com.google.common.collect.Sets;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.context.holders.Subject;
import furgl.customizations.common.customizations.selectables.SelectableAction;
import net.minecraft.item.ItemStack;

public class ConsoleMessageAction extends SelectableAction {

	public ConsoleMessageAction(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(null, id, stack, relatedElements);
	}

	@Override
	public Set<Subject> activate(Context[] configContexts, Context[] eventContexts) { 
		Contexts.get(Contexts.CONSOLE_MESSAGE, configContexts).ifPresent(context -> 
		Customizations.LOGGER.info(ContextHelper.parse(context.message, eventContexts)));
		return Sets.newHashSet();
	}

}