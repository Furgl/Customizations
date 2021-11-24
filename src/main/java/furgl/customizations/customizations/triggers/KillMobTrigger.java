package furgl.customizations.customizations.triggers;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class KillMobTrigger extends SelectableType {

	protected KillMobTrigger() {
		super(SelectableType.Type.TRIGGER, "killMob", new ItemStack(Items.DIAMOND_SWORD));
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList();
	}
	
}