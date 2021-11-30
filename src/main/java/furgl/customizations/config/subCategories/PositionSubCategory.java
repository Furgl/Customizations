package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.selectors.PositionSelector;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;

public class PositionSubCategory extends SubCategory {

	private ContextHolder contextHolder;

	public PositionSubCategory(Customization customization, ContextHolder contextHolder) {
		super("position", customization);
		this.contextHolder = contextHolder;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new PositionSelector(this.getCustomization(), this.contextHolder));
	}

}