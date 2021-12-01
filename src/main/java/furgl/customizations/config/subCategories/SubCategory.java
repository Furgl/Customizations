package furgl.customizations.config.subCategories;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import furgl.customizations.config.ConfigHelper;
import furgl.customizations.config.elements.ConfigElement;
import furgl.customizations.customizations.Customization;
import furgl.customizations.customizations.context.Context;
import furgl.customizations.impl.IAbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public abstract class SubCategory extends ConfigElement {

	private List<ConfigElement> children;

	protected SubCategory(String id, Customization customization) {
		super(id, customization);
	}

	@Override
	protected List<AbstractConfigListEntry> addToConfig(ConfigBuilder builder) {
		List<AbstractConfigListEntry> children = Lists.newArrayList();
		this.getChildren().stream()
		.map(element -> element.getOrCreateConfigEntries(builder))
		.forEach(list -> children.addAll(list));
		this.mainConfigEntry = ConfigHelper
				.createSubCategory(builder, this.getName(), children, false)
				.build();

		return Lists.newArrayList(this.mainConfigEntry);
	}

	/**Get other ConfigElements that are in this sub category*/
	public List<ConfigElement> getChildren() {
		if (this.children == null)
			this.children = this.createChildren();
		return this.children;
	}

	/**Create other ConfigElements that are in this sub category*/
	protected abstract List<ConfigElement> createChildren();

	/**Called when this controlling list item is renamed*/
	public void updateName(String name) {
		((IAbstractConfigListEntry)this.getMainConfigEntry()).setFieldName(Text.of(name));
	}
	
	@Override
	public ArrayList<Context> getRelatedContexts() {
		ArrayList<Context> contexts = Lists.newArrayList();
		for (ConfigElement element : this.getChildren())
			contexts.addAll(element.getRelatedContexts());
		return contexts;
	}

}