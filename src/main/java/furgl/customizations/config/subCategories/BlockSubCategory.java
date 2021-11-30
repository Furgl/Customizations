package furgl.customizations.config.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.config.parts.BlockPart;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.ContextHolder;

public class BlockSubCategory extends SubCategory {

	private ContextHolder contextHolder;

	public BlockSubCategory(Customization customization, ContextHolder contextHolder) {
		super("block", customization);
		this.contextHolder = contextHolder;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new BlockPart(this.getCustomization(), this.contextHolder));
	}

}