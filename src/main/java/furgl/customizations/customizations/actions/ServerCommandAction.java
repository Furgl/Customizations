package furgl.customizations.customizations.actions;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.parts.CommandPart;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ServerCommandAction extends SelectableType {

	public ServerCommandAction() {
		super("actions.serverCommand", new ItemStack(Items.COMMAND_BLOCK));
	}

	@Override
	public void activate(Context... contexts) {
		if (Customizations.server != null)
			Contexts.get(Contexts.COMMAND, contexts)
			.ifPresent(context -> Customizations.server.getCommandManager().execute(Customizations.server.getCommandSource(), context.command));
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new CommandPart(customization, contextHolder));
	}

}