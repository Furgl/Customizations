package furgl.customizations.client.subCategories;

import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.client.elements.ConfigElement;
import furgl.customizations.client.parts.BlockPart;
import furgl.customizations.common.customizations.Customization;
import furgl.customizations.common.customizations.context.holders.ConfigContextHolder;

public class BlockSubCategory extends SubCategory {

	private ConfigContextHolder contextHolder;

	public BlockSubCategory(Customization customization, ConfigContextHolder contextHolder) {
		super("block", customization);
		this.contextHolder = contextHolder;
	}

	@Override
	protected List<ConfigElement> createChildren() {
		return Lists.newArrayList(new BlockPart(this.getCustomization(), this.contextHolder));
	}

}