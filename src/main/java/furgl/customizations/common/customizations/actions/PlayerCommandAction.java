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
import furgl.customizations.common.customizations.context.ContextHolder;
import furgl.customizations.common.customizations.context.Contexts;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCommandAction extends SelectableAction {

	public PlayerCommandAction(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
	}

	@Override
	public void activate(Context[] customizationContexts, Context[] eventContexts) {
		if (Customizations.server != null) {
			Set<Entity> entities = Contexts.get(Contexts.SELECTED_ENTITY, eventContexts).map(ctx -> ctx.selectedEntities).orElse(Sets.newHashSet());
			for (Entity entity : entities)
				if (entity instanceof ServerPlayerEntity)
					Contexts.get(Contexts.COMMAND, eventContexts)
					.ifPresent(context -> Customizations.server.getCommandManager().execute(entity.getCommandSource(), ContextHelper.parse(context.command, (ServerPlayerEntity)entity, eventContexts)));
		}
	}

}