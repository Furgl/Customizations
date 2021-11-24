package furgl.customizations.customizations.actions;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.Customizations;
import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.parts.ServerCommandPart;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.customizations.context.ContextHolder;
import furgl.customizations.customizations.context.Contexts;
import furgl.customizations.customizations.context.ServerCommandContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ServerCommandAction extends SelectableType {

	protected ServerCommandAction() {
		super(SelectableType.Type.ACTION, "serverCommand", new ItemStack(Items.COMMAND_BLOCK));
	}

	@Override
	public void activate(ArrayList<Context> contexts, Context... eventContext) {
		for (Context context : contexts)
			if (context.equals(Contexts.SERVER_COMMAND) && Customizations.server != null)
				Customizations.server.getCommandManager().execute(Customizations.server.getCommandSource(), ((ServerCommandContext)context).command);
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new ServerCommandPart(customization, contextHolder));
	}

}