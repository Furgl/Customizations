package furgl.customizations.customizations.triggers;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.ConfigElement;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.config.subCategories.BlockSubCategory;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BreakBlockTrigger extends SelectableType {

	protected BreakBlockTrigger() {
		super(SelectableType.Type.TRIGGER, "breakBlock", new ItemStack(Items.DIAMOND_PICKAXE));
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new BlockSubCategory(customization, contextHolder).getChildren());
	}
	
}