package furgl.customizations.customizations.conditions;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.parts.RandomPart;
import furgl.customizations.config.selectors.SelectableType;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public class RandomCondition extends SelectableType {

	public RandomCondition() {
		super("conditions.random", new ItemStack(Blocks.DISPENSER));
	}

	@Override
	public List<ConfigElement> createRelatedElements(Customization customization, ContextHolder contextHolder) {
		return Lists.newArrayList(new RandomPart(customization, contextHolder));
	}

}