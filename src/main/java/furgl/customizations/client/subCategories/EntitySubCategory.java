package furgl.customizations.client.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.selectors.EntitySelector;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;

public class EntitySubCategory extends SubCategory {

	private ConfigContextHolder contextHolder;

	public EntitySubCategory(Customization customization, ConfigContextHolder contextHolder) {
		super("entity", customization);
		this.contextHolder = contextHolder;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new EntitySelector(this.getCustomization(), this.contextHolder));
	}

}