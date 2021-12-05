package furgl.customizations.common.customizations.triggers;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.Context;
import furgl.customizations.common.customizations.context.ContextHelper;
import furgl.customizations.common.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class TriggerPlayerLogout extends SelectableTrigger {

	public TriggerPlayerLogout(String id, ItemStack stack, BiFunction<Customization, ContextHolder, List<ConfigElement>> relatedElements) {
		super(id, stack, relatedElements);
	}

	@Override
	public List<Context> getContextForPlaceholders() {
		return getContext(null);
	}

	public List<Context> getContext(ServerPlayerEntity player) {
		List<Context> ctxs = Lists.newArrayList(); 
		ctxs.addAll(ContextHelper.getCauseContext(player));
		return ctxs;
	}

}