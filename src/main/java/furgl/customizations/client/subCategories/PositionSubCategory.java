package furgl.customizations.client.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.PositionSelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;

public class PositionSubCategory extends SubCategory {

	private ConfigContextHolder contextHolder;

	public PositionSubCategory(Customization customization, ConfigContextHolder contextHolder) {
		super("position", customization);
		this.contextHolder = contextHolder;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new PositionSelector(this.getCustomization(), this.contextHolder));
	}

}