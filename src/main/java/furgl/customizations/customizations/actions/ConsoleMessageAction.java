package furgl.customizations.customizations.actions;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.parts.ConsoleMessagePart;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ConsoleMessageContext;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ConsoleMessageAction extends SelectableType {

	public ConsoleMessageAction() {
		super("actions.consoleMessage", new ItemStack(Items.CHAIN_COMMAND_BLOCK));
	}

	@Override
	public void activate(Context[] actionContexts, Context[] triggerContexts) {
		Contexts.get(Contexts.CONSOLE_MESSAGE, actionContexts).ifPresent(context -> Customizations.LOGGER.info(((ConsoleMessageContext)context).message));
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new ConsoleMessagePart(customization, contextHolder));
	}

}