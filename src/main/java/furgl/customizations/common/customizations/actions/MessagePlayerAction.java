package furgl.customizations.common.customizations.actions;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import com.google.common.collect.Sets;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.Contexts;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.context.holders.Subject;
import furgl.customizations.common.customizations.selectables.SelectableAction;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MessagePlayerAction extends SelectableAction {

	public MessagePlayerAction(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(ServerPlayerEntity.class, id, stack, relatedElements);
	}

	@Override
	public void activate(Context[] configContexts, Context[] eventContexts) {
		Set<Entity> entities = Contexts.get(Contexts.SELECTED_ENTITY, configContexts).map(ctx -> ctx.getEntity(configContexts)).orElse(Sets.newHashSet());
		Contexts.get(Contexts.CHAT_MESSAGE, configContexts).ifPresent(context -> {
			for (Entity entity : entities) {
				if (entity instanceof ServerPlayerEntity)
					((ServerPlayerEntity)entity).sendMessage(Text.of(ContextHelper.parse(context.message, new Subject(entity), eventContexts)), false);
			}
		});
	}

}