package furgl.customizations.common.customizations.actions;

import java.util.List;
import java.util.function.BiFunction;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.Customizations;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.SelectableAction;
import net.minecraft.item.ItemStack;

public class ServerCommandAction extends SelectableAction {

	public ServerCommandAction(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(null, id, stack, relatedElements);
	}

	@Override
	public void activate(Context[] configContexts, Context[] eventContexts) {
		if (Customizations.server != null)
			Contexts.get(Contexts.COMMAND, configContexts)
			.ifPresent(context -> Customizations.server.getCommandManager().execute(Customizations.server.getCommandSource(), ContextHelper.parse(context.command, eventContexts)));
	}

}