package furgl.customizations.customizations.actions;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.ConfigElement;
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

	protected ConsoleMessageAction() {
		super(SelectableType.Type.ACTION, "consoleMessage", new ItemStack(Items.CHAIN_COMMAND_BLOCK));
	}

	@Override
	public void activate(ArrayList<Context> contexts, Context... eventContext) {
		for (Context context : contexts)
			if (context.equals(Contexts.CONSOLE_MESSAGE))
				Customizations.LOGGER.info(((ConsoleMessageContext)context).message);
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new ConsoleMessagePart(customization, contextHolder));
	}

}