package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.selectors.EntitySelector;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;

public class EntitySubCategory extends SubCategory {

	private ContextHolder contextHolder;

	public EntitySubCategory(Customization customization, ContextHolder contextHolder) {
		super("entity", customization);
		this.contextHolder = contextHolder;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new EntitySelector(this.getCustomization(), this.contextHolder));
	}

}