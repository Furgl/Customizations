package furgl.customizations.common.customizations.triggers;

import java.util.List;
import java.util.function.BiFunction;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;
import furgl.customizations.common.customizations.selectables.SelectableTrigger;
import net.minecraft.item.ItemStack;

public class PlayerLogoutTrigger extends SelectableTrigger {

	public PlayerLogoutTrigger(String id, ItemStack stack, BiFunction<Customization, ConfigContextHolder, List<ConfigElement>> relatedElements) {
		super(null, null, null, id, stack, relatedElements);
	}

	/*@Override
	public List<Context> getPlaceholders() {
		return getContext(null);
	}
	
	public List<Context> getContext(ServerPlayerEntity player) {
		List<Context> ctxs = Lists.newArrayList(); 
		ctxs.addAll(ContextHelper.getCauseContext(player));
		return ctxs;
	}*/

}